import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ServerIntroController {

    @FXML
    private TextField portField;
    @FXML
    private Label statusLabel;
    private JavaFXTemplate app;

    public void setApp(JavaFXTemplate app) {
        this.app = app;
    }

    @FXML
    private void onStartServer() {
        try {
            int port = Integer.parseInt(portField.getText());
            app.showLogScene(port);
        } catch (NumberFormatException e) {
            statusLabel.setText("INVALID PORT #");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("STARTUP ERROR");
        }
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }
}