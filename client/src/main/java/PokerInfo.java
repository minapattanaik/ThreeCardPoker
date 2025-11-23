import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private int anteBet;
    private int pairPlusBet;
    private int playBet;

    private ArrayList<String> playerHand;
    private ArrayList<String> dealerHand;

    private double winnings;
    private double totalWinnings;
    private int roundsPlayed;
    private String gameMessage;

    public PokerInfo() {
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.totalWinnings = 1000.0;
        this.roundsPlayed = 0;
    }

    // getters & setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAnteBet() {
        return anteBet;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    public int getPlayBet() {
        return playBet;
    }

    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    public ArrayList<String> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<String> playerHand) {
        this.playerHand = playerHand;
    }

    public ArrayList<String> getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(ArrayList<String> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public double getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(double totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public void setGameMessage(String gameMessage) {
        this.gameMessage = gameMessage;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void incrementRoundsPlayed() {
        this.roundsPlayed++;
    }
}