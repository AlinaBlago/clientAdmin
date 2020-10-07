package controller.impl;

import controller.LogInController;
import data.CurrentUser;
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
import providers.ServerConnectionProvider;
import request.LoginRequest;

import java.io.IOException;
import java.net.URL;
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
            String role = "ROLE_ADMIN";
            LoginRequest requestBody = new LoginRequest(loginField.getText() , passwordField.getText(), role);
            ResponseEntity<String> answer = ServerConnectionProvider.getInstance().loginRequest(requestBody);
            logger.info("Request was sent");


            if(answer.getStatusCode().is2xxSuccessful()){
                CurrentUser.setUsername(loginField.getText());
                logger.info("User is logged in");
                CurrentUser.setAuthToken(answer.getHeaders().get("Authorization").get(0));

                Stage applStage = new Stage();
                Parent applSceneRoot = null;
                try {
                    applSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/application.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                applStage.setScene(new Scene(applSceneRoot, 620, 680));
                applStage.show();

                //Закрываем текущее окно
                Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
                currentStageToClose.close();
                return;
            }
        } catch (Exception e) {
            DialogProvider.ShowDialog("ERROR" , "Wrong login or password" , Alert.AlertType.ERROR);
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
            }


}

