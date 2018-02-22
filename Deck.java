import java.util.Stack;

/**
 * Project: Blackjack
 * Date:    4/26/17
 * Author:  Roland Holban
 * Brief:   A Deck object contains 52 images.
 *          Deck is un-shuffled.
 */

class Deck {

    // Create a stack of Cards to act as the deck
    Stack<Card> deck;

    // Constructor - creates an un-shuffled deck
    Deck() {
        this.deck = new Stack<>();
        // For every suit
        for (int i = 1; i < 5; i++) {
            // For every value
            for (int j = 1; j < 14; j++) {
                switch (i) {
                    case 1:
                        deck.push(new Card(j, "images/h" + Integer.toString(j) + ".png"));
                        break;
                    case 2:
                        deck.push(new Card(j, "images/s" + Integer.toString(j) + ".png"));
                        break;
                    case 3:
                        deck.push(new Card(j, "images/d" + Integer.toString(j) + ".png"));
                        break;
                    case 4:
                        deck.push(new Card(j, "images/c" + Integer.toString(j) + ".png"));
                        break;
                }
            }
        }
    }
    
}
