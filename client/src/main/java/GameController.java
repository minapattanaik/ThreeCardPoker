import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.animation.FadeTransition;
import javafx.animation.Animation;
import java.util.ArrayList;

public class GameController {
    @FXML
    private Pane backgroundPane;
    @FXML
    private ImageView dealerCard1, dealerCard2, dealerCard3;
    @FXML
    private ImageView playerCard1, playerCard2, playerCard3;
    @FXML
    private Label pairPlusAmountLabel, anteAmountLabel, playAmountLabel;
    @FXML
    private Label totalWinningsLabel, currentRoundLabel;
    @FXML
    private VBox logVBox;
    @FXML
    private Button dealButton, playButton, foldButton;
    @FXML
    private ToggleButton pairPlusChip1, pairPlusChip5, pairPlusChip10, pairPlusChip15, pairPlusChip25;
    @FXML
    private ToggleButton anteChip1, anteChip5, anteChip10, anteChip15, anteChip25;

    private JavaFXTemplate app;
    private Stage stage;
    private static final int[] CHIP_VALUES = {1, 5, 10, 15, 25};
    private int pairPlusChipIndex = 1, anteChipIndex = 1;
    private int pairPlusTotal = 0, anteTotal = 0, playTotal = 0;
    private boolean hasDealt = false;

    public void setApp(JavaFXTemplate app, Stage stage) {
        this.app = app;
        this.stage = stage;
        initChipGroups();
        resetUI();
        restoreGameState();
        checkThemeForStars();
    }

    private void restoreGameState() {
        PokerInfo info = app.getPokerInfo();
        if (info != null) {
            // labels
            totalWinningsLabel.setText("$" + String.format("%.2f", info.getTotalWinnings()));
            currentRoundLabel.setText("$" + String.format("%.2f", info.getWinnings()));

            // cards
            if (info.getPlayerHand() != null && !info.getPlayerHand().isEmpty()) {
                updateGame(info);

                // bet variables
                anteTotal = info.getAnteBet();
                pairPlusTotal = info.getPairPlusBet();
                playTotal = info.getPlayBet();
                updateBetLabels();

                // if cards, then dealt
                hasDealt = true;
                enableBetting(false);
                dealButton.setDisable(true);
            }
        }
    }

    // pass network info to server
    public void updateGame(PokerInfo info) {
        updateCardsFromInfo(info);

        if ("DEAL".equals(info.getAction())) {
            addLog("Dealer has dealt.");
            playButton.setDisable(false);
            foldButton.setDisable(false);
        } else if ("FRESH_START".equals(info.getAction())) {
            app.getPokerInfo().setTotalWinnings(info.getTotalWinnings());
            totalWinningsLabel.setText("$" + String.format("%.2f", info.getTotalWinnings()));
            addLog("Game reset! Bankroll: $1000.00");
        } else if (info.getGameMessage() != null && !info.getGameMessage().isEmpty()) {
            finishRound(info);
        }
    }

    // update cards based on string parsing from server
    private void updateCardsFromInfo(PokerInfo info) {
        ArrayList<String> pHand = info.getPlayerHand();
        ArrayList<String> dHand = info.getDealerHand();

        if (pHand != null && pHand.size() == 3) {
            setCard(playerCard1, pHand.get(0));
            setCard(playerCard2, pHand.get(1));
            setCard(playerCard3, pHand.get(2));
        }

        if (dHand != null && dHand.size() == 3) {
            if ("DEAL".equals(info.getAction())) {
                setCardBack(dealerCard1);
                setCardBack(dealerCard2);
                setCardBack(dealerCard3);
            } else {
                setCard(dealerCard1, dHand.get(0));
                setCard(dealerCard2, dHand.get(1));
                setCard(dealerCard3, dHand.get(2));
            }
        }
    }

