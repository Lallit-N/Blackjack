package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {
    private Dealer testDealer;

    @BeforeEach
    void runBefore() {
        testDealer = new Dealer();
    }

    @Test
    void testDrawCards() {
        assertEquals(0, testDealer.getHand().length());
        testDealer.drawCards();
        assertEquals(2, testDealer.getHand().length());
    }

    @Test
    void testStandHitEqualValues() {
        Player player = new Player(100);
        player.getHand().addCard("Seven", "Hearts");
        player.getHand().addCard("Nine", "Hearts");

        testDealer.getHand().addCard("Seven", "Hearts");
        testDealer.getHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getHand().length());
        testDealer.stand(player);
        assertEquals(3, testDealer.getHand().length());
    }

    @Test
    void testStandHit() {
        Player player = new Player(100);
        player.getHand().addCard("Seven", "Hearts");
        player.getHand().addCard("Seven", "Hearts");

        testDealer.getHand().addCard("Seven", "Hearts");
        testDealer.getHand().addCard("Eight", "Hearts");
        assertEquals(2, testDealer.getHand().length());
        testDealer.stand(player);
        assertEquals(2, testDealer.getHand().length());
    }

    @Test
    void testStandNoHit() {
        Player player = new Player(100);
        player.getHand().addCard("Ace", "Hearts");
        player.getHand().addCard("Nine", "Hearts");

        testDealer.getHand().addCard("Ace", "Hearts");
        testDealer.getHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getHand().length());
        testDealer.stand(player);
        assertEquals(2, testDealer.getHand().length());
    }

    @Test
    void testBust() {
        testDealer.getHand().addCard("King", "Hearts");
        testDealer.getHand().addCard("Nine", "Hearts");
        testDealer.getHand().addCard("Eight", "Hearts");
        assertEquals(3, testDealer.getHand().length());

        assertTrue(testDealer.bust());
        assertEquals(0, testDealer.getHand().length());
    }

    @Test
    void testNotBust() {
        testDealer.getHand().addCard("King", "Hearts");
        testDealer.getHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getHand().length());

        assertFalse(testDealer.bust());
        assertEquals(2, testDealer.getHand().length());
    }

    @Test
    void testShowFirstCard() {
        testDealer.getHand().addCard("King", "Hearts");
        testDealer.getHand().addCard("Eight", "Spades");
        assertEquals("King of Hearts", testDealer.showFirstCard());
    }
}
