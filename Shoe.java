import java.util.Collections;
import java.util.Stack;

/**
 * Project: Blackjack
 * Date:    4/26/17
 * Author:  Roland Holban
 * Brief:   A shoe object contains decks. 
 *          A shoe can be shuffled and cards 
 *          can be dealt from it.
 */

class Shoe {
    
    // Stack of decks acting as the shoe
    Stack<Card> shoeStack;
    int numDecks;
    
    // Constructor. Creates a shoe containing 
    // user specified number of decks
    Shoe(int numDecks) {
        Deck tempDeck = new Deck();
        this.numDecks = numDecks;
        this.shoeStack = new Stack<>();
        for (int i = 0; i < numDecks; i++) {
            this.shoeStack.addAll(tempDeck.deck);
        }
    }
    
    // Shuffles the shoe
    void shuffle() {
        Collections.shuffle(shoeStack);
    }

    // Deals a card from the shoe
    Card dealCard() {
        return this.shoeStack.pop();
    }
    
    // Return the image of the top card
    String peek() {
        return this.shoeStack.peek().getImage();
    }
    

}
