package com.maarouf.exchange.graph;

import com.maarouf.exchange.api.ExchangeService;
import com.maarouf.exchange.api.model.GraphDataPoints;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class Graph implements Initializable {

    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    public LineChart<String, Number> lineGraph = new LineChart<String,Number>(xAxis,yAxis);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lineGraph.setAnimated(false);

        lineGraph.setTitle("Fluctuation Graph");
        yAxis.setLabel("Value");
        xAxis.setLabel("Date");

        fetchGraph();
    }

    private void fetchGraph() {

        ExchangeService.exchangeApi().getGraphDataPoints().enqueue(new Callback<GraphDataPoints>() {
            @Override
            public void onResponse(Call<GraphDataPoints> call, Response<GraphDataPoints> response) {
                GraphDataPoints graphDataPoints = response.body();
                Platform.runLater(() -> {

                    buildGraph(graphDataPoints);

                    /*
                    //for debugging purposes
                    System.out.println("Buy");
                    for (Map.Entry<String,Float> entry : graphDataPoints.buyDataPoints.entrySet()){
                        System.out.println("Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());
                    }
                    //for debugging purposes
                    System.out.println("Sell");
                    for (Map.Entry<String,Float> entry : graphDataPoints.sellDataPoints.entrySet()){
                        System.out.println("Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());
                    }
                     */
                });
            }
            @Override
            public void onFailure(Call<GraphDataPoints> call, Throwable throwable) {
            }
        });
    }

    private void buildGraph(GraphDataPoints graphDataPoints) {
        XYChart.Series series = new XYChart.Series();
        series.setName("Buy");
        for (Map.Entry<String,Float> entry : graphDataPoints.buyDataPoints.entrySet()){
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Sell");
        for (Map.Entry<String,Float> entry : graphDataPoints.sellDataPoints.entrySet()){
            series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        lineGraph.getData().addAll(series, series2);
    }
}
