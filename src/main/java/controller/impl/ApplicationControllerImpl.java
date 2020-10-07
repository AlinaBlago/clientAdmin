package controller.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import provider.DialogProvider;
import providers.RequestType;
import providers.ServerConnectionProvider;
import request.AddChatRequest;
import response.ChatResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ApplicationControllerImpl implements ApplicationController {
    @FXML
    Label currentUserNameLabel;

    @FXML
    Button logoutButton;

    @FXML
    ListView<String> usersListView;

    @FXML
    Button deleteUserButton;

    @FXML
    Button blockUserButton;

    @FXML
    Button unblockUserButton;

    @FXML
    TextField findUserLogin;

    @FXML
    Button findUserButton;

    @FXML
    Button logUpButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(this::logOut);

        findUserButton.setOnAction(this::findUser);

        deleteUserButton.setOnAction(this::deleteUser);

        blockUserButton.setOnAction(this::banUser);

        unblockUserButton.setOnAction(this::unBanUser);

        logUpButton.setOnAction(this::onSignUpClick);

        /*
        users_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //UsersListViewChanged(newValue);
            }
        });*/

       // loadUsers();

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
//        if (usersListView.getSelectionModel().isEmpty()){
//            logger.info("Delete user function call: user is empty");
//            return;
//        }
//        try {
//            List<ServerArgument> argumentsList = new ArrayList<>();
//            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));
//            //TODO
//            argumentsList.add(new ServerArgument("password" , usersListView.getSelectionModel().getSelectedItem()));
//
//            //TODO
//            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);
//
//            logger.info("request was sent");
//            usersListView.getItems().clear();
//            loadUsers();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    @Override
    @FXML
    public void banUser(ActionEvent event){
//        if (usersListView.getSelectionModel().isEmpty()){
//            logger.info("banUser function call : user is empty");
//            return;
//        }
//        try {
//            logger.info("request 'banUser' configuration");
//
//            List<ServerArgument> argumentsList = new ArrayList<>();
//            argumentsList.add(new ServerArgument("login", CurrentUser.getCurrentUser().getLogin()));
//            //TODO: name
//            argumentsList.add(new ServerArgument("password", usersListView.getSelectionModel().getSelectedItem()));
//
//            //TODO: serverFunction
//            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);
//
//            logger.info("request was sent");
//        } catch (Exception e) {
//            //TODO: LOGGER
//            System.out.println(e.getMessage());
//        }
    }

    @Override
    @FXML
    public void unBanUser(ActionEvent event){
//        if (usersListView.getSelectionModel().isEmpty()){
//            logger.info("unbanUser function call : user is empty");
//            return;
//        }
//        try {
//            logger.info("request 'unbanUser' configuration");
//
//            List<ServerArgument> argumentsList = new ArrayList<>();
//            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));
//            argumentsList.add(new ServerArgument("usersList" , usersListView.getSelectionModel().getSelectedItem()));
//
//            //TODO
//            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);
//
//            logger.info("Request was sent");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    public void setCurrentUserNameToWindow(){
        String text = "Вы вошли под логином: " + CurrentUser.getUsername();
        currentUserNameLabel.setText(text);
    }

    @FXML
    public void findUser(ActionEvent event) {
        if (findUserLogin.getText().length() == 0){
            return;
        }
        try {
            logger.info("Start send 'findUser' to server");

            AddChatRequest request = new AddChatRequest();
            request.setUsername(findUserLogin.getText());
            ResponseEntity<ChatResponse> answer = ServerConnectionProvider.getInstance().addChat(request);
            logger.info("Request sent");

            if (answer.getStatusCode().is2xxSuccessful()) {
                logger.info("Response 0 from server");
                usersListView.getItems().add(findUserLogin.getText());
                findUserLogin.clear();
            } else {
                logger.warn("Response not 0 from server: " + answer.getStatusCode());
                DialogProvider.ShowDialog("ERROR", "User not found", Alert.AlertType.ERROR);

            }
        } catch (Exception e){
            logger.warn(e.getMessage());
            System.out.println(e.getMessage());
            DialogProvider.ShowDialog("FORBIDDEN", "You are not admin", Alert.AlertType.ERROR);
        }
    }

    public void loadUsers(){
//        try {
//            logger.info("request 'loaduserchat' configuration");
//
//            List<ServerArgument> argumentsList = new ArrayList<>();
//            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));
//
//            //TODO
//            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);
//            logger.info("request was sent");
//
//            //TODO
//            Type listType = new TypeToken<Set<String>>(){}.getType();
////            Set<String> currentUsers = gson.fromJson(response1.getResponseMessage() , listType);
////            currentUsers.remove(CurrentUser.getCurrentUser().getLogin());
////            usersListView.getItems().addAll(currentUsers);
//            usersListView.refresh();
//
//        } catch (Exception e) {
//            //TODO: LOGGER
//            System.out.println(e.getMessage());
//        }

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
