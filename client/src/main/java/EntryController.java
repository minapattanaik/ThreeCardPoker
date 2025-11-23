import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EntryController implements Initializable {

    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private ImageView heroImage;

    private JavaFXTemplate app;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/cards/A-H.png"));
            heroImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Could not load image. Check the file path.");
        }
    }

    public void setApp(JavaFXTemplate app, Stage stage) {
        this.app = app;
        this.stage = stage;
    }

    @FXML
    private void onConnectClicked() {
        try {
            String host = ipField.getText().isEmpty() ? "127.0.0.1" : ipField.getText();
            int port = portField.getText().isEmpty() ? 5555 : Integer.parseInt(portField.getText());

            boolean connected = app.connectToServer(host, port);

            if (connected) {
                System.out.println("Connected successfully!");
                app.showGameScene(stage);
            } else {
                System.out.println("Connection failed.");
                ipField.setText("Connection Failed");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Number");
            portField.setText("Invalid Port");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}