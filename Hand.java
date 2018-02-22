import java.util.ArrayList;

/**
 * Project: Blackjack
 * Date:    4/26/17
 * Author:  Roland Holban
 * Brief:   A Hand object consists of a hand of cards.
 *          The hands total value can be computed.
 */

class Hand {

    // ArrayList of Cards to act as the players hand
    ArrayList<Card> hand;

    // Constructor
    Hand() { 
        hand = new ArrayList<>();
    }

    // Compute the total value of the hand
    int getValue() {
        int value = 0;
        boolean containsAces = false;

        // Check to see if hand contains aces
        for (Card card : hand) {
            if (card.getValue() == 1)
                containsAces = true;
        }

        // Compute the value of the hand with aces
        // counting as 1
        for (Card card : hand) {
            int currCardValue = card.getValue();
            if (currCardValue > 10)
                value += 10;
            else
                value += currCardValue;
        }

        // If the hand contains aces, and counting aces
        // as 11 would form a better hand, add 10 to
        // the hand value
        if (containsAces && value + 10 <= 21)
            value += 10;

        // Return the hand value
        return value;
    }

    // Returns how many cards in the hand
    int getHandSize() {
        return hand.size();
    }

}
