package model;

// represents a playing card with name, suit, and value
public class Card {

    public static final String[] SUITS = {"Diamonds", "Clubs", "Hearts", "Spades"};
    public static final String[] FACE_NAMES =
            {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    private int cardValue;
    private String faceName;
    private String suit;


    // REQUIRES: faceName must be one of FACE_NAMES and suit must be one of SUITS
    // EFFECTS: creates a new card with faceName and suit
    public Card(String faceName, String suit) {
        this.faceName = faceName;
        this.suit = suit;
        cardValue = getCardValue();
    }


    // EFFECTS: return card as a string
    public String printCard() {
        return faceName + " of " + suit;
    }

    public String getFaceName() {
        return faceName;
    }

    public String getSuit() {
        return suit;
    }


    // EFFECTS: return the value associated with the card's faceNamed
    public int getCardValue() {
        if ("Ace".equals(this.getFaceName())) {
            return 11;
        } else if ("Two".equals(this.getFaceName())) {
            return 2;
        } else if ("Three".equals(this.getFaceName())) {
            return 3;
        } else if ("Four".equals(this.getFaceName())) {
            return 4;
        } else if ("Five".equals(this.getFaceName())) {
            return 5;
        } else if ("Six".equals(this.getFaceName())) {
            return 6;
        } else if ("Seven".equals(this.getFaceName())) {
            return 7;
        } else if ("Eight".equals(this.getFaceName())) {
            return 8;
        } else if ("Nine".equals(this.getFaceName())) {
            return 9;
        } else {
            return 10;
        }
    }
}
