package com.maarouf.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime
import java.util.*

class Transaction {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("usd_amount")
    var usdAmount: Float? = null

    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null

    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null

    @SerializedName("user_id")
    var userId: Int? = null

    @SerializedName("added_date")
    var addedDate: String? = null
}