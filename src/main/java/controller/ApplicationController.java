package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ApplicationController extends Initializable {
    Logger logger = LoggerFactory.getLogger(LogInController.class);

     void deleteUser(ActionEvent event);
     void banUser(ActionEvent event);
     void unBanUser(ActionEvent event);
     void logOut(ActionEvent event);




}
