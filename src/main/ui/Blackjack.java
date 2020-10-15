package ui;

import model.Dealer;
import model.Player;

import java.util.Scanner;

// Used ca.ubc.cpsc210.bank.ui.TellerApp as a general guideline to start designing this class

// Blackjack Game
public class Blackjack {
    private Player player;
    private Dealer dealer;
    private Scanner input;
    boolean keepRunning = true;

    // EFFECTS: runs the Blackjack game
    public Blackjack() {
        runBlackjack();
    }


    // MODIFIES: this
    // EFFECTS: starts the game
    public void runBlackjack() {
        String command;
        input = new Scanner(System.in);

        initialize();

        while (keepRunning && player.getPlayerBalance() > 0) {
            placeBet();

            if (player.blackjack()) {
                System.out.println("\nBLACKJACK!!!");
                playAgain();
            } else {
                displayOptions();
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("l")) {
                    keepRunning = false;
                } else {
                    processCommand(command);
                }
            }
        }
        System.out.println("\nGoodbye!");
    }

    // REQUIRES: input must be an integer value only (no letters or special characters)
    // MODIFIES: this
    // EFFECTS: display welcome message and initialize the Player with user input
    private void initialize() {
        boolean notDone = true;
        int command;

        System.out.println("Welcome to Blackjack!");
        System.out.println("Please enter your buy-in amount...");
        command = input.nextInt();

        while (notDone) {
            if (command > 0) {
                player = new Player(command);
                notDone = false;
            } else {
                System.out.println("Please enter an amount greater than $0...");
                command = input.nextInt();
            }
        }
    }

    // REQUIRES: input must be an integer value only (no letters or special characters)
    // MODIFIES: this
    // EFFECTS: user places a bet and draws two cards
    private void placeBet() {
        dealer = new Dealer();
        boolean notDone = true;
        int command;

        System.out.println("\nYour Balance: $" + player.getPlayerBalance());
        System.out.println("How much would you like to bet?");
        command = input.nextInt();

        while (notDone) {
            if (command <= player.getPlayerBalance()) {
                player.placeBet(command);
                player.drawCards();
                dealer.drawCards();
                notDone = false;
            } else {
                System.out.println("Please enter a valid amount...");
                command = input.nextInt();
            }
        }
        printHands();
    }

    // EFFECTS: print the player and dealer hands (dealer has hidden card)
    void printHands() {
        System.out.println("\nYour Hand:\n" + player.getPlayerHand().printDeck());
        System.out.println("Your Hand Value: " + player.getPlayerHand().getDeckValue());
        System.out.println("\n\nDealer's Hand:\n" + dealer.showFirstCard() + "\n[HIDDEN CARD]");
    }

    // EFFECTS: displays options to Player
    private void displayOptions() {
        System.out.println("\nYour Balance: $" + player.getPlayerBalance());
        System.out.println("Select:");
        System.out.println("\th -> hit");
        System.out.println("\td -> double");
        System.out.println("\ts -> stand");
        System.out.println("\tl -> leave");
    }

    // MODIFIES: this
    // EFFECTS: processes Player's command
    private void processCommand(String command) {
        boolean notDone = true;
        String cmd = command;

        while (notDone) {
            if (cmd.equals("h")) {
                doHit();
                notDone = false;
            } else if (cmd.equals("d")) {
                doDouble();
                notDone = false;
            } else if (cmd.equals("s")) {
                doStand();
                notDone = false;
            } else {
                System.out.println("Please make a valid selection...");
                cmd = input.next();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draw a card to the Player's hand; the Player either busts, or gets more options
    private void doHit() {
        String command;

        player.hit();
        printHands();

        if (player.bust()) {
            System.out.println("\nBUST");
            playAgain();
        } else {
            afterHitOptions();
            command = input.next();
            command = command.toLowerCase();
            processCommandAfterHit(command);
        }
    }

    // EFFECTS: display options that the Player has after hitting
    private void afterHitOptions() {
        System.out.println("\nYour Balance: $" + player.getPlayerBalance());
        System.out.println("Select:");
        System.out.println("\th -> hit");
        System.out.println("\ts -> stand");
    }

    // MODIFIES: this
    // EFFECTS: processes Player's command after they already hit or double and hit
    private void processCommandAfterHit(String command) {
        boolean notDone = true;
        String cmd = command;

        while (notDone) {
            if (cmd.equals("h")) {
                doHit();
                notDone = false;
            } else if (cmd.equals("s")) {
                doStand();
                notDone = false;
            } else {
                System.out.println("Please make a valid selection...");
                cmd = input.next();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: double Player's bet and draw a card; the Player either busts, or gets more options
    private void doDouble() {
        String command;

        player.doubleBet();
        printHands();

        if (player.bust()) {
            System.out.println("\nBUST");
            playAgain();
        } else {
            afterHitOptions();
            command = input.next();
            command = command.toLowerCase();
            processCommandAfterHit(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: compares player and dealer hands to determine the overall outcome of the round
    private void doStand() {
        doDealerHit();

        if (dealer.bust()) {
            player.win();
            System.out.println("\nDEALER BUSTED, YOU WIN!");
        } else if (player.push(dealer)) {
            System.out.println("\nPUSH");
        } else if (player.stand(dealer)) {
            System.out.println("\nYOU WIN!");
        } else {
            System.out.println("\nDEALER WINS");
        }

        playAgain();
    }

    // MODIFIES: this
    // EFFECTS: if dealerHand value is less than 17, then dealer hits until dealerHand value is 17 or more,
    //          then shows final hands of both the player and dealer
    void doDealerHit() {
        if (dealer.getDealerHand().getDeckValue() < 17) {
            showHands();
            dealer.hit();
            System.out.println("\n\nDealer hits...");
        } else {
            System.out.println("\nDealer does not hit...");
        }
        System.out.println("\nFinal Hands:");
        showHands();
    }

    // EFFECTS: show final hands of player and dealer
    void showHands() {
        System.out.println("Your Hand:\n" + player.getPlayerHand().printDeck());
        System.out.println("Your Hand Value: " + player.getPlayerHand().getDeckValue());
        System.out.println("\n\nDealer's Hand:\n" + dealer.getDealerHand().printDeck());
        System.out.println("Dealer Hand Value: " + dealer.getDealerHand().getDeckValue());
    }

    // MODIFIES: this
    // EFFECTS: asks the player if they would like to play again or leave the table, if their balance > 0
    void playAgain() {
        boolean notDone = true;
        String command;

        if (player.getPlayerBalance() > 0) {
            playAgainOptions();
            command = input.next();

            while (notDone) {
                if (command.equals("c")) {
                    keepRunning = false;
                    notDone = false;
                } else if (command.equals("y")) {
                    notDone = false;
                } else {
                    System.out.println("Please make a valid selection...");
                    command = input.next();
                }
            }
        }
    }

    // EFFECTS: display play again options to user
    void playAgainOptions() {
        System.out.println("\nYour Balance: $" + player.getPlayerBalance());
        System.out.println("Would You Like To Play Again?");
        System.out.println("Select:");
        System.out.println("\ty -> yes");
        System.out.println("\tc -> cash out");
    }
}
