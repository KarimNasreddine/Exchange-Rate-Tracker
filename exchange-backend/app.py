import datetime
import jwt

from flask import Flask
from flask import request
from flask import jsonify
from flask import abort

from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt

# Import the database configurations by creating a db_config.py file and
# adding the configurations in DB_CONFIG variable
from .db_config import DB_CONFIG

app = Flask(__name__)

ma = Marshmallow(app)

bcrypt = Bcrypt(app)

app.config['SQLALCHEMY_DATABASE_URI'] = DB_CONFIG
CORS(app)
db = SQLAlchemy(app)

from .model.user import User, UserSchema
from .model.transaction import Transaction, TransactionSchema

SECRET_KEY = "b'|\xe7\xbfU3`\xc4\xec\xa7\xa9zf:}\xb5\xc7\xb9\x139^3@Dv'"

transaction_schema = TransactionSchema()
transactions_schema = TransactionSchema(many=True)
user_schema = UserSchema()


@app.route('/transaction', methods=['POST'])
def new_transaction():
    auth_token = extract_auth_token(request)

    usd_amount = request.json["usd_amount"]
    lbp_amount = request.json["lbp_amount"]
    usd_to_lbp = request.json["usd_to_lbp"]

    try:
        transaction = Transaction(usd_amount=usd_amount,
                                  lbp_amount=lbp_amount,
                                  usd_to_lbp=usd_to_lbp,
                                  user_id=decode_token(auth_token) if auth_token else None,
                                  )
        db.session.add(transaction)
        db.session.commit()
    except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
        abort(403)

    return jsonify(transaction_schema.dump(transaction))


@app.route('/transaction', methods=['GET'])
def get_user_transactions():
    auth_token = extract_auth_token(request)

    if not auth_token:
        abort(403)

    user_id = decode_token(auth_token)
    all_user_transactions = Transaction.query.filter(Transaction.user_id == user_id).all()

    return jsonify(transactions_schema.dump(all_user_transactions))


@app.route('/exchangeRate', methods=['GET'])
def get_rate():
    all_usd_to_lbp = Transaction.query.filter(
        Transaction.added_date.between(datetime.datetime.now() - datetime.timedelta(days=3), datetime.datetime.now()),
        Transaction.usd_to_lbp == True).all()
    all_lbp_to_usd = Transaction.query.filter(
        Transaction.added_date.between(datetime.datetime.now() - datetime.timedelta(days=3), datetime.datetime.now()),
        Transaction.usd_to_lbp == False).all()

    AVERAGE_USD_TO_LBP = find_average(all_usd_to_lbp)
    AVERAGE_LBP_TO_USD = find_average(all_lbp_to_usd)

    rate = {
        "usd_to_lbp": AVERAGE_USD_TO_LBP,
        "lbp_to_usd": AVERAGE_LBP_TO_USD
    }

    return jsonify(rate)


@app.route('/user', methods=['POST'])
def new_user():
    user_name = request.json["user_name"]
    password = request.json["password"]

    user = User(user_name=user_name, password=password)

    db.session.add(user)
    db.session.commit()

    return jsonify(user_schema.dump(user))


@app.route('/authentication', methods=['POST'])
def authenticate():
    user_name = request.json["user_name"]
    password = request.json["password"]

    if user_name is None or password is None or len(user_name) == 0 or len(password) == 0:
        abort(400)

    user = User.query.filter_by(user_name=user_name).first()
    if user is None:
        abort(403)

    if not bcrypt.check_password_hash(user.hashed_password, password):
        abort(403)

    token = create_token(user.id)

    return jsonify(
        {
            "token": token
        }
    )
   
@app.route('/graph', methods=['GET'])
def get_daily_rate():
    transactions = Transaction.query.all()

    usd_to_lbp_dict = {}
    lbp_to_usd_dict = {}

    #todays date
    today = datetime.datetime.now()
    #adding today and last 10 days to each dictionary
    for i in range(10):
        usd_to_lbp_dict[today.strftime("%Y-%m-%d")] = 0
        lbp_to_usd_dict[today.strftime("%Y-%m-%d")] = 0
        today = today - datetime.timedelta(days=1)

    #function to get the rate of usd_to_lbp transactions of certain day
    def get_usd_to_lbp_rate(date):
        daily_rate=[]
        for i in transactions:
            if i.usd_to_lbp == True and i.added_date.strftime("%Y-%m-%d") == date:
                daily_rate.append(i.lbp_amount / i.usd_amount)
        if len(daily_rate) == 0:
            return 0
        return sum(daily_rate) / len(daily_rate)
    
    #function to get the rate of lbp_to_usd transactions of certain day
    def get_lbp_to_usd_rate(date):
        daily_rate=[]
        for i in transactions:
            if i.usd_to_lbp == False and i.added_date.strftime("%Y-%m-%d") == date:
                daily_rate.append(i.lbp_amount / i.usd_amount)
        if len(daily_rate) == 0:
            return 0
        return sum(daily_rate) / len(daily_rate)

    #loop over each key of usd_to_lbp_dict and get the rate of usd_to_lbp transactions of that day
    for key in usd_to_lbp_dict:
        usd_to_lbp_dict[key] = get_usd_to_lbp_rate(key)
    
    #loop over each key of lbp_to_usd_dict and get the rate of lbp_to_usd transactions of that day
    for key in lbp_to_usd_dict:
        lbp_to_usd_dict[key] = get_lbp_to_usd_rate(key)

    return jsonify({'sell':usd_to_lbp_dict, 'buy':lbp_to_usd_dict})


def find_average(transactions):
    average = 0
    if len(transactions) == 0:
        return None
    else:
        for transaction in transactions:
            average += (transaction.lbp_amount / transaction.usd_amount)
        average /= len(transactions)
    return average


def create_token(user_id):
    payload = {
        'exp': datetime.datetime.utcnow() + datetime.timedelta(days=4),
        'iat': datetime.datetime.utcnow(),
        'sub': user_id
    }
    return jwt.encode(
        payload,
        SECRET_KEY,
        algorithm='HS256'
    )


def extract_auth_token(authenticated_request):
    auth_header = authenticated_request.headers.get('Authorization')
    if auth_header:
        return auth_header.split(" ")[1]
    else:
        return None


def decode_token(token):
    payload = jwt.decode(token, SECRET_KEY, 'HS256')
    return payload['sub']
