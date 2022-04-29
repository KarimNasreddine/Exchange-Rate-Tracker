package com.maarouf.currencyexchange.api.model

import com.google.gson.annotations.SerializedName




class ListingsData {
    @SerializedName("listing_id")
    var listingId: Int? = null

    @SerializedName("posting_user_id")
    var postingUserId: Int? = null

    @SerializedName("user_phone_number")
    var userPhoneNumber: String? = null

    @SerializedName("selling_amount")
    var sellingAmount: Int? = null

    @SerializedName("buying_amount")
    var buyingAmount: Int? = null

    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null

    @SerializedName("resolved")
    var resolved: Boolean? = null

    @SerializedName("resolved_by_user")
    var resolvedByUser: Int? = null

    override fun toString(): String {
        return "ListingsData(listingId=$listingId, postingUserId=$postingUserId, userPhoneNumber=$userPhoneNumber, sellingAmount=$sellingAmount, buyingAmount=$buyingAmount, usdToLbp=$usdToLbp, resolved=$resolved, resolvedByUser=$resolvedByUser)"
    }


}