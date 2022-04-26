package com.maarouf.exchange.insights;

import com.maarouf.exchange.api.ExchangeService;
import com.maarouf.exchange.api.model.ExchangeRates;
import com.maarouf.exchange.api.model.InsightsData;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class Insights implements Initializable {
    public Label sellOpenCloseLabel;
    public Label buyOpenCloseLabel;
    public Label transactionsNumberLabel;
    public Label transactionsVolumeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchInsights();
    }

    private void fetchInsights() {
        ExchangeService.exchangeApi().getInsightsData().enqueue(new Callback<InsightsData>() {
            @Override
            public void onResponse(Call<InsightsData> call, Response<InsightsData> response) {
                InsightsData insightsData = response.body();
                Platform.runLater(() -> {
                    constructInsightsView(insightsData);
                });
            }
            @Override
            public void onFailure(Call<InsightsData> call, Throwable throwable) {
            }
        });
    }

    private void constructInsightsView(InsightsData insightsData) {
        sellOpenCloseLabel.setText("");
        buyOpenCloseLabel.setText("");
        transactionsNumberLabel.setText("");
        transactionsVolumeLabel.setText("");

        for (Map.Entry<String,Float> entry : insightsData.usdToLbpOpen.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            sellOpenCloseLabel.setText(sellOpenCloseLabel.getText() +
                    key + " : " + value + "-"
                    + insightsData.usdToLbpClose.get(key) + "\n"
            );
        }

        for (Map.Entry<String,Float> entry : insightsData.lbpToUsdOpen.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            buyOpenCloseLabel.setText(buyOpenCloseLabel.getText() +
                    key + " : " + value + "-" +
                    insightsData.lbpToUsdClose.get(key) + "\n"
            );
        }

        for(Map.Entry<String, Long> entry : insightsData.volumeInTransactions.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            transactionsNumberLabel.setText(transactionsNumberLabel.getText() +
                    key + " : " + value + "\n"
            );
        }

        for(Map.Entry<String, Float> entry : insightsData.volumeInUsd.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            transactionsVolumeLabel.setText(transactionsVolumeLabel.getText() +
                    key + " : " + value + "\n"
            );
        }
    }
}
