import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;

public class ResultsController {

    @FXML private Label resultTitle;
    @FXML private Label roundWinningsLabel;
    @FXML private Label totalWinningsLabel;
    @FXML private Label roundsPlayedLabel;

    @FXML private Pane backgroundPane;
    @FXML private VBox contentBox;

    private JavaFXTemplate app;
    private Stage stage;

    public void setApp(JavaFXTemplate app, Stage stage) {
        this.app = app;
        this.stage = stage;
    }

    public void setResults(String title, double roundWin) {
        resultTitle.setText(title);
        roundWinningsLabel.setText("WINNINGS THIS ROUND: $" + String.format("%.2f", roundWin));

        boolean playerWon = roundWin > 0;

        if (playerWon) {
            resultTitle.setStyle("-fx-text-fill: #5ac54f;");
        } else {
            resultTitle.setStyle("-fx-text-fill: #d95763;");
        }

        PokerInfo info = app.getPokerInfo();
        if (info != null) {
            totalWinningsLabel.setText("TOTAL WINNINGS: $" + String.format("%.2f", info.getTotalWinnings()));
            roundsPlayedLabel.setText("ROUNDS PLAYED: " + info.getRoundsPlayed());
        }

        // win/loss passed to animation for different ones
        animateEntrance(playerWon);
    }

    // creates custom animations based on win/loss according to theme
    private void animateEntrance(boolean won) {
        backgroundPane.getChildren().clear();
        contentBox.setOpacity(0);
        contentBox.setScaleX(0.8);
        contentBox.setScaleY(0.8);

        FadeTransition ft = new FadeTransition(Duration.millis(800), contentBox);
        ft.setFromValue(0); ft.setToValue(1);

        ScaleTransition st = new ScaleTransition(Duration.millis(800), contentBox);
        st.setFromX(0.8); st.setFromY(0.8);
        st.setToX(1.0); st.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(ft, st);
        pt.play();

        String theme = app.getCurrentTheme();

        if (theme.contains("nightstyle")) {
            if (won) generateNightStars();
            else generateNightRain();
        }
        else if (theme.contains("pixelstyle")) {
            generatePixelParticles(won);
        }
        else if (theme.contains("classicstyle")) {
            generateClassicEffects(won);
        }
    }

    // night theme helper; generates stars
    private void generateNightStars() {
        int starCount = 80;
        for (int i = 0; i < starCount; i++) {
            double x = Math.random() * 1100;
            double y = Math.random() * 650;
            double r = 2 + Math.random() * 3;
            double d = Math.random();
            Color c;
            if ( d  > 0.5) {
                c = Color.WHITE;
            } else {
                c = Color.GOLD;
            }

            Circle star = new Circle(x, y, r, c);
            star.setOpacity(0.0);

            FadeTransition fade = new FadeTransition(Duration.seconds(0.5 + Math.random() * 2), star);
            fade.setFromValue(0.0); fade.setToValue(0.8);
            fade.setCycleCount(Animation.INDEFINITE);
            fade.setAutoReverse(true);
            fade.setDelay(Duration.seconds(Math.random()));
            fade.play();

            backgroundPane.getChildren().add(star);
        }
    }

    // loss animation for night sky
    private void generateNightRain() {
        int dropCount = 100;
        for (int i = 0; i < dropCount; i++) {
            double x = Math.random() * 1100;
            double startY = -50 - (Math.random() * 600);

            Rectangle rain = new Rectangle(x, startY, 2, 15);
            rain.setFill(Color.LIGHTBLUE);
            rain.setOpacity(0.6);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + Math.random()), rain);
            tt.setByY(800); // fall
            tt.setCycleCount(Animation.INDEFINITE);
            tt.play();

            backgroundPane.getChildren().add(rain);
        }
    }

    // pixel helpers
    private void generatePixelParticles(boolean won) {
        int count = 60;
        for (int i = 0; i < count; i++) {
            double x = Math.random() * 1100;
            double size = 5 + Math.random() * 15;
            Rectangle pixel;
            TranslateTransition tt;

            if (won) {
                double y = 650 + Math.random() * 200;
                double d = Math.random();
                Color c;
                if ( d  > 0.5) {
                    c = Color.WHITE;
                } else {
                    c = Color.GOLD;
                }
                pixel = new Rectangle(x, y, size, size);
                pixel.setFill(c);

                tt = new TranslateTransition(Duration.seconds(3 + Math.random() * 3), pixel);
                tt.setByY(-800); // upward
            } else { // loss
                double y = -50 - Math.random() * 200;
                double d = Math.random();
                Color c;
                if ( d  > 0.5) {
                    c = Color.DARKRED;
                } else {
                    c = Color.DARKGREY;
                }
                pixel = new Rectangle(x, y, size, size);
                pixel.setFill(c);

                tt = new TranslateTransition(Duration.seconds(2 + Math.random() * 2), pixel);
                tt.setByY(800); // down
            }

            tt.setCycleCount(Animation.INDEFINITE);
            tt.play();
            backgroundPane.getChildren().add(pixel);
        }
    }

    // classic animation helpers
    private void generateClassicEffects(boolean won) {
        int count = 60;
        Color[] chipColors = {Color.RED, Color.BLACK, Color.WHITE, Color.GREEN};

        for (int i = 0; i < count; i++) {
            double x = Math.random() * 1100;
            double r = 4 + Math.random() * 6;
            Circle shape = new Circle(x, 0, r);
            TranslateTransition tt;

            if (won) {
                // falling chips
                shape.setCenterY(-50 - Math.random() * 200);
                shape.setFill(chipColors[(int)(Math.random() * chipColors.length)]);
                shape.setOpacity(0.8);

                tt = new TranslateTransition(Duration.seconds(2 + Math.random() * 3), shape);
                tt.setByY(800);
            } else {
                // float away
                shape.setCenterY(650 + Math.random() * 200);
                shape.setFill(Color.LIGHTGREY);
                shape.setOpacity(0.2);

                tt = new TranslateTransition(Duration.seconds(5 + Math.random() * 5), shape);
                tt.setByY(-800);
            }

            tt.setCycleCount(Animation.INDEFINITE);
            tt.setDelay(Duration.seconds(Math.random()));
            tt.play();
            backgroundPane.getChildren().add(shape);
        }
    }

    @FXML private void onPlayAgain() {
        try { app.showGameScene(stage); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void onExit() {
        System.exit(0);
    }
}