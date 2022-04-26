package com.maarouf.exchange.api;

import com.maarouf.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;

public interface Exchange {

    @POST("/user")
    Call<User> addUser(@Body User user);

    @POST("/authentication")
    Call<Token> authenticate(@Body User user);

    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();

    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);

    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                                    String authorization);

    @GET("/graph")
    Call<GraphDataPoints> getGraphDataPoints();

    @GET("/insights")
    Call<InsightsData> getInsightsData();
}
