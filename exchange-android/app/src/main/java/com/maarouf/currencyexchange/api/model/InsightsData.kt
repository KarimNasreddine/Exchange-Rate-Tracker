package com.maarouf.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class InsightsData {
    @SerializedName("lbp_to_usd_avg")
    var lbpToUsdAverage: Map<String, Float>? = null

    @SerializedName("usd_to_lbp_avg")
    var usdToLbpAverage: Map<String, Float>? = null

    @SerializedName("usd_to_lbp_open")
    var usdToLbpOpen: Map<String, Float>? = null

    @SerializedName("lbp_to_usd_open")
    var lbpToUsdOpen: Map<String, Float>? = null

    @SerializedName("usd_to_lbp_close")
    var usdToLbpClose: Map<String, Float>? = null

    @SerializedName("lbp_to_usd_close")
    var lbpToUsdClose: Map<String, Float>? = null

    @SerializedName("volume_in_trxs")
    var volumeInTransactions: Map<String, Long>? = null

    @SerializedName("volume_in_usd")
    var volumeInUsd: Map<String, Float>? = null
}