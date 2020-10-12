package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player testPlayer;

    @BeforeEach
    void runBefore() {
        testPlayer = new Player(100);
    }

    @Test
    void testPlaceBetValid() {
        testPlayer.placeBet(25);
        assertEquals(75, testPlayer.getPlayerBalance());
        assertEquals(25, testPlayer.getBet());
    }

    @Test
    void testPlaceBetInvalid() {
        testPlayer.placeBet(125);
        assertEquals(100, testPlayer.getPlayerBalance());
        assertEquals(0, testPlayer.getBet());
    }

    @Test
    void testDrawCardsValid() {
        assertEquals(0, testPlayer.getPlayerHand().length());
        testPlayer.placeBet(50);
        testPlayer.drawCards();
        assertEquals(2, testPlayer.getPlayerHand().length());
    }

    @Test
    void testDrawCardsInvalid() {
        assertEquals(0, testPlayer.getPlayerHand().length());
        testPlayer.drawCards();
        assertEquals(0, testPlayer.getPlayerHand().length());
    }

    @Test
    void testHit() {
        assertEquals(0, testPlayer.getPlayerHand().length());
        testPlayer.hit();
        assertEquals(1, testPlayer.getPlayerHand().length());
        testPlayer.hit();
        assertEquals(2, testPlayer.getPlayerHand().length());
    }

    @Test
    void testDoubleBet() {
        testPlayer.placeBet(30);
        assertEquals(30, testPlayer.getBet());

        testPlayer.doubleBet();
        assertEquals(40, testPlayer.getPlayerBalance());
        assertEquals(60, testPlayer.getBet());
        assertEquals(1, testPlayer.getPlayerHand().length());
    }

    @Test
    void testDoubleBetJustHit() {
        testPlayer.placeBet(70);
        assertEquals(70, testPlayer.getBet());
        assertEquals(30, testPlayer.getPlayerBalance());

        testPlayer.doubleBet();
        assertEquals(30, testPlayer.getPlayerBalance());
        assertEquals(70, testPlayer.getBet());
        assertEquals(1, testPlayer.getPlayerHand().length());
    }

    @Test
    void testPushEqual() {
        Deck deck = new Deck();
        deck.addCard("Ace", "Spades");
        deck.addCard("Ten", "Hearts");
        testPlayer.getPlayerHand().addCard("Ace", "Spades");
        testPlayer.getPlayerHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getPlayerBalance());
        assertEquals(40, testPlayer.getBet());

        testPlayer.push(deck);
        assertEquals(100, testPlayer.getPlayerBalance());
        assertEquals(0, testPlayer.getBet());
    }

    @Test
    void testPushNotEqual() {
        Deck deck = new Deck();
        deck.addCard("Ace", "Spades");
        deck.addCard("Ten", "Hearts");
        testPlayer.getPlayerHand().addCard("Five", "Spades");
        testPlayer.getPlayerHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getPlayerBalance());
        assertEquals(40, testPlayer.getBet());

        testPlayer.push(deck);
        assertEquals(60, testPlayer.getPlayerBalance());
        assertEquals(40, testPlayer.getBet());
    }

    @Test
    void testBustTrue() {
        testPlayer.getPlayerHand().addCard("King", "Spades");
        testPlayer.getPlayerHand().addCard("Ten", "Diamonds");
        testPlayer.getPlayerHand().addCard("Five", "Spades");
        testPlayer.placeBet(40);

        testPlayer.bust();
        assertEquals(0, testPlayer.getBet());
        assertEquals(0, testPlayer.getPlayerHand().length());
    }

    @Test
    void testBustFalse() {
        testPlayer.getPlayerHand().addCard("King", "Spades");
        testPlayer.getPlayerHand().addCard("Ten", "Diamonds");
        testPlayer.getPlayerHand().addCard("Ace", "Spades");
        testPlayer.placeBet(40);

        testPlayer.bust();
        assertEquals(40, testPlayer.getBet());
        assertEquals(3, testPlayer.getPlayerHand().length());
    }

}
