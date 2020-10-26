package model;

// represents the Blackjack Dealer with a hand of cards
public class Dealer {

    private Deck dealerHand;

    // EFFECTS: creates a Dealer with an empty hand of cards
    public Dealer() {
        dealerHand = new Deck();
    }


    public Deck getHand() {
        return dealerHand;
    }


    // REQUIRES: dealerHand must be empty
    // MODIFIES: this
    // EFFECTS: draw 2 cards to start the game
    public void drawCards() {
        dealerHand.drawCard();
        dealerHand.drawCard();
    }


    // REQUIRES: dealerHand must contain at least 2 cards
    // MODIFIES: this
    // EFFECTS: adds another card to dealerHand until deck value >= 17
    public void hit() {
        while (dealerHand.getDeckValue() < 17) {
            dealerHand.drawCard();
        }
    }


    // REQUIRES: Dealer must have drawn 2 cards then hit at least once
    // MODIFIES: this
    // EFFECTS: if dealerHand is a bust, then reset the hand of cards and return true;
    //          otherwise, return false
    public boolean bust() {
        if (dealerHand.isBust()) {
            dealerHand = new Deck();
            return true;
        } else {
            return false;
        }
    }


    // EFFECTS: returns the first card in the deck as a string
    public String showFirstCard() {
        return dealerHand.getFaceNames().get(0) + " of " + dealerHand.getSuits().get(0);
    }

}