    private void setCard(ImageView slot, String cardCode) {
        String[] parts = cardCode.split("-");
        if (parts.length == 2) {
            slot.setImage(CardImageUtil.loadCard(parts[0], parts[1]));

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), slot);
            tt.setFromY(-20);
            tt.setToY(0);
            tt.play();
        }
    }

    private void setCardBack(ImageView slot) {
        try {
            slot.setImage(CardImageUtil.loadCardBack());
        } catch (Exception e) {
            slot.setImage(null);
        }
    }

    // actions
    @FXML
    private void onDealClicked() {
        if (anteTotal < 5 || anteTotal > 25) {
            addLog("Ante must be $5-$25");
            return;
        }
        if (pairPlusTotal > 0 && (pairPlusTotal < 5 || pairPlusTotal > 25)) {
            addLog("Pair Plus must be $5-$25");
            return;
        }

        PokerInfo info = new PokerInfo();
        info.setAction("DEAL");
        info.setAnteBet(anteTotal);
        info.setPairPlusBet(pairPlusTotal);
        app.sendToServer(info);

        hasDealt = true;
        enableBetting(false);
        dealButton.setDisable(true);
        addLog("Waiting for server...");
    }

    @FXML
    private void onPlayClicked() {
        playTotal = anteTotal;
        updateBetLabels();

        PokerInfo info = new PokerInfo();
        info.setAction("PLAY");
        info.setAnteBet(anteTotal);
        info.setPairPlusBet(pairPlusTotal);
        info.setPlayBet(playTotal);
        app.sendToServer(info);
        disablePlayButtons(true);
    }

    @FXML
    private void onFoldClicked() {
        PokerInfo info = new PokerInfo();
        info.setAction("FOLD");
        info.setAnteBet(anteTotal);
        info.setPairPlusBet(pairPlusTotal);
        app.sendToServer(info);
        disablePlayButtons(true);
    }

    private void finishRound(PokerInfo info) {
        addLog(info.getGameMessage());
        currentRoundLabel.setText("$" + String.format("%.2f", info.getWinnings()));
        totalWinningsLabel.setText("$" + String.format("%.2f", info.getTotalWinnings()));

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            try {
                app.getPokerInfo().setTotalWinnings(info.getTotalWinnings());
                app.getPokerInfo().setRoundsPlayed(info.getRoundsPlayed());
                String title = (info.getWinnings() > 0) ? "YOU WON!" : "ROUND OVER";
                app.showResultsScene(stage, title, info.getWinnings());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        pause.play();
    }

    // ui helpers
    private void resetUI() {
        PokerInfo info = app.getPokerInfo();
        totalWinningsLabel.setText("$" + String.format("%.2f", info.getTotalWinnings()));
        pairPlusTotal = 0;
        anteTotal = 0;
        playTotal = 0;
        hasDealt = false;
        updateBetLabels();
        currentRoundLabel.setText("$0.00");

        playerCard1.setImage(null);
        playerCard2.setImage(null);
        playerCard3.setImage(null);
        dealerCard1.setImage(null);
        dealerCard2.setImage(null);
        dealerCard3.setImage(null);

        logVBox.getChildren().clear();
        addLog("Welcome! Place Ante to start.");
        enableBetting(true);
        disablePlayButtons(true);
    }

    private void updateBetLabels() {
        pairPlusAmountLabel.setText("$" + pairPlusTotal);
        anteAmountLabel.setText("$" + anteTotal);
        playAmountLabel.setText("$" + playTotal);
    }

    public void addLog(String message) {
        Label logLabel = new Label(message);
        logLabel.getStyleClass().add("log-message");
        logLabel.setWrapText(true);
        logLabel.prefWidthProperty().bind(logVBox.widthProperty().subtract(20));
        logVBox.getChildren().add(0, logLabel);
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }

    @FXML
    private void onFreshStart() {
        PokerInfo info = new PokerInfo();
        info.setAction("FRESH_START");
        app.sendToServer(info);
        resetUI();
    }

    @FXML
    private void onNewLook() {
        app.toggleTheme();
        refreshCardImages();
        checkThemeForStars();
    }

    // helper for stars in night sky
    private void checkThemeForStars() {
        backgroundPane.getChildren().clear();
        if (app.getCurrentTheme().contains("nightstyle")) {
            generateStars();
        }
    }

    // star function
    private void generateStars() {
        int starCount = 100;
        for (int i = 0; i < starCount; i++) {
            double x = Math.random() * 1100;
            double y = Math.random() * 650;

            double r = 1 + Math.random() * 2;

            Circle star = new Circle(x, y, r, Color.WHITE);
            star.setOpacity(0.2);

            // twinkling
            FadeTransition fade = new FadeTransition(Duration.seconds(1 + Math.random() * 3), star);
            fade.setFromValue(0.2);
            fade.setToValue(1.0);
            fade.setCycleCount(Animation.INDEFINITE);
            fade.setAutoReverse(true);

            // randomized start time
            fade.setDelay(Duration.seconds(Math.random()));
            fade.play();

            backgroundPane.getChildren().add(star);
        }
    }

    @FXML
    private void onRules() {
        try {
            app.showRulesScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // image refresh with theme change
    private void refreshCardImages() {
        PokerInfo info = app.getPokerInfo();
        updateCardsFromInfo(info);
    }

    @FXML
    private void onPairPlusChipClicked(ActionEvent e) {
        pairPlusChipIndex = updateChipSelection(e, new ToggleButton[]{pairPlusChip1, pairPlusChip5, pairPlusChip10, pairPlusChip15, pairPlusChip25}, pairPlusChipIndex);
    }

    @FXML
    private void onAnteChipClicked(ActionEvent e) {
        anteChipIndex = updateChipSelection(e, new ToggleButton[]{anteChip1, anteChip5, anteChip10, anteChip15, anteChip25}, anteChipIndex);
    }

    @FXML
    private void onPairPlusIncrease() {
        if (!hasDealt) {
            pairPlusTotal += currentChipValue(pairPlusChipIndex);
            updateBetLabels();
        }
    }

    @FXML
    private void onPairPlusDecrease() {
        if (!hasDealt) {
            pairPlusTotal = Math.max(0, pairPlusTotal - currentChipValue(pairPlusChipIndex));
            updateBetLabels();
        }
    }

    @FXML
    private void onAnteIncrease() {
        if (!hasDealt) {
            anteTotal += currentChipValue(anteChipIndex);
            updateBetLabels();
        }
    }

    @FXML
    private void onAnteDecrease() {
        if (!hasDealt) {
            anteTotal = Math.max(0, anteTotal - currentChipValue(anteChipIndex));
            updateBetLabels();
        }
    }

    private void enableBetting(boolean enable) {
        dealButton.setDisable(!enable);
    }

    private void disablePlayButtons(boolean disable) {
        playButton.setDisable(disable);
        foldButton.setDisable(disable);
    }

    private void initChipGroups() {
        selectChip(new ToggleButton[]{pairPlusChip1, pairPlusChip5, pairPlusChip10, pairPlusChip15, pairPlusChip25}, pairPlusChipIndex);
        selectChip(new ToggleButton[]{anteChip1, anteChip5, anteChip10, anteChip15, anteChip25}, anteChipIndex);
    }

    private int updateChipSelection(ActionEvent event, ToggleButton[] group, int currentIndex) {
        ToggleButton source = (ToggleButton) event.getSource();
        int index = 0;
        for (int i = 0; i < group.length; i++) {
            if (group[i] == source) index = i;
        }
        selectChip(group, index);
        return index;
    }

    private void selectChip(ToggleButton[] group, int index) {
        for (int i = 0; i < group.length; i++) group[i].setSelected(i == index);
    }

    private int currentChipValue(int index) {
        return CHIP_VALUES[index];
    }
}