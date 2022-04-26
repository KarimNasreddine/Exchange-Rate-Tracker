module com.maarouf.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    requires java.prefs;


    opens com.maarouf.exchange to javafx.fxml;
    opens com.maarouf.exchange.api.model to javafx.base, gson;
    exports com.maarouf.exchange;
    opens com.maarouf.exchange.api to gson;
    exports com.maarouf.exchange.rates;
    opens com.maarouf.exchange.rates to javafx.fxml;
    opens com.maarouf.exchange.login to javafx.fxml;
    opens com.maarouf.exchange.register to javafx.fxml;
    opens com.maarouf.exchange.transactions to javafx.fxml;
    opens com.maarouf.exchange.graph to javafx.fxml;
    opens com.maarouf.exchange.insights to javafx.fxml;
}