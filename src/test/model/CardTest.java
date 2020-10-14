package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.Card.FACE_NAMES;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card testCard;

    @BeforeEach
    void runBefore() {
        testCard = new Card("Ace", "Spades");
    }

    @Test
    void testPrintCard() {
        assertEquals("Ace of Spades", testCard.printCard());
    }

    @Test
    void testGetFaceName() {
        assertEquals("Ace", testCard.getFaceName());
    }

    @Test
    void testGetSuit() {
        assertEquals("Spades", testCard.getSuit());
    }

    @Test
    void testGetCardValue() {
        List<Card> cards = new ArrayList<>();

        for (String faceName : FACE_NAMES) {
            cards.add(new Card(faceName, "Spades"));
        }

        assertEquals(11, cards.get(0).getCardValue());
        assertEquals(2, cards.get(1).getCardValue());
        assertEquals(3, cards.get(2).getCardValue());
        assertEquals(4, cards.get(3).getCardValue());
        assertEquals(5, cards.get(4).getCardValue());
        assertEquals(6, cards.get(5).getCardValue());
        assertEquals(7, cards.get(6).getCardValue());
        assertEquals(8, cards.get(7).getCardValue());
        assertEquals(9, cards.get(8).getCardValue());
        assertEquals(10, cards.get(9).getCardValue());
        assertEquals(10, cards.get(10).getCardValue());
        assertEquals(10, cards.get(11).getCardValue());
        assertEquals(10, cards.get(12).getCardValue());
    }


}