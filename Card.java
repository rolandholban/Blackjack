/**
 * Project: Blackjack
 * Date:    4/26/17
 * Author:  Roland Holban
 * Brief:   A Card object represents a card in a standard
 *          52 card deck (no jokers). 
 *          The card has a value and an image.
 */

class Card {

    // Variables for card value and image URL
    private final int VALUE;
    private final String IMAGE;

    // Constructor
    Card (int newValue, String newImage) {
        this.VALUE = newValue;
        this.IMAGE = newImage;
    }

    // Returns the value of this card
    int getValue() {
        return VALUE;
    }

    // Returns the image URL of this card
    String getImage() {
        return IMAGE;
    }

}
