package javafx.controller;

import app.Main;
import games_library.Game;
import games_library.Library;
import javafx.CustomMessageBox;
import javafx.ListenerMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import xml_parser.GamesLibraryXmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

@Controller
public class MainController implements Initializable {
    private Preferences pref;
    private CustomMessageBox customMessageBox;
    private ResourceBundle resourceBundle;
    private String withoutSpacesAtStartAndAndPattern = "^\\S$|^\\S[\\s\\S]*\\S$";
    private Library gamesLibrary;
    private ObservableList<Game> gameObservableList = FXCollections.observableArrayList();
    private String newGameImageUrl;
    private DecimalFormat decimalFormat;
    private NumberFormat numberFormat;

    @FXML
    private TableView<Game> tableViewGames;
    @FXML
    private TableColumn<Game, String> tableColumnTitle, tableColumnProducer, tableColumnPublisher;
    @FXML
    private TableColumn<Game, Date> tableColumnReleaseDate;
    @FXML
    private TableColumn<Game, Double> tableColumnPrice;
    @FXML
    private RadioButton radioButtonAddGameMode, radioButtonDetailsMode, radioButtonStatisticsMode;
    @FXML
    private VBox vBoxDetailsMode, vBoxAddMode, vBoxStatistics;
    @FXML
    private Label labelDetailsTitle, labelDetailsProducer, labelDetailsPublisher, labelDetailsReleaseDate,
            labelDetailsPrice, labelAddTitle, labelAddProducer, labelAddPublisher, labelAddReleaseDate, labelAddPrice,
            labelAddFrontCover, labelStatisticsNumberOfGames, labelStatisticsTotalValueOfLibrary,
            labelStatisticsMostExpensiveGameTitle, labelStatisticsMostExpensiveGamePrice,
            labelStatisticsCheapestGameTitle, labelStatisticsCheapestGamePrice;
    @FXML
    private ImageView imageViewDetailsGamePicture, imageViewAddFrontCover, imageViewStatisticsCheapestGame,
            imageViewStatisticsMostExpensiveGame;
    @FXML
    private TextField textFieldAddTitle, textFieldAddProducer, textFieldAddPublisher, textFieldAddPrice;

    @FXML
    private DatePicker datePickerAddReleaseDate;

