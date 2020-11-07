package ui;

import model.Dealer;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// Blackjack game GUI
public class BlackjackGUI extends JFrame implements ActionListener {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final Color BACKGROUND_COLOUR = new Color(29, 101, 31);
    private static final Color BUTTON_COLOUR = new Color(255, 190, 25);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 30);
    private static final FlowLayout CARDS_LAYOUT = new FlowLayout();
    private static final FlowLayout BUTTONS_LAYOUT = new FlowLayout();
    private static final String JSON_STORE = "./data/player.json";
    private static final int INITIAL_BALANCE = 25000;
    private Player player;
    private Dealer dealer;
    private int betAmount;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel panel;
    private JPanel buttons;
    private JPanel playerCards;
    private JPanel dealerCards;
    private JButton hitBut;
    private JButton standBut;
    private JButton doubleBut;


    // MODIFIES: this
    // EFFECTS: initializes the JSON reader and writer, then runs the Blackjack game
    public BlackjackGUI() {
        super("BLACKJACK");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runBlackJack();
    }

    // MODIFIES: this
    // EFFECTS: initializes the GUI in order for the game to run
    private void runBlackJack() {
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitMessage();
            }
        });

        initPanel();

        selectPlayer();

    }

    // MODIFIES: this
    // EFFECTS: displays a pop up window to let user decide to load game or start a new game
    private void selectPlayer() {
        try {
            player = jsonReader.read();
        } catch (IOException e) {
            System.out.println("There is no saved game");
        }
        int result = JOptionPane.showConfirmDialog(this,
                "Would you like to load your saved balance of $" + player.getBalance() + "?"
                        + "\nSelect No to start a new game with $" + INITIAL_BALANCE, "Welcome to Blackjack!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            //loadPlayer();
            System.out.println("yes");
        } else if (result == JOptionPane.NO_OPTION) {
            //player = new Player(INITIAL_BALANCE);
            System.out.println("no");
        } else {
            selectPlayer();
        }
    }

    private void exitMessage() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you would like to cash out with $" + player.getBalance() + "?",
                "Cash Out?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            //savePlayer();
            System.out.println("yes");
            System.exit(0);
        } else if (result == JOptionPane.NO_OPTION) {
            //do nothing
            System.out.println("no");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads player from file
    private void loadPlayer() {
        try {
            player = jsonReader.read();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(this,
                    "Could not load Player from file: " + JSON_STORE + "\nPlease restart the app.",
                    "ERROR!!!", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves player to file
    private void savePlayer() {
        try {
            jsonWriter.open();
            jsonWriter.write(player);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showConfirmDialog(this,
                    "Could not save Player to file: " + JSON_STORE, "ERROR!!!",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the main panel on which all the buttons and cards are displayed
    private void initPanel() {
        panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOUR);
        this.setContentPane(panel);
        this.setLayout(null);

        initButtons();

        playerCards = new JPanel();
        playerCards.setLayout(CARDS_LAYOUT);
        playerCards.setBounds(100, 80, 1000, 200);
        playerCards.setBackground(BUTTON_COLOUR);

        ImageIcon image = new ImageIcon("./data/cards/cardBacksmall.png");
        JLabel picture = new JLabel(image);
        JLabel picture2 = new JLabel(image);

        playerCards.add(picture);
        playerCards.add(picture2);
        panel.add(playerCards);

        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the panel which contains all of the buttons, and adds it to the main panel
    private void initButtons() {
        buttons = new JPanel();
        buttons.setLayout(BUTTONS_LAYOUT);
        buttons.setBounds(200, 600, 800, 50);
        buttons.setBackground(BACKGROUND_COLOUR);
        hitButton();
        standButton();
        doubleButton();


        panel.add(buttons);

    }

    // MODIFIES: this
    // EFFECTS: initializes the hit button and adds it to the buttons panel
    private void hitButton() {
        hitBut = new JButton("HIT");
        hitBut.setBackground(BUTTON_COLOUR);
        hitBut.setFont(BUTTON_FONT);
        hitBut.setActionCommand("hit");
        hitBut.addActionListener(this);
        buttons.add(hitBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the stand button and adds it to the buttons panel
    private void standButton() {
        standBut = new JButton("STAND");
        standBut.setBackground(BUTTON_COLOUR);
        standBut.setFont(BUTTON_FONT);
        standBut.setActionCommand("stand");
        standBut.addActionListener(this);
        buttons.add(standBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the double button and adds it to the buttons panel
    private void doubleButton() {
        doubleBut = new JButton("DOUBLE");
        doubleBut.setBackground(BUTTON_COLOUR);
        doubleBut.setFont(BUTTON_FONT);
        doubleBut.setActionCommand("double");
        doubleBut.addActionListener(this);
        buttons.add(doubleBut);
    }

//    private void displayCards() {
//        Deck deck = new Deck();
//        deck.playingDeck();
//
//        for (String suit : deck.getSuits()) {
//            for (String fn : deck.getFaceNames()) {
//                panel.add(new JLabel(new ImageIcon("./data/cards/" + fn + suit.charAt(0) + ".png")));
//            }
//        }
//    }

    // MODIFIES: this
    // EFFECTS: performs the action corresponding to the specific action command
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("hit")) {
            doHit();
        } else if (e.getActionCommand().equals("double")) {
            doDouble();
        } else if (e.getActionCommand().equals("stand")) {
            doStand();
        }
    }

    // MODIFIES: this
    // EFFECTS: draw a card to the Player's hand;
    //          if the Player busts, display bust message and ask to play again
    private void doHit() {
        System.out.println("hit");
    }

    // MODIFIES: this
    // EFFECTS: dealer hits if needed, then compare player and dealer hands to determine the overall
    //          outcome of the round
    private void doStand() {
        System.out.println("stand");
    }

    // MODIFIES: this
    // EFFECTS: double player bet and draw a card to the Player's hand;
    //          if the Player busts, display bust message and ask to play again
    private void doDouble() {
        System.out.println("double");
    }

}

