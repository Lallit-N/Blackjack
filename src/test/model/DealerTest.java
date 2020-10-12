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
        assertEquals(0, testDealer.getDealerHand().length());
        testDealer.drawCards();
        assertEquals(2, testDealer.getDealerHand().length());
    }

    @Test
    void testHit() {
        testDealer.getDealerHand().addCard("Seven", "Hearts");
        testDealer.getDealerHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getDealerHand().length());
        testDealer.hit();
        assertEquals(3, testDealer.getDealerHand().length());
    }

    @Test
    void testNoHit() {
        testDealer.getDealerHand().addCard("Ace", "Hearts");
        testDealer.getDealerHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getDealerHand().length());
        testDealer.hit();
        assertEquals(2, testDealer.getDealerHand().length());
    }

    @Test
    void testBust() {
        testDealer.getDealerHand().addCard("King", "Hearts");
        testDealer.getDealerHand().addCard("Nine", "Hearts");
        testDealer.getDealerHand().addCard("Eight", "Hearts");
        assertEquals(3, testDealer.getDealerHand().length());

        testDealer.bust();
        assertEquals(0, testDealer.getDealerHand().length());
    }

    @Test
    void testNotBust() {
        testDealer.getDealerHand().addCard("King", "Hearts");
        testDealer.getDealerHand().addCard("Nine", "Hearts");
        assertEquals(2, testDealer.getDealerHand().length());

        testDealer.bust();
        assertEquals(2, testDealer.getDealerHand().length());
    }
}
