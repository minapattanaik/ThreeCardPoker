import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class JavaFXTemplate extends Application {

    private ClientConnect client;
    private PokerInfo globalPokerInfo = new PokerInfo();
    private Object currentController;

    private String currentTheme = "/css/pixelstyle.css";
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage; // Capture the stage

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/entry.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);
        Parent root = loader.load();

        EntryController controller = loader.getController();
        controller.setApp(this, primaryStage);

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Three Card Poker Client");
        primaryStage.show();
    }

    // switches themes on click
    public void toggleTheme() {
        if (currentTheme.equals("/css/pixelstyle.css")) {
            currentTheme = "/css/classicstyle.css";
            CardImageUtil.setCardTheme("classic");
        } else if(currentTheme.equals("/css/classicstyle.css")) {
            currentTheme = "/css/nightstyle.css";
            CardImageUtil.setCardTheme("night");
        } else {
            currentTheme = "/css/pixelstyle.css";
            CardImageUtil.setCardTheme("pixel");
        }

        if (primaryStage != null && primaryStage.getScene() != null) {
            primaryStage.getScene().getStylesheets().clear();
            primaryStage.getScene().getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
        }
    }

    public boolean connectToServer(String ip, int port) {
        client = new ClientConnect(data -> {
            PokerInfo info = (PokerInfo) data;
            globalPokerInfo = info;
            if (currentController instanceof GameController) {
                Platform.runLater(() -> ((GameController) currentController).updateGame(info));
            }
        });

        try {
            client.connect(ip, port);
            return true;
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return false;
        }
    }

    public void sendToServer(PokerInfo info) {
        if (client != null) client.send(info);
    }

    public PokerInfo getPokerInfo() {
        return globalPokerInfo;
    }

    public void showGameScene(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);
        Parent root = loader.load();

        GameController controller = loader.getController();
        controller.setApp(this, stage);
        this.currentController = controller;

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
        stage.setScene(scene);
    }

    public void showResultsScene(Stage stage, String title, double roundWin) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/results.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);
        Parent root = loader.load();

        ResultsController controller = loader.getController();
        controller.setApp(this, stage);
        this.currentController = controller;

        controller.setResults(title, roundWin);

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
        stage.setScene(scene);
    }

    public void showRulesScene(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rules.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);
        Parent root = loader.load();

        RulesController controller = loader.getController();
        controller.setApp(this, stage);

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());

        stage.setScene(scene);
    }

    public String getCurrentTheme() {
        return currentTheme;
    }
}