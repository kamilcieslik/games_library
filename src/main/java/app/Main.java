package app;

import javafx.application.Application;
import javafx.controller.WelcomeBannerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Main extends Application {
    private static Stage mainStage;

    public static void setMainStage(Stage mainStage) {
        Main.mainStage = mainStage;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            Main.mainStage = primaryStage;
            Preferences pref = Preferences.userRoot();
            Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
            loader.setLocation(getClass().getClassLoader().getResource("fxml/welcome_banner.fxml"));
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
            loader.setResources(resourceBundle);
            loader.load();
            Parent root = loader.getRoot();
            primaryStage.setTitle(resourceBundle.getString("application.title"));
            primaryStage.getIcons().add(new Image("/image/icon.png"));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.setScene(new Scene(root, 819, 325));
            WelcomeBannerController loaderController = loader.getController();
            primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> {
                Thread windowShownListener = new Thread(loaderController::initMainScene);
                windowShownListener.start();
            });
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
