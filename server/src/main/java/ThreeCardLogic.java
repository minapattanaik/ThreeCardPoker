import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThreeCardLogic {

    // hand type ranking (higher = better)
    public static final int HIGH_CARD = 0;
    public static final int PAIR = 1;
    public static final int FLUSH = 2;
    public static final int STRAIGHT = 3;
    public static final int THREE_OF_A_KIND = 4;
    public static final int STRAIGHT_FLUSH = 5;

    /**
     * Evaluate a 3-card hand and return a hand type code.
     */
    public static int evalHand(ArrayList<Card> hand) {
        if (hand == null || hand.size() != 3) {
            throw new IllegalArgumentException("Hand must contain exactly 3 cards");
        }

        List<Card> sorted = new ArrayList<>(hand);
        sorted.sort(Comparator.comparingInt(Card::getRank));

        int r1 = sorted.get(0).getRank();
        int r2 = sorted.get(1).getRank();
        int r3 = sorted.get(2).getRank();

        boolean flush = isFlush(sorted);
        boolean straight = isStraight(sorted);
        boolean threeKind = (r1 == r2 && r2 == r3);
        boolean pair = (r1 == r2 || r2 == r3 || r1 == r3);

        if (flush && straight) {
            return STRAIGHT_FLUSH;
        } else if (threeKind) {
            return THREE_OF_A_KIND;
        } else if (straight) {
            return STRAIGHT;
        } else if (flush) {
            return FLUSH;
        } else if (pair) {
            return PAIR;
        } else {
            return HIGH_CARD;
        }
    }

    /**
     * Pair Plus winnings for a given hand and bet.
     * Uses the payout table from the project PDF.
     */
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int type = evalHand(hand);

        switch (type) {
            case STRAIGHT_FLUSH:
                return bet * 40; // 40 to 1
            case THREE_OF_A_KIND:
                return bet * 30; // 30 to 1
            case STRAIGHT:
                return bet * 6;  // 6 to 1
            case FLUSH:
                return bet * 3;  // 3 to 1
            case PAIR:
                return bet;      // 1 to 1
            default:
                return 0;        // loses Pair Plus
        }
    }

    /**
     * Compare dealer and player hands.
     *
     * @return 0 if tie, 1 if dealer wins, 2 if player wins.
     */
    public static int compareHands(ArrayList<Card> dealer,
                                   ArrayList<Card> player) {
        int dealerType = evalHand(dealer);
        int playerType = evalHand(player);

        if (dealerType > playerType) {
            return 1;
        } else if (playerType > dealerType) {
            return 2;
        }

        // same type = use rank vectors for tie-breaking
        List<Integer> dealerRankVec = handRankVector(dealer, dealerType);
        List<Integer> playerRankVec = handRankVector(player, playerType);

        for (int i = 0; i < dealerRankVec.size(); i++) {
            int d = dealerRankVec.get(i);
            int p = playerRankVec.get(i);
            if (d > p) {
                return 1;
            } else if (p > d) {
                return 2;
            }
        }

        // exact tie
        return 0;
    }

    private static boolean isFlush(List<Card> sorted) {
        return sorted.get(0).getSuit() == sorted.get(1).getSuit()
                && sorted.get(1).getSuit() == sorted.get(2).getSuit();
    }

    /**
     * Straight with Ace-high and Ace-low (A-2-3) support.
     */
    private static boolean isStraight(List<Card> sorted) {
        int r1 = sorted.get(0).getRank();
        int r2 = sorted.get(1).getRank();
        int r3 = sorted.get(2).getRank();

        // Normal consecutive
        if (r2 == r1 + 1 && r3 == r2 + 1) {
            return true;
        }

        // Ace-low straight: A-2-3 (ranks 2,3,14)
        return (r1 == 2 && r2 == 3 && r3 == 14);
    }

    /**
     * Build a rank vector used for tie-breaking.
     * Higher lexicographic vector wins.
     */
    private static List<Integer> handRankVector(ArrayList<Card> hand, int type) {
        List<Card> sorted = new ArrayList<>(hand);
        sorted.sort(Comparator.comparingInt(Card::getRank));

        int r1 = sorted.get(0).getRank();
        int r2 = sorted.get(1).getRank();
        int r3 = sorted.get(2).getRank();

        List<Integer> vec = new ArrayList<>();

        switch (type) {
            case STRAIGHT:
            case STRAIGHT_FLUSH:
                // a-2-3 = 3 high
                if (r1 == 2 && r2 == 3 && r3 == 14) {
                    vec.add(3);
                } else {
                    vec.add(r3);
                }
                break;

            case THREE_OF_A_KIND:
                // all same rank = any card is fine
                vec.add(r1);
                break;

            case FLUSH:
            case HIGH_CARD:
                // compare highest, then middle, then lowest
                vec.add(r3);
                vec.add(r2);
                vec.add(r1);
                break;

            case PAIR:
                int pairRank;
                int kickerRank;
                if (r1 == r2) {
                    pairRank = r1;
                    kickerRank = r3;
                } else if (r2 == r3) {
                    pairRank = r2;
                    kickerRank = r1;
                } else {
                    pairRank = r1;
                    kickerRank = r2;
                }
                vec.add(pairRank);
                vec.add(kickerRank);
                break;

            default:
                // Should not happen
                vec.add(r3);
                vec.add(r2);
                vec.add(r1);
        }

        return vec;
    }
}
