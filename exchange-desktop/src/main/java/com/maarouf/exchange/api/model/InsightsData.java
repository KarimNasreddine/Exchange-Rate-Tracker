package com.maarouf.exchange.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class InsightsData {
    @SerializedName("lbp_to_usd_avg")
    public Map<String, Float> lbpToUsdAverage;

    @SerializedName("usd_to_lbp_avg")
    public Map<String, Float> usdToLbpAverage;

    @SerializedName("usd_to_lbp_open")
    public Map<String, Float> usdToLbpOpen;

    @SerializedName("lbp_to_usd_open")
    public Map<String, Float> lbpToUsdOpen;

    @SerializedName("usd_to_lbp_close")
    public Map<String, Float> usdToLbpClose;

    @SerializedName("lbp_to_usd_close")
    public Map<String, Float> lbpToUsdClose;

    @SerializedName("volume_in_trxs")
    public  Map<String, Long> volumeInTransactions;

    @SerializedName("volume_in_usd")
    public Map<String, Float> volumeInUsd;

}
