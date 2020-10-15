package controller.impl;

import controller.ApplicationController;
import data.CurrentUser;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import provider.DialogProvider;
import providers.ServerConnectionProvider;
import request.UserRequest;
import response.ChatResponse;
import response.FindUserResponse;
import response.UserResponse;
import util.FxUtilTest;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class ApplicationControllerImpl implements ApplicationController {
    @FXML
    Label currentUserNameLabel;

    @FXML
    Button logoutButton;

    @FXML
    ListView<String> usersListView;

    @FXML
    ListView<String> userInfo;

    @FXML
    Button deleteUserButton;

    @FXML
    Button blockUserButton;

    @FXML
    Button unblockUserButton;

    @FXML
    TextField findUserLogin;

    @FXML
    ComboBox<String> findComboBox;

    @FXML
    Button findUserButton;

    @FXML
    Button logUpButton;

    private String selectedChatLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(this::logOut);

        findUserButton.setOnAction(this::findUser);

        deleteUserButton.setOnAction(this::deleteUser);

        blockUserButton.setOnAction(this::banUser);

        unblockUserButton.setOnAction(this::unBanUser);

        logUpButton.setOnAction(this::onSignUpClick);

        findUserLogin.textProperty().addListener(this::textChanged);

        usersListView.getSelectionModel().selectedItemProperty().addListener(this::usersListViewChanged);

        setCurrentUserNameToWindow();
    }

    @FXML
    private void onSignUpClick(ActionEvent event){
        Stage signUp = new Stage();
        Parent signUpSceneRoot = null;
        try {
            signUpSceneRoot = FXMLLoader.load(ApplicationControllerImpl.this.getClass().getResource("/logUp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUp.setScene(new Scene(signUpSceneRoot, 620, 680));
        signUp.show();
    }

    @Override
    @FXML
    public void deleteUser(ActionEvent event){
        if (usersListView.getSelectionModel().isEmpty()){
            logger.info("Delete user function call: user is empty");
            return;
        }
        try {
            UserRequest requestBody = new UserRequest(usersListView.getSelectionModel().getSelectedItem());
            ResponseEntity<String> answer = ServerConnectionProvider.getInstance().deleteUser(requestBody);
            logger.info("request was sent");
            if(answer.getStatusCode().is2xxSuccessful()) {
                usersListView.getItems().clear();
                DialogProvider.showDialog("Successful" , "User deleted" , Alert.AlertType.INFORMATION);
                //  loadUsers();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            DialogProvider.showDialog("ERROR" , "User doesn't deleted" , Alert.AlertType.ERROR);
        }
    }

    @Override
    @FXML
    public void banUser(ActionEvent event){
        if (usersListView.getSelectionModel().isEmpty()){
            logger.info("banUser function call : user is empty");
            return;
        }
        try {
            logger.info("request 'banUser' configuration");
            UserRequest requestBody = new UserRequest(usersListView.getSelectionModel().getSelectedItem());
            logger.info("request was sent");
            ResponseEntity<String> answer = ServerConnectionProvider.getInstance().lockUser(requestBody);

            if(answer.getStatusCode().is2xxSuccessful()) {
                DialogProvider.showDialog("Successful" , "User locked" , Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            logger.info("User doesn't banned, error");
            DialogProvider.showDialog("ERROR" , "User doesn't locked" , Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    @Override
    @FXML
    public void unBanUser(ActionEvent event){
        if (usersListView.getSelectionModel().isEmpty()){
            logger.info("unlockUser function call : user is empty");
            return;
        }
        try {
            logger.info("request 'unlockUser' configuration");
            UserRequest requestBody = new UserRequest(usersListView.getSelectionModel().getSelectedItem());
            logger.info("Request was sent");
            ResponseEntity<String> answer = ServerConnectionProvider.getInstance().unLockUser(requestBody);

            if(answer.getStatusCode().is2xxSuccessful()) {
                DialogProvider.showDialog("Successful" , "User unlocked" , Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            logger.info("User doesn't banned, error");
            System.out.println(e.getMessage());
            DialogProvider.showDialog("ERROR" , "User doesn't unLocked" , Alert.AlertType.ERROR);
        }
    }

    public void setCurrentUserNameToWindow(){
        String text = "Вы вошли под логином: " + CurrentUser.getUsername();
        currentUserNameLabel.setText(text);
    }

    @FXML

    public void usersListViewChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        this.selectedChatLogin = newValue;

        try {
            updateUserInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateUserInfo() throws IOException {
        if(selectedChatLogin != null){
            if(!selectedChatLogin.isEmpty()){
                logger.info("Sending 'updateChatForUser' request to server");

                UserRequest request = new UserRequest();
                request.setUsername(selectedChatLogin);

                ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().addChat(request);

                logger.info("Request was sent");

                if (answer.getStatusCode().is2xxSuccessful()) {
                    logger.info("Successful");
                    userInfo.getItems().clear();
                    userInfo.getItems().add(answer.getBody().getUsername());
                    userInfo.getItems().add(answer.getBody().getEmail());
                    userInfo.getItems().add(answer.getBody().getCreatedAt());
                    userInfo.refresh();

                } else {
                    logger.warn("Error: " + answer.getStatusCode());
                    DialogProvider.showDialog("ERROR", "Loading error", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    public void textChanged(ObservableValue<? extends String> observable,
                            String oldValue, String newValue){
        if (newValue.length() >= 2) {
            findComboBox.getItems().clear();
            UserRequest request = new UserRequest();
            request.setUsername(newValue);
            ResponseEntity<FindUserResponse> answer = ServerConnectionProvider.getInstance().findUser(request);
            logger.info("Request sent");

            if (answer.getStatusCode().is2xxSuccessful()) {
                logger.info("Response 200 from server");
                answer.getBody().getUsernames().forEach(username ->{
                    findComboBox.getItems().addAll(username);
                });

            } else {
                logger.warn("Response not 200 from server: " + answer.getStatusCode());
                DialogProvider.showDialog("ERROR", "User not found", Alert.AlertType.ERROR);

            }
        }
    }

    @FXML
    public void findUser(ActionEvent event) {
        try {
            if (!findComboBox.getSelectionModel().getSelectedItem().isBlank())
            {
                String login = findComboBox.getSelectionModel().getSelectedItem();
                logger.info("Start send 'findUser' to server");

                FxUtilTest.getComboBoxValue(findComboBox);
                UserRequest request = new UserRequest();
                request.setUsername(login);
                ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().addChat(request);
                logger.info("Request sent");

                if (answer.getStatusCode().is2xxSuccessful()) {
                    logger.info("Response 200 from server");
                    usersListView.getItems().add(login);
                    usersListView.refresh();


                } else {
                    logger.warn("Response not 200 from server: " + answer.getStatusCode());
                    DialogProvider.showDialog("ERROR", "User not found", Alert.AlertType.ERROR);

                }
            }
        } catch (Exception e){
            logger.warn(e.getMessage());
            System.out.println(e.getMessage());
            DialogProvider.showDialog("FORBIDDEN", "You are not admin", Alert.AlertType.ERROR);
        }


    }

    @Override
    @FXML
    public void logOut(ActionEvent event){
        logger.info("logout command");
        CurrentUser.logOut();

        Stage stageToClose = (Stage) logoutButton.getScene().getWindow();
        stageToClose.close();

        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.setScene(new Scene(root, 620, 680));
        mainStage.show();
        logger.info("opened login.fxml");
    }
}
