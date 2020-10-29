package model;

import org.json.JSONObject;
import persistence.Writable;

// represents a Blackjack Player having a balance, hand of cards, and a bet
public class Player implements Writable {

    private int playerBalance;
    private Deck playerHand;
    private int bet;


    // EFFECTS: creates a new Player with balance of INITIAL_BALANCE, empty hand, and 0 bet
    public Player(int balance) {
        playerBalance = balance;
        playerHand = new Deck();
        bet = 0;
    }


    public int getBalance() {
        return playerBalance;
    }


    public Deck getHand() {
        return playerHand;
    }


    public int getBet() {
        return bet;
    }


    // MODIFIES: this
    // EFFECTS: if b <= playerBalance add the amount b to the Player's bet and subtract b from
    //          playerBalance; also, draw 2 cards
    public void placeBet(int b) {
        if (b <= playerBalance) {
            playerBalance -= b;
            bet = b;
        }
    }


    // REQUIRES: playerHand must be empty
    // MODIFIES: this
    // EFFECTS: draw 2 cards to start the game, if bet > 0
    public void drawCards() {
        if (bet > 0) {
            playerHand.drawCard();
            playerHand.drawCard();
        }
    }


    // REQUIRES: playerHand must contain at least 2 cards
    // MODIFIES: this
    // EFFECTS: adds another card to playerHand
    public void hit() {
        playerHand.drawCard();

    }


    // REQUIRES: playerHand must only have 2 cards
    // MODIFIES: this
    // EFFECTS: if playerBalance >= bet, subtract initial bet amount from balance,
    //          double the bet, and add a card to playerHand; otherwise, call hit()
    public void doubleBet() {
        if (playerBalance >= bet) {
            playerBalance -= bet;
            bet = 2 * bet;
            playerHand.drawCard();
        } else {
            hit();
        }
    }


    // REQUIRES: both Player and Dealer decks must have at least 2 cards (meaning that the
    //           Player has placed a bet)
    // MODIFIES: this
    // EFFECTS: if both decks have same value, return bet to Player's balance, reset hand and return true; otherwise,
    //          return false
    public boolean push(Dealer dealer) {
        if (playerHand.getDeckValue() == dealer.getHand().getDeckValue()) {
            playerBalance += bet;
            bet = 0;
            playerHand = new Deck();
            return true;
        } else {
            return false;
        }
    }


    // REQUIRES: both Player and Dealer decks must have at least 2 cards (meaning that the
    //           Player has placed a bet)
    // MODIFIES: this
    // EFFECTS: if playerHand has greater value than dealerHand, return true, add 2*bet to Balance, and reset
    //          bet and hand; otherwise, return false and reset bet and hand
    public boolean stand(Dealer dealer) {
        if (playerHand.getDeckValue() > dealer.getHand().getDeckValue()) {
            playerBalance += 2 * bet;
            bet = 0;
            playerHand = new Deck();
            return true;
        } else {
            bet = 0;
            playerHand = new Deck();
            return false;
        }
    }


    // REQUIRES: only called when dealer busts
    // MODIFIES: this
    // EFFECTS: updates the player's balance and hand, as well as setting the bet back to 0
    public void win() {
        playerBalance += 2 * bet;
        bet = 0;
        playerHand = new Deck();
    }


    // REQUIRES: Player must have drawn 2 cards then hit at least once
    // MODIFIES: this
    // EFFECTS: if playerHand is a bust, lose bet money and get an empty hand of cards and return true;
    //          otherwise, return false
    public boolean bust() {
        if (playerHand.isBust()) {
            bet = 0;
            playerHand = new Deck();
            return true;
        } else {
            return false;
        }
    }


    // REQUIRES: playerHand can only have 2 cards
    // MODIFIES: this
    // EFFECTS: return true if value of playerHand equals 21 and add 2.5*bet to player balance, and
    //          reset bet and hand; otherwise, return false
    public boolean blackjack() {
        if (playerHand.getDeckValue() == 21) {
            playerBalance += 2.5 * bet;
            bet = 0;
            playerHand = new Deck();
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: stores player balance as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("balance", playerBalance);
        return json;
    }
}
