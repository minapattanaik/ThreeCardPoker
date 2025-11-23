import javafx.fxml.FXML;
import javafx.stage.Stage;

public class RulesController {

    private JavaFXTemplate app;
    private Stage stage;

    public void setApp(JavaFXTemplate app, Stage stage) {
        this.app = app;
        this.stage = stage;
    }

    // returns to game screen
    @FXML
    private void onBack() {
        try {
            app.showGameScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}