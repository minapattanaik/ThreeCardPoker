public class Result {

    private String result;          // e.g., "Player wins", "Dealer wins", "Player folds"
    private double winnings;        // net amount won this round (can be negative)
    private boolean dealerQualifies;
    private boolean tie;

    public Result(String result, double winnings,
                  boolean dealerQualifies, boolean tie) {
        this.result = result;
        this.winnings = winnings;
        this.dealerQualifies = dealerQualifies;
        this.tie = tie;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public boolean isDealerQualifies() {
        return dealerQualifies;
    }

    public void setDealerQualifies(boolean dealerQualifies) {
        this.dealerQualifies = dealerQualifies;
    }

    public boolean isTie() {
        return tie;
    }

    public void setTie(boolean tie) {
        this.tie = tie;
    }
}