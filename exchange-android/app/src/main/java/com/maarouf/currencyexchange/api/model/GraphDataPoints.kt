package com.maarouf.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class GraphDataPoints {
    @SerializedName("buy")
    var buyDataPoints: Map<String, Float>? = null

    @SerializedName("sell")
    var sellDataPoints: Map<String, Float>? = null
}