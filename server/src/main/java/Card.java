import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}

    private final int rank;
    private final Suit suit;

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}