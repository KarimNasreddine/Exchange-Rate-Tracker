from app import db, ma


class Listing(db.Model):
    ListingID = db.Column(db.Integer, primary_key=True)
    PostingUserID = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    UserPhoneNumer = db.Column(db.String(20), nullable=False)
    MoneyAmount = db.Column(db.Float, nullable=False)
    Usd_To_Lbp = db.Column(db.Boolean, nullable=False)
    Resolved = db.Column(db.Boolean, nullable=True)
    ResolvedByUser = db.Column(db.Integer, nullable=True)

    # def __init__(self, PostingUserID, UserPhoneNumer, MoneyAmount, Usd_To_Lbp, Resolved, ResolvedByUser):
    #     super(Listing, self).__init__()


class ListingSchema(ma.Schema):
    class Meta:
        fields = ('ListingID', 'PostingUserID', 'UserPhoneNumer', 'MoneyAmount', 'Usd_To_Lbp', 'Resolved', 'ResolvedByUser')
        model = Listing

