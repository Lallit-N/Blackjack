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
    private static final Color TEXT_COLOUR = new Color(0, 0, 0);
    private static final Color BACKGROUND_COLOUR = new Color(29, 101, 31);
    private static final Color BUTTON_COLOUR = new Color(246, 198, 115, 255);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 42);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 26);
    private static final Font CASH_OUT_BUTTON_FONT = new Font("Arial", Font.PLAIN, 30);
    private static final FlowLayout CARDS_LAYOUT = new FlowLayout();
    private static final FlowLayout BUTTONS_LAYOUT = new FlowLayout();
    private static final Dimension BUTTON_DIMENSION = new Dimension(140, 36);
    private static final String JSON_STORE = "./data/player.json";
    private static final int INITIAL_BALANCE = 25000;
    private Player player;
    private Dealer dealer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel panel;
    private JPanel buttons;
    private JPanel betPanel;
    private JPanel playerCards;
    private JPanel dealerCards;
    private JButton hitBut;
    private JButton standBut;
    private JButton doubleBut;
    private JButton cashOutBut;
    private JButton betBut;
    private JButton allInBut;


    // MODIFIES: this
    // EFFECTS: initializes the JSON reader and writer and the dealer, then runs the Blackjack game
    public BlackjackGUI() {
        super("BLACKJACK");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        dealer = new Dealer();
        runBlackJack();
    }

    // MODIFIES: this
    // EFFECTS: initializes the GUI in order for the game to run
    private void runBlackJack() {
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitMessage();
            }
        });

        selectPlayer();

        initPanel();

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

        initLabels();

        initButtons();
        initBet();

        initPlayerCards();
        initDealerCards();

        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: calls the methods that initialize all of the JLabels
    private void initLabels() {
        playerLabels();
        dealerLabels();
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the player JLabels
    private void playerLabels() {
        JLabel yourHand = new JLabel("Your Hand");
        yourHand.setFont(TEXT_FONT);
        yourHand.setForeground(TEXT_COLOUR);
        yourHand.setBounds(100, 310, 300, 60);

        JLabel yourValue = new JLabel("Value: " + player.getHand().getDeckValue());
        yourValue.setFont(TEXT_FONT);
        yourValue.setForeground(TEXT_COLOUR);
        yourValue.setBounds(900, 310, 300, 60);

        JLabel yourBalance = new JLabel("Balance: $" + player.getBalance());
        yourBalance.setFont(TEXT_FONT);
        yourBalance.setForeground(TEXT_COLOUR);
        yourBalance.setBounds(225, 700, 750, 60);
        yourBalance.setHorizontalAlignment(JLabel.CENTER);

        panel.add(yourBalance);
        panel.add(yourHand);
        panel.add(yourValue);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the player JLabels
    private void dealerLabels() {
        JLabel dealerHand = new JLabel("Dealer Hand");
        dealerHand.setFont(TEXT_FONT);
        dealerHand.setForeground(TEXT_COLOUR);
        dealerHand.setBounds(100, 30, 400, 60);

        JLabel dealerValue = new JLabel("Value: " + dealer.getHand().getDeckValue());
        dealerValue.setFont(TEXT_FONT);
        dealerValue.setForeground(TEXT_COLOUR);
        dealerValue.setBounds(900, 30, 300, 60);

        panel.add(dealerHand);
        panel.add(dealerValue);
    }

    // MODIFIES: this
    // EFFECTS: initializes the playerCards panel and adds it to the main panel
    private void initPlayerCards() {
        playerCards = new JPanel();
        playerCards.setLayout(CARDS_LAYOUT);
        playerCards.setBounds(100, 360, 1000, 200);
        playerCards.setBackground(BUTTON_COLOUR);

        ImageIcon image = new ImageIcon("./data/cards/cardBacksmall.png");
        JLabel picture = new JLabel(image);
        JLabel picture2 = new JLabel(image);

        playerCards.add(picture);
        playerCards.add(picture2);
        panel.add(playerCards);
    }

    // MODIFIES: this
    // EFFECTS: initializes the dealerCards panel and adds it to the main panel
    private void initDealerCards() {
        dealerCards = new JPanel();
        dealerCards.setLayout(CARDS_LAYOUT);
        dealerCards.setBounds(100, 80, 1000, 200);
        dealerCards.setBackground(BUTTON_COLOUR);

        ImageIcon image = new ImageIcon("./data/cards/cardBacksmall.png");
        JLabel picture = new JLabel(image);
        JLabel picture2 = new JLabel(image);

        dealerCards.add(picture);
        dealerCards.add(picture2);
        panel.add(dealerCards);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the buttons, and adds them to the main panel
    private void initButtons() {
        buttons = new JPanel();
        buttons.setLayout(BUTTONS_LAYOUT);
        buttons.setBounds(350, 600, 500, 50);
        buttons.setBackground(BACKGROUND_COLOUR);
        hitButton();
        doubleButton();
        standButton();
        cashOutButton();

        panel.add(buttons);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the bet related buttons and the bet TextField, then adds them to the main panel
    private void initBet() {
        betPanel = new JPanel();
        betPanel.setLayout(BUTTONS_LAYOUT);
        betPanel.setBounds(300, 650, 600, 50);
        betPanel.setBackground(BACKGROUND_COLOUR);

        initBetText();
        betButton();
        allInButton();

        panel.add(betPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes betText, then adds it to betPanel
    private void initBetText() {
        JTextField betText = new JTextField("Enter your bet here...");
        betText.setColumns(11);
        betText.setFont(BUTTON_FONT);
        betText.setForeground(TEXT_COLOUR);
        betPanel.add(betText);
    }

    // MODIFIES: this
    // EFFECTS: initializes the bet button, then adds it to betPanel
    private void betButton() {
        betBut = new JButton("BET");
        betBut.setBackground(BUTTON_COLOUR);
        betBut.setForeground(TEXT_COLOUR);
        betBut.setFont(BUTTON_FONT);
        betBut.setActionCommand("bet");
        betBut.addActionListener(this);
        betBut.setPreferredSize(BUTTON_DIMENSION);
        betPanel.add(betBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes allInText, then adds it to betPanel
    private void allInButton() {
        allInBut = new JButton("ALL IN");
        allInBut.setBackground(BUTTON_COLOUR);
        allInBut.setForeground(TEXT_COLOUR);
        allInBut.setFont(BUTTON_FONT);
        allInBut.setActionCommand("betAll");
        allInBut.addActionListener(this);
        allInBut.setPreferredSize(BUTTON_DIMENSION);
        betPanel.add(allInBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the hit button and adds it to the buttons panel
    private void hitButton() {
        hitBut = new JButton("HIT");
        hitBut.setBackground(BUTTON_COLOUR);
        hitBut.setForeground(TEXT_COLOUR);
        hitBut.setFont(BUTTON_FONT);
        hitBut.setActionCommand("hit");
        hitBut.addActionListener(this);
        hitBut.setPreferredSize(BUTTON_DIMENSION);
        buttons.add(hitBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the stand button and adds it to the buttons panel
    private void standButton() {
        standBut = new JButton("STAND");
        standBut.setBackground(BUTTON_COLOUR);
        standBut.setForeground(TEXT_COLOUR);
        standBut.setFont(BUTTON_FONT);
        standBut.setActionCommand("stand");
        standBut.addActionListener(this);
        standBut.setPreferredSize(BUTTON_DIMENSION);
        buttons.add(standBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the double button and adds it to the buttons panel
    private void doubleButton() {
        doubleBut = new JButton("DOUBLE");
        doubleBut.setBackground(BUTTON_COLOUR);
        doubleBut.setForeground(TEXT_COLOUR);
        doubleBut.setFont(BUTTON_FONT);
        doubleBut.setActionCommand("double");
        doubleBut.addActionListener(this);
        doubleBut.setPreferredSize(BUTTON_DIMENSION);
        buttons.add(doubleBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the Cash Out button and adds it to the main panel
    private void cashOutButton() {
        Color cashOut = new Color(109, 255, 71);

        cashOutBut = new JButton("CA$H OUT");
        cashOutBut.setBackground(cashOut);
        cashOutBut.setForeground(TEXT_COLOUR);
        cashOutBut.setFont(CASH_OUT_BUTTON_FONT);
        cashOutBut.setActionCommand("leave");
        cashOutBut.addActionListener(this);
        cashOutBut.setBounds(990, 715, 200, 50);
        panel.add(cashOutBut);
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
        } else if (e.getActionCommand().equals("leave")) {
            exitMessage();
        } else if (e.getActionCommand().equals("bet")) {
            //placeBet();
        } else if (e.getActionCommand().equals("betAll")) {
            //placeBet();
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

