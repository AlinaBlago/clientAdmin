package controller.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import data.CurrentUser;
import data.ServerArgument;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.RequestType;
import providers.ServerConnectionProvider;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(this::logOut);

        deleteUserButton.setOnAction(this::deleteUser);

        blockUserButton.setOnAction(this::banUser);

        unblockUserButton.setOnAction(this::unBanUser);

        /*
        users_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //UsersListViewChanged(newValue);
            }
        });*/

        loadUsers();

        setCurrentUserNameToWindow();
    }

    @Override
    @FXML
    public void deleteUser(ActionEvent event){
        if (usersListView.getSelectionModel().isEmpty()){
            logger.info("Delete user function call: user is empty");
            return;
        }
        try {
            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));
            //TODO
            argumentsList.add(new ServerArgument("password" , usersListView.getSelectionModel().getSelectedItem()));

            //TODO
            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);

            logger.info("request was sent");
            usersListView.getItems().clear();
            loadUsers();

        } catch (Exception e) {
            System.out.println(e.getMessage());
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

            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login", CurrentUser.getCurrentUser().getLogin()));
            //TODO: name
            argumentsList.add(new ServerArgument("password", usersListView.getSelectionModel().getSelectedItem()));

            //TODO: serverFunction
            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);

            logger.info("request was sent");
        } catch (Exception e) {
            //TODO: LOGGER
            System.out.println(e.getMessage());
        }
    }

    @Override
    @FXML
    public void unBanUser(ActionEvent event){
        if (usersListView.getSelectionModel().isEmpty()){
            logger.info("unbanUser function call : user is empty");
            return;
        }
        try {
            logger.info("request 'unbanUser' configuration");

            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));
            argumentsList.add(new ServerArgument("usersList" , usersListView.getSelectionModel().getSelectedItem()));

            //TODO
            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);

            logger.info("Request was sent");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentUserNameToWindow(){
        String text = "Вы вошли под логином: " + CurrentUser.getCurrentUser().getLogin();
        currentUserNameLabel.setText(text);
    }

    public void loadUsers(){
        try {
            logger.info("request 'loaduserchat' configuration");

            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login" , CurrentUser.getCurrentUser().getLogin()));

            //TODO
            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);
            logger.info("request was sent");

            //TODO
            Type listType = new TypeToken<Set<String>>(){}.getType();
//            Set<String> currentUsers = gson.fromJson(response1.getResponseMessage() , listType);
//            currentUsers.remove(CurrentUser.getCurrentUser().getLogin());
//            usersListView.getItems().addAll(currentUsers);
            usersListView.refresh();

        } catch (Exception e) {
            //TODO: LOGGER
            System.out.println(e.getMessage());
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
