package controller.impl;

import com.google.gson.Gson;
import controller.LogInController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInControllerImpl implements LogInController {
    @FXML
    private TextField loginField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    if(loginField.getText().length() == 0 || passwordField.getText().length() == 0){
                        return;
                    }

                    StringBuffer url = new StringBuffer();
                    url.append("http://localhost:8080/Login?login=");
                    url.append(loginField.getText());
                    url.append("&password=");
                    url.append(passwordField.getText());

                    URL obj = new URL(url.toString());
                    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Gson gson = new Gson();
                    User user = new User("" , loginField.getText() , "");
                    AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);
                    CurrentUserInfo.Init(user , response1.getResponseMessage());

                    if(response1.getResponseID() == 0) {
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
                    }

                } catch (Exception e) {
                    logger.info(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
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
        });
    }

    }
}