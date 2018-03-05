package javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomMessageBox {
    private String iconImagePath;

    public CustomMessageBox(String iconImagePath) {
        this.iconImagePath = iconImagePath;
    }

    public Alert showMessageBox(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(iconImagePath));
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public Alert showConfirmMessageBox(Alert.AlertType alertType, String title, String header, String content, String confirmText, String cancelText) {
        ButtonType confirmButton = new ButtonType(confirmText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(alertType, content, confirmButton, cancelButton);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(iconImagePath));
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }
}
