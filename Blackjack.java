import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Project: Blackjack 
 * Date:    4/26/17 
 * Author:  Roland Holban 
 * Brief:   Single player blackjack game. 
 *          Blackjack Rules: 
 *          http://www.pagat.com/banking/blackjack.html
 */

public class Blackjack extends Application {

    // Declare a shoe
    private static Shoe shoe;

    // Declare a hand for the dealer and player
    private Hand playerHand;
    private Hand dealerHand;

    // Declare a bankroll and a bet
    private double bankroll;
    private double bet;

    // Buttons for Hit/Stay/Double/Split
    private Button btHit = new Button("Hit");
    private Button btStay = new Button("Stay");
    private Button btDouble = new Button("Double");
    private Button btSplit = new Button("Split");

    // HBoxes for displaying dealer/player cards
    private HBox dealer = new HBox(5);
    private HBox player = new HBox(5);

    // Holds dealers face down card image
    private String faceDownCard;

    // Text fields for money operations
    private TextField tfBet = new TextField();
    private TextField tfBankroll = new TextField();

    // Labels for displaying total card counts
    private Label playerCardCount = new Label();
    private Label dealerCardCount = new Label();

    @Override
    public void start(Stage primaryStage) {
        // Display welcome screen
        welcomeScreen(primaryStage);
        tfBankroll.setDisable(true);

        // Create the game board and display it
        Scene mainScene = new Scene(displayGameTable());
        mainScene.getStylesheets().add("gameScene.css");
        primaryStage.setScene(mainScene);

        // Bet text field event handler.
        // Start a new round each time user presses ENTER
        tfBet.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {                
                try {
                    // If the player has enough money for the bet
                    if (Double.parseDouble(tfBet.getText()) <= bankroll) {
                        // Initialize the new bet
                        bet = Double.parseDouble(tfBet.getText());
                        btHit.setDisable(false);
                        btStay.setDisable(false);
                        tfBet.setDisable(true);
                        // Start the blackjack round
                        startRound();
                    } else {
                        tfBet.setText("Not enough money.");
                    }
                } 
                // Make sure user input is only numbers
                catch (NumberFormatException ex) {
                    tfBet.setText("Numbers only.");
                }
            }
        });

        // Hit button event handler. Draws a new card
        btHit.setOnAction(e -> playerDrawCard());

        // Stay button event handler. 
        // Locks in player's cards, reveals dealers face down card,
        // then dealer draws until total >= 17
        btStay.setOnAction(e -> {
            disableButtons();
            revealFaceDownCard();
            while (dealerHand.getValue() < 17) {
                dealerDrawCard(true);
            }
            endRound();
        });

        // Double button event handler. 
        // Doubles the player's bet and draws one more final card
        btDouble.setOnAction(e -> {
            // Adjust the bet and the bankroll
            updateBankroll(1, false);
            bet *= 2;
            tfBet.setText(Double.toString(bet));
            // Draw one final card then ends turn
            playerDrawCard();
            btStay.fire();
        });
    }

    // Welcome screen GUI and logic.
    // Parameter is only for displaying primaryStage at the end
    private void welcomeScreen(Stage primaryStage) {
        // Welcome screen GUI
        TextField tfStartingBankroll = new TextField();
        TextField tfNumDecks = new TextField();
        Button btStart = new Button("Play");
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.getChildren().addAll(
                new Label("Starting bankroll:"),
                tfStartingBankroll,
                new Label("Number of decks:"),
                tfNumDecks, btStart);
        Scene welcomeScene = new Scene(pane);
        welcomeScene.getStylesheets().add("otherScene.css");
        Stage welcomeStage = new Stage();
        welcomeStage.setScene(welcomeScene);
        welcomeStage.show();

        // Start button event handler. 
        // Gets the starting bankroll and number of decks
        btStart.setOnAction(e -> {
            try {
                // Only allow a max of 6 decks, min 1 deck
                if (Integer.parseInt(tfNumDecks.getText()) < 1) {
                    tfNumDecks.setText("Minimum 1 deck.");
                } else if (Integer.parseInt(tfNumDecks.getText()) > 6) {
                    tfNumDecks.setText("Maximum 6 decks.");
                } else {
                    // Initialize the shoe and bankroll
                    shoe = new Shoe(Integer.parseInt(tfNumDecks.getText()));
                    bankroll = Integer.parseInt(tfStartingBankroll.getText());
                    shoe.shuffle();
                    tfBankroll.setText(Double.toString(bankroll));
                    welcomeStage.close();
                    disableButtons();
                    primaryStage.show();
                }
            } 
            // If input not numeric catch exception
            catch (NumberFormatException ex) {
                tfNumDecks.setText("Numbers only.");
                tfStartingBankroll.setText("Numbers only.");
            }
        });
    }

    // GUI for game board
    private StackPane displayGameTable() {
        // Set the game board pane settings
        player.setPadding(new Insets(5, 5, 5, 5));
        player.setMinHeight(150);
        player.setMinWidth(800);
        player.setAlignment(Pos.CENTER);
        dealer.setPadding(new Insets(5, 5, 5, 5));
        dealer.setMinHeight(150);
        dealer.setMinWidth(800);
        dealer.setAlignment(Pos.CENTER);

        // HBox for holding the buttons
        HBox buttonPane = new HBox(
                15, btHit, btStay, btDouble, btSplit);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(5, 5, 5, 5));

        // HBox for holding bankroll/bet text fields
        HBox moneyPane = new HBox(5,
                new Label("Bankroll: "), tfBankroll,
                new Label("   Bet: "), tfBet);
        moneyPane.setAlignment(Pos.CENTER);
        moneyPane.setPadding(new Insets(10, 5, 0, 5));

        // Main pane
        VBox pane = new VBox(10, dealerCardCount, dealer,
                new ImageView("images/dealerPays.png"),
                playerCardCount, player, moneyPane, buttonPane);
        pane.setPadding(new Insets(5, 10, 5, 10));
        pane.setAlignment(Pos.CENTER);
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(pane);

        // Return the main pane
        return mainPane;
    }

    // Deals the first hand of the round
    private void startRound() {
        // Clear the game board
        player.getChildren().clear();
        dealer.getChildren().clear();
        dealerCardCount.setText("");
        playerCardCount.setText("");

        // Initialize new hands
        playerHand = new Hand();
        dealerHand = new Hand();

        // Initialize a new shoe if there aren't enough 
        // cards remaining for the round
        if (shoe.shoeStack.size() <= 14) {
            shoe = new Shoe(shoe.numDecks);
            shoe.shuffle();
        }

        // Subtract bet amount from players bankroll
        updateBankroll(1, false);

        // Deal the first round of cards. Dealer's second card is face down
        playerDrawCard();
        dealerDrawCard(false);
        playerDrawCard();
        faceDownCard = shoe.peek();
        dealerHand.hand.add(shoe.dealCard());
        dealer.getChildren().add(new ImageView("images/r.png"));
        

        // If player has a pair, enable the split button
        if (playerHand.hand.get(0).getValue() == playerHand.hand.get(1).getValue()) {
            btSplit.setDisable(false);
        }

        // If player has enough for doubling down, enable double button
        if (bet * 2 <= bankroll) {
            btDouble.setDisable(false);
        }

        // If dealer has an ace showing and player has blackjack,
        // offer even money
        if (dealerHand.hand.get(0).getValue() == 1 && playerHand.getValue() == 21) {
            evenMoney();
        } // Else if dealer has an ace showing and player doesnt have blackjack,
        // offer insurance
        else if (dealerHand.hand.get(0).getValue() == 1) {
            insurance();
        } // Else if either dealer or player has a blackjack, end the round
        else if (dealerHand.getValue() == 21 || playerHand.getValue() == 21) {
            revealFaceDownCard();
            endRound();
        }
    }

    // Ends the round and pays player if they won
    private void endRound() {
        disableButtons();
        revealFaceDownCard();
        // If player didn't bust (universal win condition)
        if (playerHand.getValue() <= 21) {
            // If dealer busted
            if (dealerHand.getValue() > 21) {
                updateBankroll(2, true);
            } // Else if player has blackjack and dealer doesn't, pay 1.5x bet
            else if (playerHand.getValue() == 21
                    && playerHand.getHandSize() == 2 && dealerHand.getValue() < 21) {
                updateBankroll(2.5, true);
            } // Else if player has general better hand
            else if (playerHand.getValue() > dealerHand.getValue()) {
                updateBankroll(2, true);
            } // Else if they have equal hands
            else if (playerHand.getValue() == dealerHand.getValue()) {
                updateBankroll(1, true);
            }
        }
        // Enable a new bet
        tfBet.setDisable(false);
    }

    // Disables the game buttons
    private void disableButtons() {
        btHit.setDisable(true);
        btStay.setDisable(true);
        btSplit.setDisable(true);
        btDouble.setDisable(true);
    }

    // Increases/decreases bankroll based on parameters
    private void updateBankroll(double betMultiplier, boolean add) {
        if (add) {
            bankroll += bet * betMultiplier;
        } else {
            bankroll -= bet * betMultiplier;
        }
        tfBankroll.setText(Double.toString(bankroll));
    }

    // Draw a card for the player
    private void playerDrawCard() {
        player.getChildren().add(
                new ImageView(((Card)shoe.shoeStack.peek()).getImage()));
        playerHand.hand.add(shoe.dealCard());        
        playerCardCount.setText(Integer.toString(playerHand.getValue()));
        // If new count exceeds 21, end the round. Player busted
        if (playerHand.getValue() > 21) {
            endRound();
        }
    }

    // Draw a card for the dealer. If parameter true, also updates count
    private void dealerDrawCard(boolean updateCount) {
        if (updateCount) {
            dealer.getChildren().add(new ImageView(shoe.peek()));
            dealerHand.hand.add(shoe.dealCard());
            dealerCardCount.setText(Integer.toString(dealerHand.getValue()));
        } else {
            dealer.getChildren().add(new ImageView(shoe.peek()));
            dealerHand.hand.add(shoe.dealCard());
        }
    }

    // Reveals face down card and displays initial count
    private void revealFaceDownCard() {
        dealer.getChildren().remove(1);
        dealer.getChildren().add(new ImageView(faceDownCard));
        dealerCardCount.setText(Integer.toString(dealerHand.getValue()));
    }

    // Offers insurance
    private void insurance() {
        // Create insurance GUI
        Button btYes = new Button("Yes");
        Button btNo = new Button("No");
        HBox hBox = new HBox(15, btYes, btNo);
        VBox vBox = new VBox(15,
                new Label("Insurance?\nCost: " + bet / 2), hBox);
        vBox.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("otherScene.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Yes button event handler.
        // Pays player and ends round if dealer has blackjack
        btYes.setOnAction(e -> {
            updateBankroll(0.5, false);
            if (dealerHand.getValue() == 21) {
                updateBankroll(1.5, true);
                stage.close();
                endRound();
            }
            stage.close();
        });

        // No button event handler. Ends round if dealer has blackjack
        btNo.setOnAction(e -> {
            if (dealerHand.getValue() == 21) {
                stage.close();
                endRound();
            }
            stage.close();
        });
    }

    // Offers even money
    private void evenMoney() {
        // Create even money GUI
        Button btYes = new Button("Yes");
        Button btNo = new Button("No");
        HBox hBox = new HBox(15, btYes, btNo);
        VBox vBox = new VBox(15, new Label("Even money?"), hBox);
        vBox.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("otherScene.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Yes button event handler.
        // Pays player even money and reveals dealer's face down 
        btYes.setOnAction(e -> {
            updateBankroll(2, true);
            revealFaceDownCard();
            disableButtons();
            tfBet.setDisable(false);
            stage.close();
        });

        // No button event handler. Ends round if player refuses even money
        btNo.setOnAction(e -> {
            stage.close();
            endRound();
        });
    }

    // Main method
    public static void main(String[] args) {
        launch();

    }

}
