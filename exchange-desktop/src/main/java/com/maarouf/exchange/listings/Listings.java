package com.maarouf.exchange.listings;

import com.maarouf.exchange.api.ExchangeService;
import com.maarouf.exchange.api.model.ListingsData;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Listings implements Initializable {
    public Label listingsLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchListings();
    }

    private void fetchListings() {
        ExchangeService.exchangeApi().getListings().enqueue(new Callback<List<ListingsData>>() {
            @Override
            public void onResponse(Call<List<ListingsData>> call, Response<List<ListingsData>> response) {
                List<ListingsData> listings = response.body();
                Platform.runLater(() -> {
                    displayListings(listings);
                });
            }
            @Override
            public void onFailure(Call<List<ListingsData>> call, Throwable throwable) {
            }
        });
    }

    private void displayListings(List<ListingsData> listings) {
        int counter = 1;
        for(ListingsData listing : listings) {
            if(!listing.resolved){
                listingsLabel.setText(listingsLabel.getText() +
                        "-Listing: " + counter +
                        "\n\tAmount Selling: " + listing.sellingAmount + (listing.usdToLbp ? " USD" : " LBP") +
                        "\n\tBuying Amount Ask: " + listing.buyingAmount + (listing.usdToLbp ? " LBP" : " USD") +
                        "\n\tListing Type: " + (listing.usdToLbp ? "USD To LBP" : "LBP To USD") +
                        "\n\t Phone Number: " + listing.userPhoneNumber +
                        "\n");
                counter++;
            }
        }
    }
}