package provider;

import javafx.scene.control.Alert;

public class DialogProvider {
    public static void showDialog(String Title , String content){
        configureAlert(Title , content).show();
    }

    public static void showDialog(String Title , String content , Alert.AlertType type){
        Alert alert = configureAlert(Title , content);
        alert.setAlertType(type);
        alert.show();
    }

    private static Alert configureAlert(String Title , String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Title);
        alert.setHeaderText(content);
        return alert;
    }
}
