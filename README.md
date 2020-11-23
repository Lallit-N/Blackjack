# Blackjack

## Proposal

I am going to attempt to create a simple **Blackjack** application which will function very similar to playing Blackjack
at a casino. There will be a Player who will be able to buy in to the game and play against the Dealer. The Player can
place a bet before both the Player and the Dealer have been dealt their cards and choose to either hit, stand, or
double their bet and hit. If the total value of the Player's hand is greater than the Dealer's hand, then the Player 
wins double their bet. If the Player has been dealt a hand
with total value of 21, then that hand will be declared a *Blackjack*, and the Player will receive 2.5 times their
initial bet. In the case where the Player's hand has a smaller value than the Dealer's hand, the Player will lose the
money that they have bet on their hand. This game will occur in a cycle until either the Player chooses to leave and
cash out, or the Player goes bankrupt.\
I want to make this app for anyone and everyone to use as a source of entertainment. I am interested in this project
because I like to play Blackjack for fun with my friends online, so I thought it would be fascinating to try 
to create the game myself as my first personal project in Java.


## User Stories

- As a user, I want to be able to buy in to the game
- As a user, I want to be able to place a bet and receive two cards
- As a user, I want to be able to add another card to my hand (add X's to Y)
- As a user, I want to be able to choose whether to **hit**, **stand**, or **double**
- As a user, I want to be able to cash out from the game (save my total balance)
- As a user, when I start the app, I want to choose to either load my last player balance or start as a new player


## Phase 4: Task 2
I chose to implement:
- Test and design a class in your model package that is robust.  You must have at least one method that throws a 
checked exception.  You must have one test for the case where the exception is expected and another where the exception 
is not expected.

The robust class is the Dealer class. The method that throws the checked exception is the drawCards() method.


## Phase 4: Task 3
In my UML class diagram, I chose to use an aggregation from the Deck class to the Card class because the cards are a
part of the Deck (in other words, you can't have a deck of cards without cards).\
If I had more time to work on this project, I would:
- reduce the duplication between Dealer and Player by creating a superclass.
- make Deck an abstract class so that Player and Dealer extend it because most of the methods in Dealer and Player make
changes to their respective Deck objects.


