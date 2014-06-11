******************************************************
*             BlackJack Game 
* 
* Submitted by: Gopalakrishnan Santhanaraman
********************************************************/

This is a simple BlackJack game written in Java.

-------------------------------------------------------------------------------------------------------
src files in the

Game.java - Main method
GameManager.java
Card.java
Deck.Java
Dealer.java
Player.java

-----------------------------------------------------------------------------------------------------------

Black Jack Game (Rules, Implementations and Assumptions) 

1. The player starts with 100 credits. The game prompts the user to place bets
for each round of the game (atleast 1 credit). 
*** Assumption made: for user input, assumes that the user enters an integer value as input.
The user can continue playing till the credits
are exhausted or can end the game by entering 0 . At the end of the game a summary
of the entire game with some basic statistics is displayed to the user.

2. Overall Game: For each round, once the user places the bets, the cards are dealt for the user and the
dealer. Next, it is the players turn. It checks for BlackJack else uses a private strategy
to either "Hit", "Split" or "Stand".
Next, it is the dealers turn and it follows the standard rules. The dealer checks for black jack, else
the dealer continues to take cards ("hit") until the total is 17 or greater.
Next, the outcome of the round is calculated by comparing the players hand with the dealers 
hand and based on win/loss/push(draw),the players available credits are updated.
In case of a "Split", the player ends up with two hands with a bet for each hand and both the 
hands are compared with the dealer for a win/loss/push(draw) outcome and the players available 
credits are updated

3. Player Strategy
   First it checks for the possibiltiy of a split.(both in terms of matching cards as well as whether
   there are sufficient credits for betting on two hands)

 (a)  HIT - For the "hit", the player uses the following strategy. It calculates an updated total of the value
   of the cards in its hand by choosing a value of 1 or 11 to its benefit in case an Ace is present. else
   the updated total is just the sum total of all value of the cards in its hand with king, queen and jack 
   taking a value of 10.  In addition the player is able keep track of all the cards dealt so far and the
   remaining cards in the deck in a cardCountArray. Using a simple formula, we calculate the probabilty of
   getting a card whose value summed up with the existing total would get us <= 21 (success_probab). and we calculate the
   probability of getting a card that could result in the total going over 21 and hence "go bust".If the success_prob
   is greater than probability of busting, then it performs a "hit" else it performs a "stand". 
   To be noted: the player tries to calculate the above only if the updated total is < 17 similar to dealer strategy.  
   Also as part of hit if the deck is empty, a reshuffle of the deck occurs.

 (b) SPLIT - Due to lack of time it is very basic. 
   Player "Splits" if the cards have the same face value. (both Queen, both Jack, both King, both 5 etc  
  *** Assumption made:  It is allowed to split only once. After a split creating two Hands, 
                        each hand gets to compulsorily hit once and no more.
   It checks for black jack condition for each hand and then performs "stand".
   As part of "split" if the deck is empty , a reshuffle of the deck occurs.
   To perform a "split", it takes into account the requirement of matching cards, availability of credits for two 
   hands as well.
   The decision is based on the the value of the cards it holds as well as the value of the 1 upturned 
   visible card of the dealer. I used the following strategy for the split.
   http://www.wikihow.com/Know-when-to-Split-Pairs-in-Blackjack
   

  (c) STAND - If the dealer does not perform a "hit" or a "split" , it defaults to "stand"

4. Some examples of outcome of each round calculation
    if (player black jack && dealer blackjack -  push/draw    0*bet;
        player black jack && !dealer blackjack - player wins  1.5*bet;
        player !black jack && dealerblackjack  - player loses -1*bet;
        player < dealer and (dealer < 21)     - player loses  -1*bet;
        player > dealer and (player < 21)     - player wins   1*bet;  
        player == dealer and player <21       - push/draw     0*bet

    In case of split some examples
        playerHand1 > dealerHand and playerHand1 < 21 Hand1 wins  1*bet
        playerHand2 < dealerHand and dealerHand < 21  hand2 loses -1*bet

       *** Assumption made:  once the split occurs, and player Hand1 or Hand2 gets a blackjack and dealer !blackj ack
           in this scenario, player Hand wins but it gets only 1*bet, (ie there is no 1.5 bonus for blackjack after split) 

5. A Sample output is shown in BlackJackSampleOutput1.txt              
----------------------------------------------------------------------------------------------------------------------------                                                       
                                                                              