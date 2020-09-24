package controller.impl;

import controller.LogUpController;
import data.ServerArgument;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class LogUpControllerImpl implements LogUpController {
    @FXML
    TextField nameField;

    @FXML
    TextField loginField;

    @FXML
    Button signUpButton;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField repeatPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setOnAction(this::onSignUpClick);
    }

    @Override
    @FXML
    public void onSignUpClick(ActionEvent event) {
                try {
                    if (nameField.getText().length() < 1) throw new Exception("Please enter name more than 1 symbol!");
                    if (loginField.getText().length() < 2) throw new Exception("Please enter login more than 2 symbols!");
                    if (passwordField.getText().length() < 4)
                        throw new Exception("Password cannot be less than 4 symbols!");
                    if (!passwordField.getText().equals(repeatPasswordField.getText()))
                        throw new Exception("Passwords are not equal!");
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("Wrong data");
                    alert.setContentText(e.getMessage());
                    alert.show();
                    return;
                }

                logger.info("request sign up sending");

                List<ServerArgument> argumentsList = new ArrayList<>();
                argumentsList.add(new ServerArgument("name" , nameField.getText()));
                argumentsList.add(new ServerArgument("login" , loginField.getText()));
                argumentsList.add(new ServerArgument("password" , passwordField.getText()));
                //TODO

                ResponseEntity<Integer> answer = null;
                try {
                    answer = ServerConnectionProvider.getInstance().loginRequest("signUp", argumentsList, RequestType.GET);
                } catch (IOException e) {
                 e.printStackTrace();
                }

                logger.info("request was sent");

                if(answer != null){
                    if (answer.getBody() != 0){
                    logger.info("registration successful");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Answer");
                    alert.setHeaderText("Results:");
                    alert.show();
                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    stage.close();
                    return;
                }

                    //TODO
//                if(response1.getResponseID() == 2){
//                    logger.warn("registration failed");
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle(response1.getResponseMessage());
//                    alert.setHeaderText("Results:");
//                    alert.setContentText("Такой пользователь уже существует");
//                    alert.show();
//                    loginField.clear();
//                    nameField.clear();
//                    passwordField.clear();
//                    repeatPasswordField.clear();
//                }
            }
    }
}
