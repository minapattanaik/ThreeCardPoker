import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ThreeCardLogicTest {

    // creating cards
    private Card c(Card.Suit s, int r) {
        return new Card(r, s);
    }

    private ArrayList<Card> hand(Card a, Card b, Card c) {
        ArrayList<Card> h = new ArrayList<>();
        h.add(a); h.add(b); h.add(c);
        return h;
    }

    // ranking tests

    @Test
    public void testEvalHandHighCard() {
        System.out.println("\nTesting Eval: High Card (No matches)");
        ArrayList<Card> h = hand(
                c(Card.Suit.SPADES, 2),
                c(Card.Suit.HEARTS, 7),
                c(Card.Suit.CLUBS, 11)
        );
        assertEquals(ThreeCardLogic.HIGH_CARD, ThreeCardLogic.evalHand(h));
        System.out.println("High Card test passed!");
    }

    @Test
    public void testEvalHandPair() {
        System.out.println("\nTesting Eval: Pair (Two cards same rank)");
        ArrayList<Card> h = hand(
                c(Card.Suit.SPADES, 10),
                c(Card.Suit.HEARTS, 10),
                c(Card.Suit.CLUBS, 3)
        );
        assertEquals(ThreeCardLogic.PAIR, ThreeCardLogic.evalHand(h));
        System.out.println("Pair test passed!");
    }

    @Test
    public void testEvalHandFlush() {
        System.out.println("\nTesting Eval: Flush (Three cards same suit)");
        ArrayList<Card> h = hand(
                c(Card.Suit.DIAMONDS, 2),
                c(Card.Suit.DIAMONDS, 7),
                c(Card.Suit.DIAMONDS, 13)
        );
        assertEquals(ThreeCardLogic.FLUSH, ThreeCardLogic.evalHand(h));
        System.out.println("Flush test passed!");
    }

    @Test
    public void testEvalHandStraightAceHigh() {
        System.out.println("\nTesting Eval: Straight (Q, K, A sequence)");
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 12),
                c(Card.Suit.SPADES, 13),
                c(Card.Suit.HEARTS, 14)
        );
        assertEquals(ThreeCardLogic.STRAIGHT, ThreeCardLogic.evalHand(h));
        System.out.println("Straight (Ace High) test passed!");
    }

    @Test
    public void testEvalHandStraightAceLow() {
        System.out.println("\nTesting Eval: Straight (A, 2, 3 sequence)");
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 2),
                c(Card.Suit.SPADES, 3),
                c(Card.Suit.HEARTS, 14)
        );
        assertEquals(ThreeCardLogic.STRAIGHT, ThreeCardLogic.evalHand(h));
        System.out.println("Straight (Ace Low) test passed!");
    }

    @Test
    public void testEvalHandThreeOfAKind() {
        System.out.println("\nTesting Eval: Three of a Kind");
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 9),
                c(Card.Suit.SPADES, 9),
                c(Card.Suit.HEARTS, 9)
        );
        assertEquals(ThreeCardLogic.THREE_OF_A_KIND, ThreeCardLogic.evalHand(h));
        System.out.println("Three of a Kind test passed!");
    }

    @Test
    public void testEvalHandStraightFlush() {
        System.out.println("\nTesting Eval: Straight Flush");
        ArrayList<Card> h = hand(
                c(Card.Suit.SPADES, 8),
                c(Card.Suit.SPADES, 9),
                c(Card.Suit.SPADES, 10)
        );
        assertEquals(ThreeCardLogic.STRAIGHT_FLUSH, ThreeCardLogic.evalHand(h));
        System.out.println("Straight Flush test passed!");
    }

    // pair plus tests

    @Test
    public void testEvalPPWinningsNoHand() {
        System.out.println("\nTesting PairPlus: High Card (Should return 0)");
        ArrayList<Card> h = hand(
                c(Card.Suit.SPADES, 2),
                c(Card.Suit.HEARTS, 7),
                c(Card.Suit.CLUBS, 11)
        );
        assertEquals(0, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Loss test passed!");
    }

    @Test
    public void testEvalPPWinningsPair() {
        System.out.println("\nTesting PairPlus: Pair (Pays 1 to 1)");
        ArrayList<Card> h = hand(
                c(Card.Suit.SPADES, 5),
                c(Card.Suit.HEARTS, 5),
                c(Card.Suit.CLUBS, 9)
        );
        assertEquals(10, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Pair test passed!");
    }

    @Test
    public void testEvalPPWinningsFlush() {
        System.out.println("\nTesting PairPlus: Flush (Pays 3 to 1)");
        ArrayList<Card> h = hand(
                c(Card.Suit.HEARTS, 2),
                c(Card.Suit.HEARTS, 9),
                c(Card.Suit.HEARTS, 12)
        );
        assertEquals(30, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Flush test passed!");
    }

    @Test
    public void testEvalPPWinningsStraight() {
        System.out.println("\nTesting PairPlus: Straight (Pays 6 to 1)");
        ArrayList<Card> h = hand(
                c(Card.Suit.HEARTS, 4),
                c(Card.Suit.SPADES, 5),
                c(Card.Suit.CLUBS, 6)
        );
        assertEquals(60, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Straight test passed!");
    }

    @Test
    public void testEvalPPWinningsThreeOfAKind() {
        System.out.println("\nTesting PairPlus: Three of a Kind (Pays 30 to 1)");
        ArrayList<Card> h = hand(
                c(Card.Suit.HEARTS, 7),
                c(Card.Suit.SPADES, 7),
                c(Card.Suit.CLUBS, 7)
        );
        assertEquals(300, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Three of a Kind test passed!");
    }

    @Test
    public void testEvalPPWinningsStraightFlush() {
        System.out.println("\nTesting PairPlus: Straight Flush (Pays 40 to 1)");
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 9),
                c(Card.Suit.CLUBS, 10),
                c(Card.Suit.CLUBS, 11)
        );
        assertEquals(400, ThreeCardLogic.evalPPWinnings(h, 10));
        System.out.println("PairPlus Straight Flush test passed!");
    }

    // comparing hands tests

    @Test
    public void testCompareHandsPlayerWins() {
        System.out.println("\nTesting Compare: Player (Pair) beats Dealer (High Card)");
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 2), c(Card.Suit.HEARTS, 7), c(Card.Suit.CLUBS, 11));
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 10), c(Card.Suit.HEARTS, 10), c(Card.Suit.CLUBS, 3));

        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Player Win test passed!");
    }

    @Test
    public void testCompareHandsDealerWins() {
        System.out.println("\nTesting Compare: Dealer (Straight Flush) beats Player (Straight)");
        ArrayList<Card> dealer = hand(c(Card.Suit.DIAMONDS, 9), c(Card.Suit.DIAMONDS, 10), c(Card.Suit.DIAMONDS, 11));
        ArrayList<Card> player = hand(c(Card.Suit.HEARTS, 4), c(Card.Suit.SPADES, 5), c(Card.Suit.CLUBS, 6));

        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Dealer Win test passed!");
    }

    @Test
    public void testCompareHandsTieExact() {
        System.out.println("\nTesting Compare: Exact Tie");
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 2), c(Card.Suit.SPADES, 3), c(Card.Suit.SPADES, 4));
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 2), c(Card.Suit.SPADES, 3), c(Card.Suit.SPADES, 4));

        assertEquals(0, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Exact Tie test passed!");
    }

    @Test
    public void testCompareHandsStraightBeatsFlush() {
        System.out.println("\nTesting Rule: Straight should beat Flush (3-Card Rule)");
        ArrayList<Card> dealer = hand(c(Card.Suit.HEARTS, 2), c(Card.Suit.HEARTS, 8), c(Card.Suit.HEARTS, 11)); // flush
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 6), c(Card.Suit.CLUBS, 7), c(Card.Suit.DIAMONDS, 8)); // straight

        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Straight > Flush test passed!");
    }

    @Test
    public void testCompareHandsSameTypeRankCheck() {
        System.out.println("\nTesting Compare: Same hand type (Pair vs Pair), check ranks");
        // dealer - pair of 4s
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 4), c(Card.Suit.HEARTS, 4), c(Card.Suit.CLUBS, 9));
        // player - pair of 6s
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 6), c(Card.Suit.CLUBS, 6), c(Card.Suit.HEARTS, 3));

        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Rank check test passed!");
    }

    // edge cases & tiebreakers

    @Test
    public void testEvalHandUnsortedStraight() {
        System.out.println("\nTesting Edge: Unsorted Straight Input (3-A-2)");
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 3),
                c(Card.Suit.SPADES, 14),
                c(Card.Suit.HEARTS, 2)
        );
        assertEquals(ThreeCardLogic.STRAIGHT, ThreeCardLogic.evalHand(h));
        System.out.println("Unsorted Straight test passed!");
    }

    @Test
    public void testEvalHandHighestHighCard() {
        System.out.println("\nTesting Edge: Highest possible High Card (A-K-J)");
        // a-k-q is straight, so a-k-j should be best high card
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 14),
                c(Card.Suit.SPADES, 13),
                c(Card.Suit.HEARTS, 11)
        );
        assertEquals(ThreeCardLogic.HIGH_CARD, ThreeCardLogic.evalHand(h));
        System.out.println("Highest High Card test passed!");
    }

    @Test
    public void testEvalHandLowestHighCard() {
        System.out.println("\nTesting Edge: Lowest possible High Card (5-3-2)");
        // can't be 4-3-2 or suited
        ArrayList<Card> h = hand(
                c(Card.Suit.CLUBS, 2),
                c(Card.Suit.SPADES, 3),
                c(Card.Suit.HEARTS, 5)
        );
        assertEquals(ThreeCardLogic.HIGH_CARD, ThreeCardLogic.evalHand(h));
        System.out.println("Lowest High Card test passed!");
    }

    @Test
    public void testCompareHighCardSecondKicker() {
        System.out.println("\nTesting Tie-Breaker: High Cards match, 2nd card decides");
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 14), c(Card.Suit.HEARTS, 9), c(Card.Suit.CLUBS, 2));
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 14), c(Card.Suit.CLUBS, 10), c(Card.Suit.SPADES, 2));

        // player wins (10 > 9)
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Second Kicker test passed!");
    }

    @Test
    public void testCompareHighCardThirdKicker() {
        System.out.println("\nTesting Tie-Breaker: 1st & 2nd match, 3rd card decides");
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 13), c(Card.Suit.HEARTS, 11), c(Card.Suit.CLUBS, 5));
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 13), c(Card.Suit.CLUBS, 11), c(Card.Suit.SPADES, 4));

        // dealer wins (5 > 4)
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Third Kicker test passed!");
    }

    @Test
    public void testComparePairsSameRankDifferentKicker() {
        System.out.println("\nTesting Tie-Breaker: Same Pair Rank, Kicker decides");
        // dealer: pair of 8s, King kicker
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 8), c(Card.Suit.HEARTS, 8), c(Card.Suit.CLUBS, 13));
        // player: pair of 8s, Queen kicker
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 8), c(Card.Suit.CLUBS, 8), c(Card.Suit.SPADES, 12));

        // dealer wins (K > Q)
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Pair Kicker test passed!");
    }

    @Test
    public void testCompareFlushesByRank() {
        System.out.println("\nTesting Tie-Breaker: Flush vs Flush (High card wins)");
        ArrayList<Card> dealer = hand(c(Card.Suit.HEARTS, 13), c(Card.Suit.HEARTS, 11), c(Card.Suit.HEARTS, 2));
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 12), c(Card.Suit.SPADES, 10), c(Card.Suit.SPADES, 9));

        // dealer wins (K > Q)
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Flush Rank test passed!");
    }

    @Test
    public void testCompareAceLowVsNormalStraight() {
        System.out.println("\nTesting Edge: A-2-3 (Lowest Straight) vs 2-3-4");
        // 3-high straight
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 14), c(Card.Suit.HEARTS, 2), c(Card.Suit.CLUBS, 3));
        // 4-high straight
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 2), c(Card.Suit.CLUBS, 3), c(Card.Suit.SPADES, 4));

        // player wins (4 > 3)
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Low Straight comparison test passed!");
    }

    @Test
    public void testCompareHighestVsSecondHighestStraight() {
        System.out.println("\nTesting Edge: A-K-Q (Highest) vs K-Q-J");
        ArrayList<Card> dealer = hand(c(Card.Suit.SPADES, 13), c(Card.Suit.HEARTS, 12), c(Card.Suit.CLUBS, 11));
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 14), c(Card.Suit.CLUBS, 13), c(Card.Suit.SPADES, 12));

        // player wins (ace high straight beats king high)
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("High Straight comparison test passed!");
    }

    @Test
    public void testCompareStraightFlushVsStraightFlush() {
        System.out.println("\nTesting Tie-Breaker: Straight Flush vs Straight Flush");
        ArrayList<Card> dealer = hand(c(Card.Suit.HEARTS, 4), c(Card.Suit.HEARTS, 5), c(Card.Suit.HEARTS, 6));
        ArrayList<Card> player = hand(c(Card.Suit.SPADES, 5), c(Card.Suit.SPADES, 6), c(Card.Suit.SPADES, 7));

        // player wins (7 high > 6 high)
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("SF Comparison test passed!");
    }

    @Test
    public void testEvalPPWinningsHighBet() {
        System.out.println("\nTesting Pair Plus: Math check with Large Bet ($500)");
        // straight flush is 40:1
        ArrayList<Card> h = hand(c(Card.Suit.CLUBS, 10), c(Card.Suit.CLUBS, 11), c(Card.Suit.CLUBS, 12));
        // 500 * 40 = 20000
        assertEquals(20000, ThreeCardLogic.evalPPWinnings(h, 500));
        System.out.println("Large Bet test passed!");
    }

    @Test
    public void testCompareIdenticalHandsDifferentSuits() {
        System.out.println("\nTesting Edge: Exact Tie (Identical Ranks, Different Suits)");
        ArrayList<Card> dealer = hand(c(Card.Suit.HEARTS, 14), c(Card.Suit.CLUBS, 13), c(Card.Suit.SPADES, 11));
        ArrayList<Card> player = hand(c(Card.Suit.DIAMONDS, 14), c(Card.Suit.SPADES, 13), c(Card.Suit.HEARTS, 11));

        // should be 0 (true tie)
        assertEquals(0, ThreeCardLogic.compareHands(dealer, player));
        System.out.println("Suit Independence test passed!");
    }
}