package ui;

import model.Dealer;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// modelled after ca.ubc.cpsc210.bank.ui.TellerApp from https://github.students.cs.ubc.ca/CPSC210/TellerApp

// Blackjack Game
public class Blackjack {
    private static final String JSON_STORE = "./data/player.json";
    private static final int INITIAL_BALANCE = 25000;
    private Player player;
    private Dealer dealer;
    private Scanner input;
    private boolean keepRunning = true;
    private int betAmount;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: initializes the JSON reader and writer, then runs the Blackjack game
    public Blackjack() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runBlackjack();
    }


    // MODIFIES: this
    // EFFECTS: runs the game
    private void runBlackjack() {
        input = new Scanner(System.in);

        initialize();

        while (keepRunning) {
            if (player.getBalance() == 0) {
                askNewGame();
            } else {
                placeBet();
                playGame();
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: if player has blackjack, then update player accordingly and ask to play again;
    //          otherwise, display options to the player
    private void playGame() {
        String command;

        if (player.blackjack()) {
            System.out.println("\nBLACKJACK!!!");
            playAgain();
        } else {
            displayOptions();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("l")) {
                savePlayer();
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: ask player if they would like to play again or not
    private void askNewGame() {
        boolean notDone = true;
        String command;

        newGameOptions();
        command = input.next();
        command = command.toLowerCase();

        while (notDone) {
            if (command.equals("y")) {
                player = new Player(INITIAL_BALANCE);
                notDone = false;
            } else if (command.equals("n")) {
                savePlayer();
                notDone = false;
            } else {
                System.out.println("Please make a valid selection...");
                command = input.next();
            }
        }
    }

    // EFFECTS: display new game options
    private void newGameOptions() {
        System.out.println("\nYou are officially BROKE!");
        System.out.println("Would you like to start a new game?");
        System.out.println("y -> yes");
        System.out.println("n -> no");
    }

    // MODIFIES: this
    // EFFECTS: display welcome message and initialize the Player with user input
    private void initialize() {
        System.out.println("Welcome to Blackjack!");
        selectPlayer();
    }

    // MODIFIES: this
    // EFFECTS: user selects whether to load old balance or start the game as a new player
    private void selectPlayer() {
        boolean notDone = true;
        String command;

        selectPlayerOptions();
        command = input.next();
        command = command.toLowerCase();

        while (notDone) {
            if (command.equals("l")) {
                loadPlayer();
                playerIsBroke();
                notDone = false;
            } else if (command.equals("n")) {
                player = new Player(INITIAL_BALANCE);
                notDone = false;
            } else {
                System.out.println("Please make a valid selection...");
                command = input.next();
                command = command.toLowerCase();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if saved player has 0 balance then start a new game
    private void playerIsBroke() {
        if (player.getBalance() == 0) {
            System.out.println("\nYour saved Player has no money");
            System.out.println("Starting new game...");
            player = new Player(INITIAL_BALANCE);
        }
    }

    // MODIFIES: this
    // EFFECTS: display options to either load saved game or start new game with respective balances
    private void selectPlayerOptions() {
        try {
            player = jsonReader.read();
        } catch (IOException e) {
            System.out.println("There is no saved game");
        }
        System.out.println("\nWhat would you like to do:\n");
        System.out.println("l -> load saved game with balance of $" + player.getBalance());
        System.out.println("n -> start new game with balance of $" + INITIAL_BALANCE);
    }

    // MODIFIES: this
    // EFFECTS: loads player from file
    private void loadPlayer() {
        try {
            player = jsonReader.read();
            System.out.println("\nLoaded Player");
        } catch (IOException e) {
            System.out.println("\nUnable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: user places a bet and draws two cards
    private void placeBet() {
        dealer = new Dealer();
        boolean notDone = true;

        System.out.println("\nYour Balance: $" + player.getBalance());
        System.out.println("How much would you like to bet?");
        int command = enterBet();

        while (notDone) {
            if (command <= player.getBalance() && command > 0) {
                player.placeBet(command);
                player.drawCards();
                dealer.drawCards();
                notDone = false;
            } else {
                System.out.println("Please enter an amount less than your balance...");
                command = enterBet();
            }
        }
        printHands();
    }

    // MODIFIES: this
    // EFFECTS: takes user input of bet amount;
    //          catches NumberFormatException if user input is not an integer or too large
    private int enterBet() {
        try {
            betAmount = Integer.parseInt(input.next());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numerical amount...");
            enterBet();
        }
        return betAmount;
    }

    // EFFECTS: print the player and dealer hands (dealer has hidden card)
    private void printHands() {
        System.out.println("\nYour Hand:\n" + player.getHand().printDeck());
        System.out.println("Your Hand Value: " + player.getHand().getDeckValue());
        System.out.println("\n\nDealer's Hand:\n" + dealer.showFirstCard() + "\n[HIDDEN CARD]");
    }

    // EFFECTS: displays options to Player
    private void displayOptions() {
        System.out.println("\nYour Balance: $" + player.getBalance());
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
        System.out.println("\nYour Balance: $" + player.getBalance());
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
    // EFFECTS: dealer hits if needed, then compare player and dealer hands to determine the overall
    //          outcome of the round
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
    private void doDealerHit() {
        if (dealer.getHand().getDeckValue() < 17) {
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
    private void showHands() {
        System.out.println("Your Hand:\n" + player.getHand().printDeck());
        System.out.println("Your Hand Value: " + player.getHand().getDeckValue());
        System.out.println("\n\nDealer's Hand:\n" + dealer.getHand().printDeck());
        System.out.println("Dealer Hand Value: " + dealer.getHand().getDeckValue());
    }

    // MODIFIES: this
    // EFFECTS: asks the player if they would like to play again or leave the table, if their balance > 0
    private void playAgain() {
        boolean notDone = true;
        String command;

        if (player.getBalance() > 0) {
            playAgainOptions();
            command = input.next();

            while (notDone) {
                if (command.equals("c")) {
                    savePlayer();
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
    private void playAgainOptions() {
        System.out.println("\nYour Balance: $" + player.getBalance());
        System.out.println("Would You Like To Play Again?");
        System.out.println("Select:");
        System.out.println("\ty -> yes");
        System.out.println("\tc -> cash out");
    }

    // MODIFIES: this
    // EFFECTS: saves player to file and sets keepRunning to false
    private void savePlayer() {
        try {
            jsonWriter.open();
            jsonWriter.write(player);
            jsonWriter.close();
            System.out.println("\nSaved Player");
            keepRunning = false;
        } catch (FileNotFoundException e) {
            System.out.println("\nUnable to write to file: " + JSON_STORE);
        }
    }
}
