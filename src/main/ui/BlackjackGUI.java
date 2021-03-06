package ui;

import exceptions.IllegalHandSizeException;
import model.Card;
import model.Dealer;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// images for GUI playing cards were sourced from http://acbl.mybigcommerce.com/52-playing-cards/

// Blackjack game GUI
public class BlackjackGUI extends JFrame implements ActionListener {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;
    private static final Color TITLE_COLOUR = new Color(0, 0, 0, 255);
    private static final Color TEXT_COLOUR = new Color(0, 0, 0, 255);
    private static final Color BACKGROUND_COLOUR = new Color(29, 101, 31);
    private static final Color BUTTON_COLOUR = new Color(246, 198, 115, 255);
    private static final Color CASH_OUT_BUTTON_COLOUR = new Color(109, 255, 71);
    private static final Font TITLE_FONT = new Font("Century Gothic", Font.PLAIN, 80);
    private static final Font LABEL_FONT = new Font("Century Gothic", Font.PLAIN, 45);
    private static final Font BUTTON_FONT = new Font("Century Gothic", Font.PLAIN, 26);
    private static final Font CASH_OUT_BUTTON_FONT = new Font("Century Gothic", Font.PLAIN, 32);
    private static final FlowLayout CARDS_LAYOUT = new FlowLayout(FlowLayout.LEFT, 5, 2);
    private static final FlowLayout BUTTONS_LAYOUT = new FlowLayout();
    private static final Dimension BUTTON_DIMENSION = new Dimension(140, 36);
    private static final String JSON_STORE = "./data/player.json";
    private static final int INITIAL_BALANCE = 25000;
    private Player player;
    private Dealer dealer;
    private boolean standPlayer = false;
    private boolean doubled = false;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private JPanel panel;
    private JPanel buttons;
    private JPanel betPanel;
    private JTextField betText;


