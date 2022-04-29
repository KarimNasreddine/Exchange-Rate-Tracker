package com.maarouf.exchange.listings;

import com.maarouf.exchange.Authentication;
import com.maarouf.exchange.api.ExchangeService;
import com.maarouf.exchange.api.model.ListingsData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Listings implements Initializable {
    public Label listingsLabel;
    public TextField sellAmountTextField;
    public TextField buyAskAmountTextField;
    public TextField phoneNumberTextField;
    public ToggleGroup listingType;

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
        listingsLabel.setText("");
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

    public void addListing(ActionEvent actionEvent) {
        ListingsData listing = new ListingsData(
                phoneNumberTextField.getText(),
                Integer.parseInt(sellAmountTextField.getText()),
                Integer.parseInt(buyAskAmountTextField.getText()),
                ((RadioButton) listingType.getSelectedToggle()).getText().equals("USD TO LBP")
        );
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addListing(listing, authHeader).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                fetchListings();
                Platform.runLater(() -> {
                    phoneNumberTextField.setText("");
                    sellAmountTextField.setText("");
                    buyAskAmountTextField.setText("");
                });
            }
            @Override
            public void onFailure(Call<Object> call, Throwable throwable)
            {
            }
        });
    }
}