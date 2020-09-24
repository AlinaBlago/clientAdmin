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
import providers.RequestType;
import providers.ServerConnectionProvider;

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
        signUpButton.setOnAction(this::onSignUpClick);
    }

    @Override
    @FXML
    public void onLoginClick(ActionEvent event){
                try {
                    if(loginField.getText().length() == 0 || passwordField.getText().length() == 0){
                        return;
                    }

                    List<ServerArgument> argumentsList = new ArrayList<>();
                    argumentsList.add(new ServerArgument("login" , loginField.getText()));
                    argumentsList.add(new ServerArgument("password" , passwordField.getText()));

                    ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);

                    User user = new User("" , loginField.getText() , "");
                    CurrentUser.init(user, answer.getBody());

                    if(answer != null) {
                        if (answer.getBody() != 0) {
                            logger.info("User logined");
                            //Открываем главное окно
                            Stage applStage = new Stage();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/admin.fxml"));
                            Parent root = loader.load();
                            applStage.setScene(new Scene(root, 620, 680));
                            applStage.show();

                            //Закрываем текущее окно
                            Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
                            currentStageToClose.close();
                            return;
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Wrong login or password");
                    alert.show();

                } catch (Exception e) {
                    logger.info(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

    @Override
    @FXML
    public void onSignUpClick(ActionEvent event){
        Stage signUp = new Stage();
        Parent signUpSceneRoot = null;
        try {
            signUpSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/logUp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUp.setScene(new Scene(signUpSceneRoot, 620, 680));
        signUp.show();
    }

}

