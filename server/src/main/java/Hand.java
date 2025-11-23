import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public int evalRank() {
        if (cards.size() != 3) {
            throw new IllegalStateException("Hand must contain exactly 3 cards");
        }
        return ThreeCardLogic.evalHand(new ArrayList<>(cards));
    }
}