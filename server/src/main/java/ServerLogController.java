import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerLogController {

    @FXML
    private Label clientCountLabel;
    @FXML
    private Label gameCountLabel;
    @FXML
    private ListView<String> logListView;

    private JavaFXTemplate app;
    private Server server;
    private ObservableList<String> logs = FXCollections.observableArrayList();

    public void setApp(JavaFXTemplate app, Server server) {
        this.app = app;
        this.server = server;
        logListView.setItems(logs);
    }

    public void log(String msg) {
        logs.add(0, msg); // Add to top
    }

    public void updateClientCount(int count) {
        clientCountLabel.setText("CONNECTED CLIENTS: " + count);
    }

    public void updateTotalGames(int count) {
        gameCountLabel.setText("TOTAL GAMES: " + count);
    }

    @FXML
    private void onStopServer() {
        try {
            app.showIntroScene();
            if (server != null) server.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClearLog() {
        logs.clear();
    }
}