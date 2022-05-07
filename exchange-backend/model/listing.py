from app import db, ma


class Listing(db.Model):
    listing_id = db.Column(db.Integer, primary_key=True)
    posting_user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    user_phone_number = db.Column(db.String(20), nullable=False)
    selling_amount = db.Column(db.Float, nullable=False)
    buying_amount = db.Column(db.Float, nullable=False)
    usd_to_lbp = db.Column(db.Boolean, nullable=False)
    resolved = db.Column(db.Boolean, nullable=True)
    resolved_by_user = db.Column(db.Integer, nullable=True)

    def __init__(self, posting_user_id, user_phone_number, selling_amount, buying_amount, usd_to_lbp, resolved, resolved_by_user):
        super(Listing, self).__init__(posting_user_id = posting_user_id,
                                      user_phone_number = user_phone_number,
                                      selling_amount = selling_amount,
                                      buying_amount = buying_amount,
                                      usd_to_lbp = usd_to_lbp,
                                      resolved = resolved,
                                      resolved_by_user = resolved_by_user)


class ListingSchema(ma.Schema):
    class Meta:
        fields = ('listing_id', 'posting_user_id', 'user_phone_number', 'selling_amount', 'buying_amount', 'usd_to_lbp', 'resolved', 'resolved_by_user')
        model = Listing