    // MODIFIES: this
    // EFFECTS: initializes the JSON reader, JSON writer, Dealer, and a temporary Player then runs the Blackjack game
    public BlackjackGUI() {
        super("BLACKJACK");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        dealer = new Dealer();
        player = new Player(0); //placeholder for panel to initialize at the start
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
                exitPopUp();
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
            errorPopUp("Could not load Player from file: " + JSON_STORE + "\nPlease restart the app.");
        }
        int result = JOptionPane.showConfirmDialog(this,
                "Would you like to load your saved balance of $" + player.getBalance() + "?"
                        + "\nSelect No to start a new game with $" + INITIAL_BALANCE, "Welcome to Blackjack!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            loadPlayer();
        } else if (result == JOptionPane.NO_OPTION) {
            player = new Player(INITIAL_BALANCE);
        } else {
            selectPlayer();
        }
        initPanel();
    }

    // MODIFIES: this
    // EFFECTS: confirms with user if they would like to cash out;
    //          if yes, then save player and close app; otherwise, do nothing
    private void exitPopUp() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you would like to cash out with $" + player.getBalance() + "?",
                "Cash Out?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            savePlayer();
            System.exit(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads player from file
    private void loadPlayer() {
        try {
            player = jsonReader.read();
            checkBalance();
        } catch (IOException e) {
            errorPopUp("Could not load Player from file: " + JSON_STORE + "\nPlease restart the app.");
        }
    }

    // MODIFIES: this
    // EFFECTS: if saved player has no money, start a new game
    private void checkBalance() {
        if (player.getBalance() == 0) {
            JOptionPane.showConfirmDialog(this,
                    "Your saved Player has no money\nStarting new game...", "Saved Player is Broke",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            player = new Player(INITIAL_BALANCE);
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
            errorPopUp("Could not save Player to file: " + JSON_STORE);
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
        initTitle();
        initPlayerLabels();
        initDealerLabels();
    }

    // MODIFIES: this
    // EFFECTS: initializes the main blackjack title
    private void initTitle() {
        JLabel title = new JLabel("BLACKJACK");
        title.setFont(TITLE_FONT);
        title.setForeground(TITLE_COLOUR);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(100, 20, 1000, 80);

        panel.add(title);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the player JLabels
    private void initPlayerLabels() {
        JLabel yourHand = new JLabel("Your Hand");
        yourHand.setFont(LABEL_FONT);
        yourHand.setForeground(TEXT_COLOUR);
        yourHand.setBounds(100, 400, 300, 60);

        if (player.getHand().length() > 0) {
            JLabel yourValue = new JLabel("Value: " + player.getHand().getDeckValue());
            yourValue.setFont(LABEL_FONT);
            yourValue.setForeground(TEXT_COLOUR);
            yourValue.setBounds(900, 400, 300, 60);
            panel.add(yourValue);
        }

        JLabel yourBalance = new JLabel("Balance: $" + player.getBalance());
        yourBalance.setFont(LABEL_FONT);
        yourBalance.setForeground(TEXT_COLOUR);
        yourBalance.setBounds(225, 800, 750, 60);
        yourBalance.setHorizontalAlignment(JLabel.CENTER);

        panel.add(yourBalance);
        panel.add(yourHand);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the player JLabels
    private void initDealerLabels() {
        JLabel dealerHand = new JLabel("Dealer Hand");
        dealerHand.setFont(LABEL_FONT);
        dealerHand.setForeground(TEXT_COLOUR);
        dealerHand.setBounds(100, 120, 400, 60);

        if (standPlayer) {
            JLabel dealerValue = new JLabel("Value: " + dealer.getHand().getDeckValue());
            dealerValue.setFont(LABEL_FONT);
            dealerValue.setForeground(TEXT_COLOUR);
            dealerValue.setBounds(900, 120, 300, 60);
            panel.add(dealerValue);
        } else if (dealer.getHand().length() == 2) {
            JLabel dealerValue = new JLabel("Value: " + dealer.getHand().getCard(0).getCardValue());
            dealerValue.setFont(LABEL_FONT);
            dealerValue.setForeground(TEXT_COLOUR);
            dealerValue.setBounds(900, 120, 300, 60);
            panel.add(dealerValue);
        }
        panel.add(dealerHand);
    }

    // MODIFIES: this
    // EFFECTS: initializes the playerCards panel and adds it to the main panel
    private void initPlayerCards() {
        JPanel playerCards = new JPanel();
        playerCards.setLayout(CARDS_LAYOUT);
        playerCards.setBounds(100, 460, 1000, 204);
        playerCards.setBackground(BACKGROUND_COLOUR);

        for (Card c : player.getHand().getCards()) {
            playerCards.add(new JLabel(new ImageIcon("./data/cards/"
                    + c.getFaceName() + c.getSuit().charAt(0) + ".png")));
        }

        panel.add(playerCards);
    }

    // MODIFIES: this
    // EFFECTS: initializes the dealerCards panel and adds it to the main panel
    private void initDealerCards() {
        JPanel dealerCards = new JPanel();
        dealerCards.setLayout(CARDS_LAYOUT);
        dealerCards.setBounds(100, 180, 1000, 204);
        dealerCards.setBackground(BACKGROUND_COLOUR);

        if (standPlayer) {
            for (Card c : dealer.getHand().getCards()) {
                dealerCards.add(new JLabel(new ImageIcon("./data/cards/"
                        + c.getFaceName() + c.getSuit().charAt(0) + ".png")));
            }
        } else if (dealer.getHand().length() == 2) {
            Card card = dealer.getHand().getCard(0);
            dealerCards.add(new JLabel(new ImageIcon("./data/cards/"
                    + card.getFaceName() + card.getSuit().charAt(0) + ".png")));
            dealerCards.add(new JLabel(new ImageIcon("./data/cards/cardBack.png")));
        }

        panel.add(dealerCards);
    }

    // MODIFIES: this
    // EFFECTS: initializes all of the buttons, and adds them to the main panel
    private void initButtons() {
        buttons = new JPanel();
        buttons.setLayout(BUTTONS_LAYOUT);
        buttons.setBounds(350, 700, 500, 50);
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
        betPanel.setBounds(300, 750, 600, 50);
        betPanel.setBackground(BACKGROUND_COLOUR);

        initBetText();
        betButton();
        allInButton();

        panel.add(betPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes betText, then adds it to betPanel
    private void initBetText() {
        betText = new JTextField("Enter your bet here...");
        betText.setColumns(11);
        betText.setFont(BUTTON_FONT);
        betText.setForeground(TEXT_COLOUR);
        betText.setBackground(BUTTON_COLOUR);
        betText.setHorizontalAlignment(JTextField.CENTER);
        betText.setToolTipText("Enter your bet here...");
        betText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                betText.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        betPanel.add(betText);
    }

    // MODIFIES: this
    // EFFECTS: initializes the bet button, then adds it to betPanel
    private void betButton() {
        JButton betBut = new JButton("BET");
        betBut.setBackground(BUTTON_COLOUR);
        betBut.setForeground(TEXT_COLOUR);
        betBut.setFont(BUTTON_FONT);
        betBut.setActionCommand("bet");
        betBut.addActionListener(this);
        betBut.setPreferredSize(BUTTON_DIMENSION);
        betBut.setToolTipText("Place your desired bet");
        betPanel.add(betBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes all in button, then adds it to betPanel
    private void allInButton() {
        JButton allInBut = new JButton("ALL IN");
        allInBut.setBackground(BUTTON_COLOUR);
        allInBut.setForeground(TEXT_COLOUR);
        allInBut.setFont(BUTTON_FONT);
        allInBut.setActionCommand("betAll");
        allInBut.addActionListener(this);
        allInBut.setPreferredSize(BUTTON_DIMENSION);
        allInBut.setToolTipText("Bet all of your money");
        betPanel.add(allInBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the hit button and adds it to the buttons panel
    private void hitButton() {
        JButton hitBut = new JButton("HIT");
        hitBut.setBackground(BUTTON_COLOUR);
        hitBut.setForeground(TEXT_COLOUR);
        hitBut.setFont(BUTTON_FONT);
        hitBut.setActionCommand("hit");
        hitBut.addActionListener(this);
        hitBut.setPreferredSize(BUTTON_DIMENSION);
        hitBut.setToolTipText("Draw another card");
        buttons.add(hitBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the stand button and adds it to the buttons panel
    private void standButton() {
        JButton standBut = new JButton("STAND");
        standBut.setBackground(BUTTON_COLOUR);
        standBut.setForeground(TEXT_COLOUR);
        standBut.setFont(BUTTON_FONT);
        standBut.setActionCommand("stand");
        standBut.addActionListener(this);
        standBut.setPreferredSize(BUTTON_DIMENSION);
        standBut.setToolTipText("Face-off against the Dealer");
        buttons.add(standBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the double button and adds it to the buttons panel
    private void doubleButton() {
        JButton doubleBut = new JButton("DOUBLE");
        doubleBut.setBackground(BUTTON_COLOUR);
        doubleBut.setForeground(TEXT_COLOUR);
        doubleBut.setFont(BUTTON_FONT);
        doubleBut.setActionCommand("double");
        doubleBut.addActionListener(this);
        doubleBut.setPreferredSize(BUTTON_DIMENSION);
        doubleBut.setToolTipText("Double your bet and Hit");
        buttons.add(doubleBut);
    }

    // MODIFIES: this
    // EFFECTS: initializes the Cash Out button and adds it to the main panel
    private void cashOutButton() {
        JButton cashOutBut = new JButton("CA$H OUT");
        cashOutBut.setBackground(CASH_OUT_BUTTON_COLOUR);
        cashOutBut.setForeground(TEXT_COLOUR);
        cashOutBut.setFont(CASH_OUT_BUTTON_FONT);
        cashOutBut.setActionCommand("leave");
        cashOutBut.addActionListener(this);
        cashOutBut.setBounds(990, 815, 200, 50);
        cashOutBut.setToolTipText("Save your current balance and exit");
        panel.add(cashOutBut);
    }

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
            exitPopUp();
        } else if (e.getActionCommand().equals("bet")) {
            doBet();
        } else if (e.getActionCommand().equals("betAll")) {
            doAllIn();
        }
    }

    // MODIFIES: this
    // EFFECTS: places bet based off of bet TextField entry and catches NumberFormatException;
    //          if 0 < betAmount <= Player balance, place bet with betAmount;
    //          otherwise, display an error pop up accordingly
    private void doBet() {
        if (player.getBet() == 0) {
            try {
                int betAmount = Integer.parseInt(betText.getText());
                if (betAmount <= player.getBalance() && betAmount > 0) {
                    doPlaceBet(betAmount);
                } else if (betAmount > player.getBalance()) {
                    errorPopUp("Please enter an amount less than your balance");
                } else {
                    errorPopUp("Please enter a valid numerical amount");
                }
            } catch (NumberFormatException e) {
                errorPopUp("Please enter a valid numerical amount");
            }
        } else {
            errorPopUp("You cannot place a bet in the middle of the round");
        }
    }

    // MODIFIES: this
    // EFFECTS: places bet given the bet amount and updates the panel, then checks if player has blackjack
    private void doPlaceBet(int betAmount) {
        try {
            dealer = new Dealer();
            player.placeBet(betAmount);
            player.drawCards();
            dealer.drawCards();
            initPanel();
            checkBlackjack();
        } catch (IllegalHandSizeException e) {
            errorPopUp("Dealer's hand is not empty");
        }
    }

    // MODIFIES: this
    // EFFECTS: if player's hand value is 21 after placing bet and drawing two cards,
    //          then display blackjack pop up
    private void checkBlackjack() {
        if (player.blackjack()) {
            result("BLACKJACK!!!", "BLACKJACK");
        }
    }

    // EFFECTS: display error pop up with given message string
    private void errorPopUp(String message) {
        JOptionPane.showConfirmDialog(this, message, "Uh-Oh...", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: place bet equal to player balance
    private void doAllIn() {
        if (player.getBet() == 0) {
            doPlaceBet(player.getBalance());
        } else {
            errorPopUp("You can't place a bet in the middle of the round");
        }
    }

    // MODIFIES: this
    // EFFECTS: if Player has not yet doubled, draw a card to the Player's hand and
    //          if the Player busts, display bust message and ask to play again;
    //          otherwise, display error pop up
    private void doHit() {
        if (!doubled) {
            if (player.getHand().length() >= 2) {
                player.hit();
                initPanel();

                playerBust();
            } else {
                errorPopUp("You must place a bet before you can hit");
            }
        } else {
            errorPopUp("You can't hit after you Double");
        }
    }

    // MODIFIES: this
    // EFFECTS: if player busted, display pop up message and update balance on panel
    private void playerBust() {
        if (player.bust()) {
            result("YOU BUSTED", "BUST");
        }
    }

    // MODIFIES: this
    // EFFECTS: if round has started, dealer hits if needed, then compare player and dealer hands to determine
    //          the overall outcome of the round;
    //          otherwise, display error pop up telling player to place a bet first
    private void doStand() {
        if (dealer.getHand().length() == 2) {
            standPlayer = true;
            dealer.stand(player);
            initPanel();

            if (dealer.bust()) {
                player.win();
                result("DEALER BUSTED\nYOU WIN!", "DEALER BUSTED");
            } else if (player.push(dealer)) {
                result("IT'S A PUSH", "PUSH");
            } else if (player.stand(dealer)) {
                result("YOU WIN!", "WIN");
            } else {
                result("DEALER WINS!\nYOU LOSE...", "DEALER WON");
            }
        } else {
            errorPopUp("You must place a bet before you can stand");
        }
    }

    // MODIFIES: this
    // EFFECTS: display a pop up message with the result of the round
    private void result(String message, String title) {
        dealer = new Dealer();
        standPlayer = false;
        doubled = false;

        JOptionPane.showConfirmDialog(this, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        initPanel();
        playAgain();
    }

    // MODIFIES: this
    // EFFECTS: if player balance is 0, then ask if player would like to start a new game or leave
    private void playAgain() {
        if (player.getBalance() == 0) {
            int result = JOptionPane.showConfirmDialog(this,
                    "You are officially BROKE!\nWould you like to start a new game with $"
                            + INITIAL_BALANCE + "?",
                    "Cash Out?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                player = new Player(INITIAL_BALANCE);
                initPanel();
            } else if (result == JOptionPane.NO_OPTION) {
                savePlayer();
                System.exit(0);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: double player bet and draw a card to the Player's hand;
    //          if the Player busts, display bust message and ask to play again;
    //          if the Player cannot double and hit, then display pop up message telling them why
    private void doDouble() {
        if (player.getHand().length() == 2 && player.getBalance() >= player.getBet()) {
            doubled = true;
            player.doubleBet();
            initPanel();
            playerBust();
        } else if (player.getHand().length() != 2) {
            errorPopUp("You can only Double when you have two cards in your hand");
        } else {
            errorPopUp("You must have at least $" + player.getBet() + " in order to Double");
        }
    }
}

