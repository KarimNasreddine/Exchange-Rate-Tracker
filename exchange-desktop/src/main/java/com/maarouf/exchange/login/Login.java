package com.maarouf.exchange.login;

import com.maarouf.exchange.Authentication;
import com.maarouf.exchange.OnPageCompleteListener;
import com.maarouf.exchange.PageCompleter;
import com.maarouf.exchange.api.ExchangeService;
import com.maarouf.exchange.api.model.Token;
import com.maarouf.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login implements PageCompleter {

    public TextField usernameTextField;
    public TextField passwordTextField;

    private OnPageCompleteListener onPageCompleteListener;

    public void login(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText());
        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token>
                    response) {

                Authentication.getInstance().saveToken(response.body().getToken());

                Platform.runLater(() -> {
                    onPageCompleteListener.onPageCompleted();
                });
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
            }
        });
    }

    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

}
