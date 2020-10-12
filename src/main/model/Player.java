package model;

// represents a Blackjack Player having a balance, hand of cards, and a bet
public class Player {

    private int playerBalance;
    private Deck playerHand;
    private int bet;


    // EFFECTS: creates a new Player with balance, empty hand, and 0 bet
    public Player(int balance) {
        playerBalance = balance;
        playerHand = new Deck();
        bet = 0;
    }


    public int getPlayerBalance() {
        return playerBalance;
    }


    public Deck getPlayerHand() {
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
    // EFFECTS: if both decks have same value, return bet to Player's balance
    public void push(Deck deck) {
        if (playerHand.getDeckValue() == deck.getDeckValue()) {
            playerBalance += bet;
            bet = 0;
        }
    }


    // REQUIRES: Player must have drawn 2 cards then hit at least once
    // MODIFIES: this
    // EFFECTS: if playerHand is a bust, then lose bet money and get an empty hand of cards
    public void bust() {
        if (playerHand.isBust()) {
            bet = 0;
            playerHand = new Deck();
        }
    }


}
