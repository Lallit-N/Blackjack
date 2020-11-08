package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private Deck testDeck;

    @BeforeEach
    void runBefore() {
        testDeck = new Deck();
    }

    @Test
    void testPlayingDeck() {
        testDeck.playingDeck();
        assertEquals(52, testDeck.length());
    }

    @Test
    void testPrintDeck() {
        testDeck.addCard("Ace", "Spades");
        testDeck.addCard("Two", "Hearts");
        assertEquals("Ace of Spades\nTwo of Hearts\n", testDeck.printDeck());
    }

    @Test
    void testAddCard() {
        testDeck.addCard("Ace", "Spades");
        assertEquals(1, testDeck.length());
        testDeck.addCard("Two", "Hearts");
        assertEquals(2, testDeck.length());
    }

    @Test
    void testDrawCard() {
        assertEquals(0, testDeck.length());
        testDeck.drawCard();
        assertEquals(1, testDeck.length());
        testDeck.drawCard();
        assertEquals(2, testDeck.length());
    }

    @Test
    void testGetCard() {
        testDeck.playingDeck();
        Card c1 = new Card("Ace", "Diamonds");

        assertEquals(c1.printCard(), testDeck.getCard(0).printCard());
    }

    @Test
    void testGetCards() {
        testDeck.playingDeck();
        assertEquals(52, testDeck.getCards().size());
    }

    @Test
    void testGetFaceNames() {
        testDeck.addCard("Ace", "Spades");
        testDeck.addCard("Two", "Hearts");
        testDeck.addCard("King", "Clubs");
        List<String> faceNames = new ArrayList<>();
        faceNames.add("Ace");
        faceNames.add("Two");
        faceNames.add("King");

        assertEquals(faceNames, testDeck.getFaceNames());
    }

    @Test
    void testGetSuits() {
        testDeck.addCard("Ace", "Spades");
        testDeck.addCard("Two", "Hearts");
        testDeck.addCard("King", "Clubs");
        List<String> suits = new ArrayList<>();
        suits.add("Spades");
        suits.add("Hearts");
        suits.add("Clubs");

        assertEquals(suits, testDeck.getSuits());
    }

    @Test
    void testGetDeckValue() {
        testDeck.addCard("Ace", "Spades");
        testDeck.addCard("Ace", "Hearts");
        testDeck.addCard("King", "Clubs");

        assertEquals(12, testDeck.getDeckValue());
    }

    @Test
    void testIsBust() {
        assertFalse(testDeck.isBust());

        testDeck.addCard("Five", "Spades");
        testDeck.addCard("Jack", "Hearts");
        assertFalse(testDeck.isBust());

        testDeck.addCard("King", "Clubs");
        assertTrue(testDeck.isBust());
    }

    @Test
    void testNumberOfAces() {
        testDeck.addCard("Two", "Hearts");
        testDeck.addCard("King", "Clubs");
        assertEquals(0, testDeck.numberOfAces());

        testDeck.addCard("Ace", "Spades");
        assertEquals(1, testDeck.numberOfAces());
    }

}
