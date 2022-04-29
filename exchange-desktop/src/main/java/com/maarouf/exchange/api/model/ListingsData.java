package com.maarouf.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class ListingsData {

    @SerializedName("listing_id")
    public Integer listingId;

    @SerializedName("posting_user_id")
    public Integer postingUserId;

    @SerializedName("user_phone_number")
    public String userPhoneNumber;

    @SerializedName("selling_amount")
    public Integer sellingAmount;

    @SerializedName("buying_amount")
    public Integer buyingAmount;

    @SerializedName("usd_to_lbp")
    public Boolean usdToLbp;

    @SerializedName("resolved")
    public Boolean resolved;

    @SerializedName("resolved_by_user")
    public Integer resolvedByUser;

    public ListingsData(String userPhoneNumber, Integer sellingAmount, Integer buyingAmount, Boolean usdToLbp) {
        this.userPhoneNumber = userPhoneNumber;
        this.sellingAmount = sellingAmount;
        this.buyingAmount = buyingAmount;
        this.usdToLbp = usdToLbp;
    }

    @Override
    public String toString() {
        return "ListingsData{" +
                "listingId=" + listingId +
                ", postingUserId=" + postingUserId +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", sellingAmount=" + sellingAmount +
                ", buyingAmount=" + buyingAmount +
                ", usdToLbp=" + usdToLbp +
                ", resolved=" + resolved +
                ", resolvedByUser=" + resolvedByUser +
                '}';
    }
}
