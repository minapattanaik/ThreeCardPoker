import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class JavaFXTemplate extends Application {

    private Stage primaryStage;
    private Server server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);

        showIntroScene();
    }

    public void showIntroScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server_intro.fxml"));
        Parent root = loader.load();

        ServerIntroController controller = loader.getController();
        controller.setApp(this);

        Scene scene = new Scene(root, 700, 500); // window for server
        scene.getStylesheets().add(getClass().getResource("/css/serverstyle.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Poker Server - Config");
        primaryStage.show();
    }

    public void showLogScene(int port) throws Exception {
        // init Server Backend
        server = new Server(port);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server_log.fxml"));
        Parent root = loader.load();

        ServerLogController controller = loader.getController();
        controller.setApp(this, server);

        server.setController(controller);
        server.startServer();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/css/serverstyle.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Poker Server - Running");
    }

    @Override
    public void stop() throws Exception {
        if (server != null) server.stopServer();
        super.stop();
        System.exit(0);
    }
}