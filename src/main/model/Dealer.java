package model;

import exceptions.IllegalHandSizeException;

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


    // MODIFIES: this
    // EFFECTS: draw 2 cards to start the game; throws, IllegalHandSizeException if dealerHand is not empty
    public void drawCards() throws IllegalHandSizeException {
        if (dealerHand.length() != 0) {
            throw new IllegalHandSizeException();
        } else {
            dealerHand.drawCard();
            dealerHand.drawCard();
        }
    }


    // MODIFIES: this
    // EFFECTS: adds another card to dealerHand until deck value >= 17
    public void stand(Player player) {
        int playerHandValue = player.getHand().getDeckValue();
        while (dealerHand.getDeckValue() < 17 && dealerHand.getDeckValue() <= playerHandValue) {
            dealerHand.drawCard();
        }
    }


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
