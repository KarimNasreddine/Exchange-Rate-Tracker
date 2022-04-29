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

    fun ListingsData(
        userPhoneNumber: String?,
        sellingAmount: Int?,
        buyingAmount: Int?,
        usdToLbp: Boolean?
    ) {
        this.userPhoneNumber = userPhoneNumber
        this.sellingAmount = sellingAmount
        this.buyingAmount = buyingAmount
        this.usdToLbp = usdToLbp
    }
}