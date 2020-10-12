package ui;

import model.Player;

public class Main {
    public static void main(String[] args) {

        Player p = new Player(100);
        p.placeBet(50);
        System.out.println(p.getBet());
        p.getPlayerHand().addCard("Ace", "Spades");
        p.getPlayerHand().addCard("Ace", "Hearts");
        p.getPlayerHand().addCard("King", "Hearts");
        p.getPlayerHand().addCard("Ten", "Hearts");
        System.out.println(p.getPlayerHand().getDeckValue());
        System.out.println(p.getPlayerHand().isBust());
    }
}
