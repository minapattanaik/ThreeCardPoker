import java.util.ArrayList;

public class GamePlay {

    private final Deck deck;
    private Hand currentHand;
    private Hand dealerHand;
    private double winnings;

    public GamePlay() {
        this.deck = new Deck();
        resetRound();
    }

    public Hand getCurrentHand() {
        return currentHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public double getWinnings() {
        return winnings;
    }

    public void dealHands() {
        resetRound();
        currentHand = new Hand();
        dealerHand = new Hand();
        for (int i = 0; i < 3; i++) currentHand.addCard(deck.dealCard());
        for (int i = 0; i < 3; i++) dealerHand.addCard(deck.dealCard());
    }

    public Result evalHands(int anteBet, int pairPlusBet, boolean playerFolds) {
        double net = 0.0;
        String message;

        boolean dealerQualifies = dealerQualifies(dealerHand);
        boolean tie = false;

        // pair plus
        if (!playerFolds && pairPlusBet > 0) {
            int ppWin = ThreeCardLogic.evalPPWinnings(new ArrayList<>(currentHand.getCards()), pairPlusBet);
            if (ppWin > 0) {
                net += ppWin;
            } else {
                net -= pairPlusBet;
            }
        } else if (playerFolds && pairPlusBet > 0) {
            net -= pairPlusBet;
        }

        // ante + play (should be equal)
        if (playerFolds) {
            net -= anteBet;
            message = "Player folds. Lost Ante & Pair Plus.";
            return new Result(message, net, dealerQualifies, false);
        }

        int playBet = anteBet;
        int result = ThreeCardLogic.compareHands(new ArrayList<>(dealerHand.getCards()), new ArrayList<>(currentHand.getCards()));

        if (!dealerQualifies) {
            net += anteBet;
            message = "Dealer does not qualify (Queen High). Ante wins.";
        } else {
            if (result == 2) {
                net += anteBet + playBet;
                message = "Player wins! (Ante & Play pay 1:1)";
            } else if (result == 1) {
                net -= (anteBet + playBet);
                message = "Dealer wins.";
            } else {
                message = "It's a tie. Bets push.";
                tie = true;
            }
        }

        winnings = net;
        return new Result(message, winnings, dealerQualifies, tie);
    }

    private boolean dealerQualifies(Hand dealerHand) {
        int maxRank = 0;
        for (Card c : dealerHand.getCards()) {
            if (c.getRank() > maxRank) maxRank = c.getRank();
        }
        int type = dealerHand.evalRank();
        if (type > ThreeCardLogic.HIGH_CARD) return true;

        return maxRank >= 12;
    }

    public void resetRound() {
        deck.reset();
        currentHand = null;
        dealerHand = null;
        winnings = 0.0;
    }
}