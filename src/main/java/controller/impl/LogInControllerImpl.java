package controller.impl;

import controller.LogInController;
import data.CurrentUser;
import data.ServerArgument;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import provider.DialogProvider;
import providers.RequestType;
import providers.ServerConnectionProvider;
import request.LoginRequest;
import response.JwtResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LogInControllerImpl implements LogInController {
    @FXML
    TextField loginField;

    @FXML
    Button loginButton;

    @FXML
    Button signUpButton;

    @FXML
    PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::onLoginClick);

    }

    @Override
    @FXML
    public void onLoginClick(ActionEvent event){
        try {
            if(loginField.getText().length() == 0 || passwordField.getText().length() == 0){
                return;
            }
            LoginRequest requestBody = new LoginRequest(loginField.getText() , passwordField.getText());
            ResponseEntity<JwtResponse> answer = ServerConnectionProvider.getInstance().loginRequest(requestBody);
            logger.info("Request was sent");

            User user = new User(loginField.getText(), passwordField.getText());
            CurrentUser.init(user);

            if(answer.getStatusCode().is2xxSuccessful()){
                logger.info("User is logged in");
                DialogProvider.ShowDialog("SUCCESSFUL" , "You are logged in");
                CurrentUser.setAuthToken(answer.getBody().getToken());
                System.out.println(answer.getBody().getToken());

                //Открываем главное окно
                Stage applStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/application.fxml"));
                Parent root = loader.load();
                applStage.setScene(new Scene(root, 620, 680));
                applStage.show();

                //Закрываем текущее окно
                Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
                currentStageToClose.close();
                return;
            }
            DialogProvider.ShowDialog("ERROR" , "Wrong login or password" , Alert.AlertType.ERROR);

        } catch (Exception e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
            }


}

