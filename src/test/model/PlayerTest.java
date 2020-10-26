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
        assertEquals(75, testPlayer.getBalance());
        assertEquals(25, testPlayer.getBet());
    }

    @Test
    void testPlaceBetInvalid() {
        testPlayer.placeBet(125);
        assertEquals(100, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());
    }

    @Test
    void testDrawCardsValid() {
        assertEquals(0, testPlayer.getHand().length());
        testPlayer.placeBet(50);
        testPlayer.drawCards();
        assertEquals(2, testPlayer.getHand().length());
    }

    @Test
    void testDrawCardsInvalid() {
        assertEquals(0, testPlayer.getHand().length());
        testPlayer.drawCards();
        assertEquals(0, testPlayer.getHand().length());
    }

    @Test
    void testHit() {
        assertEquals(0, testPlayer.getHand().length());
        testPlayer.hit();
        assertEquals(1, testPlayer.getHand().length());
        testPlayer.hit();
        assertEquals(2, testPlayer.getHand().length());
    }

    @Test
    void testDoubleBet() {
        testPlayer.placeBet(30);
        assertEquals(30, testPlayer.getBet());

        testPlayer.doubleBet();
        assertEquals(40, testPlayer.getBalance());
        assertEquals(60, testPlayer.getBet());
        assertEquals(1, testPlayer.getHand().length());
    }

    @Test
    void testDoubleBetJustHit() {
        testPlayer.placeBet(70);
        assertEquals(70, testPlayer.getBet());
        assertEquals(30, testPlayer.getBalance());

        testPlayer.doubleBet();
        assertEquals(30, testPlayer.getBalance());
        assertEquals(70, testPlayer.getBet());
        assertEquals(1, testPlayer.getHand().length());
    }

    @Test
    void testPushTrue() {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard("Ace", "Spades");
        dealer.getHand().addCard("Ten", "Hearts");
        testPlayer.getHand().addCard("Ace", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());

        assertTrue(testPlayer.push(dealer));
        assertEquals(100, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());
    }

    @Test
    void testPushFalse() {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard("Ace", "Spades");
        dealer.getHand().addCard("Ten", "Hearts");
        testPlayer.getHand().addCard("Five", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());

        assertFalse(testPlayer.push(dealer));
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());
    }

    @Test
    void testStandTrue() {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard("Ace", "Spades");
        dealer.getHand().addCard("Ace", "Hearts");
        testPlayer.getHand().addCard("Five", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());

        assertTrue(testPlayer.stand(dealer));
        assertEquals(140, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());

    }

    @Test
    void testStandFalse() {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard("Ace", "Spades");
        dealer.getHand().addCard("Ten", "Hearts");
        testPlayer.getHand().addCard("Five", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());

        testPlayer.stand(dealer);
        assertFalse(testPlayer.stand(dealer));
        assertEquals(60, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());

    }

    @Test
    void testWin() {
        testPlayer.getHand().addCard("Five", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.placeBet(40);
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());
        assertEquals(2, testPlayer.getHand().length());

        testPlayer.win();
        assertEquals(140, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());
        assertEquals(0, testPlayer.getHand().length());
    }

    @Test
    void testBustTrue() {
        testPlayer.getHand().addCard("King", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.getHand().addCard("Five", "Spades");
        testPlayer.placeBet(40);

        assertTrue(testPlayer.bust());
        assertEquals(0, testPlayer.getBet());
        assertEquals(0, testPlayer.getHand().length());
    }

    @Test
    void testBustFalse() {
        testPlayer.getHand().addCard("King", "Spades");
        testPlayer.getHand().addCard("Ten", "Diamonds");
        testPlayer.getHand().addCard("Ace", "Spades");
        testPlayer.placeBet(40);

        assertFalse(testPlayer.bust());
        assertEquals(40, testPlayer.getBet());
        assertEquals(3, testPlayer.getHand().length());
    }

    @Test
    void testBlackjackTrue() {
        testPlayer.getHand().addCard("King", "Spades");
        testPlayer.getHand().addCard("Ace", "Spades");
        testPlayer.placeBet(40);

        assertTrue(testPlayer.blackjack());
        assertEquals(160, testPlayer.getBalance());
        assertEquals(0, testPlayer.getBet());
    }

    @Test
    void testBlackjackFalse() {
        testPlayer.getHand().addCard("King", "Spades");
        testPlayer.getHand().addCard("One", "Spades");
        testPlayer.placeBet(40);

        assertFalse(testPlayer.blackjack());
        assertEquals(60, testPlayer.getBalance());
        assertEquals(40, testPlayer.getBet());
    }
}
