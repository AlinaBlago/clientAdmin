package runner;

import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    Logger logger = LoggerFactory.getLogger(LogInController.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        logger.info("Application start");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 620, 680));
        primaryStage.show();
        logger.info("login.xml loaded");


    }

    //TODO
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        logger.info("Program finalized");
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
