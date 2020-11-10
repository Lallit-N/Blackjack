package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static model.Card.FACE_NAMES;
import static model.Card.SUITS;

// represents a deck of cards
public class Deck {

    private List<Card> cards;
    private int deckValue;


    // EFFECTS: creates a new deck with 0 cards and 0 deck value
    public Deck() {
        this.cards = new ArrayList<>();
        deckValue = 0;
    }


    // REQUIRES: method can only be called on an empty deck designated to be a playing deck
    // MODIFIES: this
    // EFFECTS: create a full deck of 52 cards
    public void playingDeck() {
        for (String s : SUITS) {
            for (String fn : FACE_NAMES) {
                cards.add(new Card(fn, s));
            }
        }
    }


    // EFFECTS: returns the deck as a string, with each card on a new line
    public String printDeck() {
        String stringOfDeck = "";
        for (Card c : cards) {
            stringOfDeck += c.getFaceName() + " of " + c.getSuit() + "\n";
        }
        return stringOfDeck;
    }


    // MODIFIES: this
    // EFFECTS: adds a card with faceName fn and suit s to the deck
    public void addCard(String fn, String s) {
        Card newCard = new Card(fn, s);
        cards.add(newCard);
    }


    // MODIFIES: this
    // EFFECTS: add a random card from the playingDeck to the current deck
    public void drawCard() {
        Deck playingDeck = new Deck();
        playingDeck.playingDeck();
        int randomInt = new Random().nextInt(playingDeck.length());
        cards.add(playingDeck.getCard(randomInt));
    }


    // EFFECTS: return the card at index index from the deck
    public Card getCard(int index) {
        return cards.get(index);
    }


    // EFFECTS: return a list of the cards in the deck
    public List<Card> getCards() {
        return cards;
    }


    // EFFECTS: returns the length of the deck
    public int length() {
        return cards.size();
    }


    // EFFECTS: return a list of string of the faceNames of all the cards in the deck
    public List<String> getFaceNames() {
        List<String> faceNames = new ArrayList<>();
        for (Card c : cards) {
            faceNames.add(c.getFaceName());
        }
        return faceNames;
    }


    // EFFECTS: return a list of string of the suits of all the cards in the deck
    public List<String> getSuits() {
        List<String> suits = new ArrayList<>();
        for (Card c : cards) {
            suits.add(c.getSuit());
        }
        return suits;
    }


    // MODIFIES: this
    // EFFECTS: return the sum of all the card values following the Blackjack rule for Aces
    public int getDeckValue() {
        deckValue = 0;
        for (Card c : cards) {
            deckValue += c.getCardValue();
        }
        for (int i = numberOfAces(); (deckValue > 21) && i > 0; i--) {
            deckValue -= 10;
        }
        return deckValue;
    }


    // EFFECTS: returns true if the deck value is greater than 21
    public boolean isBust() {
        return getDeckValue() > 21;
    }


    // EFFECTS: return the number of Aces in the deck
    public int numberOfAces() {
        int numAces = 0;
        for (Card c : cards) {
            if (c.getFaceName().equals("Ace")) {
                numAces++;
            }
        }
        return numAces;
    }

}