    private void fillTableViewAfterGUIReload(Library gamesLibrary) {
        this.gamesLibrary = gamesLibrary;
        if (gamesLibrary != null)
            gameObservableList.addAll(gamesLibrary.getGames());
        fillStatisticsComponents();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        customMessageBox = new CustomMessageBox("image/icon.png");
        initRadioButtons();
        clearDetailsComponents();
        clearAddGameComponents(true);
        clearStatisticsComponents();
        initTableViews();

        resourceBundle = ResourceBundle.getBundle("bundles.messages");
        ListenerMethods listenerMethods = new ListenerMethods();
        textFieldAddTitle.textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelTextField(withoutSpacesAtStartAndAndPattern, textFieldAddTitle, labelAddTitle,
                        resourceBundle.getString("main.label.warnings.enter.title"),
                        resourceBundle.getString("main.label.warnings_incorrect_format")));
        textFieldAddProducer.textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelTextField(withoutSpacesAtStartAndAndPattern, textFieldAddProducer, labelAddProducer,
                        resourceBundle.getString("main.label.warnings.enter.producer"),
                        resourceBundle.getString("main.label.warnings_incorrect_format")));
        textFieldAddPublisher.textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelTextField(withoutSpacesAtStartAndAndPattern, textFieldAddPublisher, labelAddPublisher,
                        resourceBundle.getString("main.label.warnings.enter.publisher"),
                        resourceBundle.getString("main.label.warnings_incorrect_format")));
        datePickerAddReleaseDate.getEditor().textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelDatePicker(datePickerAddReleaseDate, labelAddReleaseDate,
                        resourceBundle.getString("main.label.warnings.release_date")));
        textFieldAddPrice.textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelTextField("^[0-9,.]*$", textFieldAddPrice, labelAddPrice,
                        resourceBundle.getString("main.label.warnings.price"),
                        resourceBundle.getString("main.label.warnings_incorrect_format")));

        imageViewDetailsGamePicture.fitHeightProperty().setValue(200);
        imageViewDetailsGamePicture.fitWidthProperty().setValue(0);

        Locale.setDefault(new Locale(pref
                .get("games_library_locale", "en_US")));
        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        decimalFormat = (DecimalFormat) numberFormat;
        numberFormat = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        decimalFormat.applyPattern("##.##");
    }

    @FXML
    void imageViewAddFrontCover_onMouseClicked() {
        FileChooser frontCoversFileChooser = new FileChooser();
        frontCoversFileChooser.setTitle(resourceBundle.getString("main.filechooser.front_cover"));
        frontCoversFileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(resourceBundle
                        .getString("main.filechooser.extension_filter_name.pictures"), "*.png", "*.jpg"));
        File file = frontCoversFileChooser.showOpenDialog(Main.getMainStage());
        if (file != null) {
            try {
                newGameImageUrl = String.valueOf(file.toURI().toURL());
                imageViewAddFrontCover.setImage(new Image(newGameImageUrl));
            } catch (MalformedURLException e) {
                newGameImageUrl = null;
                imageViewAddFrontCover.setImage(new Image("image/select_image.png"));
                labelAddFrontCover.setText(resourceBundle.getString("main.label.warnings.front_cover"));
            }
            labelAddFrontCover.setText("");
        } else {
            newGameImageUrl = null;
            imageViewAddFrontCover.setImage(new Image("image/select_image.png"));
            labelAddFrontCover.setText(resourceBundle.getString("main.label.warnings.front_cover"));
        }
    }

    @FXML
    void buttonAddGame_onAction() {
        if (!labelAddTitle.getText().equals("") || !labelAddProducer.getText().equals("") || !labelAddPublisher.getText().equals("")
                || !labelAddReleaseDate.getText().equals("") || !labelAddPrice.getText().equals("") || !labelAddFrontCover.getText().equals(""))
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                    resourceBundle.getString("alert.warning.header.add_game"),
                    resourceBundle.getString("alert.warning.context.add_game"))
                    .showAndWait();
        else {
            try {
                LocalDate localDate = datePickerAddReleaseDate.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, localDate.getYear());
                calendar.set(Calendar.MONTH, localDate.getMonthValue() - 1);
                calendar.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
                Date releaseDate = calendar.getTime();

                Number number = numberFormat.parse(textFieldAddPrice.getText());
                Double doubleValue = number.doubleValue();

                Game game = new Game(textFieldAddTitle.getText(), textFieldAddProducer.getText(), textFieldAddPublisher.getText(),
                        releaseDate, doubleValue, newGameImageUrl);

                if (gamesLibrary == null)
                    gamesLibrary = new Library();
                gamesLibrary.addGame(game);
                gameObservableList.add(game);

                clearAddGameComponents(false);
                fillStatisticsComponents();
            } catch (ParseException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                        resourceBundle.getString("alert.warning.header.add_game"),
                        resourceBundle.getString("alert.warning.context.add_game.price_parse_error"))
                        .showAndWait();
            }
        }
    }

    @FXML
    void buttonClear_onAction() {
        clearAddGameComponents(false);
    }

    @FXML
    void buttonDeleteGame_onAction() {
        Game selectedGame = tableViewGames.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION,
                    resourceBundle.getString("alert.confirmation.title"),
                    resourceBundle.getString("alert.confirmation.header"),
                    resourceBundle.getString("alert.confirmation.context"),
                    resourceBundle.getString("alert.confirm_button"),
                    resourceBundle.getString("alert.cancel_button")).showAndWait()
                    .ifPresent(rs -> {
                        if (rs.getText().equals(resourceBundle.getString("alert.confirm_button"))) {
                            gamesLibrary.deleteGame(selectedGame);
                            gameObservableList.remove(selectedGame);
                            clearDetailsComponents();
                            fillStatisticsComponents();
                        }
                    });
        } else {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                    resourceBundle.getString("alert.warning.header.delete_game"),
                    resourceBundle.getString("alert.warning.context.delete_game"))
                    .showAndWait();
        }
    }

    @FXML
    void buttonReadGames_onAction() {
        FileChooser gamesLibrariesFileChooser = new FileChooser();
        gamesLibrariesFileChooser.setTitle(resourceBundle.getString("main.filechooser.games_library"));
        gamesLibrariesFileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(resourceBundle
                        .getString("main.filechooser.extension_filter_name.game_library"), "*.xml"));
        File file = gamesLibrariesFileChooser.showOpenDialog(Main.getMainStage());
        if (file != null) {
            GamesLibraryXmlParser gamesLibraryXmlParser = new GamesLibraryXmlParser();
            try {
                gamesLibrary = gamesLibraryXmlParser.readGamesLibrary(file);
                gameObservableList.clear();
                gameObservableList.addAll(gamesLibrary.getGames());
                fillStatisticsComponents();
                clearDetailsComponents();
            } catch (JAXBException e) {
                String exceptionMessage;
                if (e.getCause() != null && !e.getCause().getMessage().isEmpty())
                    exceptionMessage = e.getCause().getMessage();
                else
                    exceptionMessage = e.getMessage();
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                        resourceBundle.getString("alert.warnings.header.read_games_library"),
                        resourceBundle.getString("alert.cause") + " " + exceptionMessage + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    void buttonWriteGames_onAction() {
        if (gamesLibrary == null || gamesLibrary.getGames().size() == 0)
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                    resourceBundle.getString("alert.warnings.header.write_games_library"),
                    resourceBundle.getString("alert.warnings.context.write_games_library"))
                    .showAndWait();
        else {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(resourceBundle.getString("main.directorychooser.games_library"));
            File directory = chooser.showDialog(Main.getMainStage());
            if (directory != null) {
                try {
                    String directoryPath = directory.getAbsolutePath();
                    GamesLibraryXmlParser gamesLibraryXmlParser = new GamesLibraryXmlParser();
                    gamesLibraryXmlParser.writeGamesLibrary(gamesLibrary, directoryPath, "games_library");
                } catch (JAXBException e) {
                    e.printStackTrace();
                    String exceptionMessage;
                    if (e.getCause() != null && !e.getCause().getMessage().isEmpty())
                        exceptionMessage = e.getCause().getMessage();
                    else
                        exceptionMessage = e.getMessage();
                    customMessageBox.showMessageBox(Alert.AlertType.WARNING, resourceBundle.getString("alert.warning.title"),
                            resourceBundle.getString("alert.warnings.header.write_games_library"),
                            resourceBundle.getString("alert.cause") + " " + exceptionMessage + ".")
                            .showAndWait();
                }
            }
        }
    }

    @FXML
    void menuItemLanguageAmericanEnglish_onAction() {
        pref.put("games_library_locale", "en_US");

        Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
        reloadGUI();
    }

    @FXML
    void menuItemLanguageGerman_onAction() {
        pref.put("games_library_locale", "de");
        Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
        reloadGUI();
    }

    @FXML
    void menuItemLanguagePolish_onAction() {
        pref.put("games_library_locale", "pl");
        Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
        reloadGUI();
    }

    @FXML
    void menuItemLanguageFrench_onAction() {
        pref.put("games_library_locale", "fr");
        Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
        reloadGUI();
    }

    @FXML
    void radioButtonAddGameMode_onAction() {
        prepareContactModeComponents("add");
        pref.put("games_library_game_mode", "add");
    }

    @FXML
    void radioButtonDetailsMode_onAction() {
        prepareContactModeComponents("details");
        pref.put("games_library_game_mode", "details");
    }

    @FXML
    void radioButtonStatisticsMode_onAction() {
        prepareContactModeComponents("statistics");
        pref.put("games_library_game_mode", "statistics");
    }

    @FXML
    void tableViewGames_onMouseClicked() {
        Game selectedGame = tableViewGames.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            labelDetailsTitle.setText(selectedGame.getTitle());
            labelDetailsProducer.setText(selectedGame.getProducer());
            labelDetailsPublisher.setText(selectedGame.getPublisher());

            String locale = pref.get("games_library_locale", "en_US");
            Locale currentLocale = new Locale(locale);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, currentLocale);
            if (locale.equals("en_US")) {
                String releasedDate = dateFormat.format(selectedGame.getReleaseDate())
                        .substring(0, dateFormat.format(selectedGame.getReleaseDate()).lastIndexOf(' '));
                labelDetailsReleaseDate.setText(releasedDate.substring(0, releasedDate.lastIndexOf(' ')));
            } else
                labelDetailsReleaseDate.setText(dateFormat.format(selectedGame.getReleaseDate())
                        .substring(0, dateFormat.format(selectedGame.getReleaseDate()).lastIndexOf(' ')));

            labelDetailsPrice.setText(decimalFormat.format(selectedGame.getPrice()));

            imageViewDetailsGamePicture.setImage(new Image(selectedGame.getImagePath()));
        }
    }

    private void initRadioButtons() {
        ToggleGroup toggleGroupGameModes = new ToggleGroup();
        radioButtonAddGameMode.setToggleGroup(toggleGroupGameModes);
        radioButtonDetailsMode.setToggleGroup(toggleGroupGameModes);
        radioButtonStatisticsMode.setToggleGroup(toggleGroupGameModes);

        String contactMode = pref.get("games_library_game_mode", "details");

        switch (contactMode) {
            case "add":
                prepareContactModeComponents("add");
                radioButtonAddGameMode.setSelected(true);
                break;
            case "details":
                prepareContactModeComponents("details");
                radioButtonDetailsMode.setSelected(true);
                break;
            case "statistics":
                prepareContactModeComponents("statistics");
                radioButtonStatisticsMode.setSelected(true);
                break;
        }
    }

    private void setVBoxVisible(VBox vBox, Boolean visible) {
        if (visible) {
            vBox.setVisible(true);
            vBox.setDisable(false);
            vBox.setMinWidth(Control.USE_COMPUTED_SIZE);
            vBox.setMinHeight(Control.USE_COMPUTED_SIZE);
            vBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
            vBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
            vBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
            vBox.setMaxWidth(Control.USE_COMPUTED_SIZE);
        } else {
            vBox.setVisible(false);
            vBox.setDisable(true);
            vBox.setMinWidth(0);
            vBox.setPrefWidth(0);
            vBox.setMinHeight(0);
            vBox.setPrefHeight(0);
            vBox.setMaxWidth(0);
            vBox.setMaxHeight(0);
        }
    }

    private void prepareContactModeComponents(String mode) {
        switch (mode) {
            case "add":
                setVBoxVisible(vBoxAddMode, true);
                setVBoxVisible(vBoxDetailsMode, false);
                setVBoxVisible(vBoxStatistics, false);
                break;
            case "details":
                setVBoxVisible(vBoxAddMode, false);
                setVBoxVisible(vBoxDetailsMode, true);
                setVBoxVisible(vBoxStatistics, false);
                break;
            case "statistics":
                setVBoxVisible(vBoxAddMode, false);
                setVBoxVisible(vBoxDetailsMode, false);
                setVBoxVisible(vBoxStatistics, true);
                break;
        }
    }

    private void clearAddGameComponents(Boolean initScene) {
        textFieldAddTitle.clear();
        textFieldAddProducer.clear();
        textFieldAddPublisher.clear();
        datePickerAddReleaseDate.getEditor().clear();
        textFieldAddPrice.clear();
        newGameImageUrl = null;

        imageViewAddFrontCover.setImage(new Image("image/select_image.png"));

        if (!initScene)
            labelAddFrontCover.setText(resourceBundle.getString("main.label.warnings.front_cover"));
    }

    private void clearDetailsComponents() {
        labelDetailsTitle.setText("------");
        labelDetailsProducer.setText("------");
        labelDetailsPublisher.setText("------");
        labelDetailsReleaseDate.setText("------");
        labelDetailsPrice.setText("------");

        imageViewDetailsGamePicture.setImage(new Image("image/no_game.png"));
    }

    private void clearStatisticsComponents() {
        labelStatisticsNumberOfGames.setText("------");
        labelStatisticsTotalValueOfLibrary.setText("------");
        labelStatisticsMostExpensiveGamePrice.setText("------");
        labelStatisticsMostExpensiveGameTitle.setText("------");
        labelStatisticsCheapestGamePrice.setText("------");
        labelStatisticsCheapestGameTitle.setText("------");

        imageViewStatisticsMostExpensiveGame.setImage(new Image("image/no_game.png"));
        imageViewStatisticsCheapestGame.setImage(new Image("image/no_game.png"));
    }

    private void fillStatisticsComponents() {
        if (gamesLibrary != null && gamesLibrary.getGames().size() > 0) {
            labelStatisticsTotalValueOfLibrary.setText(decimalFormat.format(gamesLibrary.getTotalLibraryPrice()) + " €");

            labelStatisticsCheapestGamePrice.setText(decimalFormat.format(gamesLibrary.getGames()
                    .get(0).getPrice()) + " €");

            labelStatisticsCheapestGameTitle.setText(gamesLibrary.getGames()
                    .get(0).getTitle());

            imageViewStatisticsCheapestGame.setImage(new Image(gamesLibrary.getGames().get(0).getImagePath()));

            labelStatisticsMostExpensiveGamePrice.setText(decimalFormat.format(gamesLibrary.getGames()
                    .get(gamesLibrary.getGames().size() - 1).getPrice()) + " €");

            labelStatisticsMostExpensiveGameTitle.setText(gamesLibrary.getGames()
                    .get(gamesLibrary.getGames().size() - 1).getTitle());

            imageViewStatisticsMostExpensiveGame.setImage(new Image(gamesLibrary.getGames()
                    .get(gamesLibrary.getGames().size() - 1).getImagePath()));
        } else
            clearStatisticsComponents();
    }

    private void reloadGUI() {
        FXMLLoader loader = new FXMLLoader();
        try {
            Locale.setDefault(new Locale(pref.get("games_library_locale", "en_US")));
            loader.setLocation(getClass().getClassLoader().getResource("fxml/main.fxml"));
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.messages");
            loader.setResources(resourceBundle);
            loader.load();
            MainController display = loader.getController();
            display.fillTableViewAfterGUIReload(gamesLibrary);
            Parent parent = loader.getRoot();
            Stage stage = Main.getMainStage();
            stage.setTitle(resourceBundle.getString("application.title"));
            Stage currentStage = (Stage) tableViewGames.getScene().getWindow();
            Scene scene = new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 39.5);
            stage.setScene(scene);
        } catch (IOException ioEcx) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    private void initTableViews() {
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumnProducer.setCellValueFactory(new PropertyValueFactory<>("producer"));
        tableColumnPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tableColumnReleaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        tableColumnReleaseDate.setCellFactory(col -> localDateFormat());
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnPrice.setCellFactory(col -> localDoubleFormat());

        tableViewGames.setItems(gameObservableList);
    }

    private TableCell<Game, Date> localDateFormat() {
        return new TableCell<Game, Date>() {
            @Override
            public void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    String locale = pref.get("games_library_locale", "en_US");
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,
                            new Locale(locale));
                    if (locale.equals("en_US")) {
                        String releasedDate = dateFormat.format(date).substring(0, dateFormat.format(date).lastIndexOf(' '));
                        setText(releasedDate.substring(0, releasedDate.lastIndexOf(' ')));
                    } else
                        setText(dateFormat.format(date).substring(0, dateFormat.format(date).lastIndexOf(' ')));
                }
            }
        };
    }

    private TableCell<Game, Double> localDoubleFormat() {
        return new TableCell<Game, Double>() {
            @Override
            public void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else
                    setText(decimalFormat.format(price) + numberFormat.getCurrency().getSymbol());
            }
        };
    }
}
