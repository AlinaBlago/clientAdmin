package controller;

import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public interface ApplicationController extends Initializable {
    Logger logger = LoggerFactory.getLogger(LogInController.class);

     void deleteUser(ActionEvent event);
     void banUser(ActionEvent event);
     void unBanUser(ActionEvent event);
     void logOut(ActionEvent event);




}
