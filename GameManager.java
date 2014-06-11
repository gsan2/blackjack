package blackjackgame;
import java.io.*;

/**
 * 
 * @author gopal
 *
 */

public class GameManager {

	private int numRoundsPlayed; /*Number of rounds played in the game*/
	private int numRoundsPlayerWon; /* Number of wins for the player */
	private int numCardsLeft; /* Number of cards left in the deck */
	private int numChipsLeft; /* Number of credits available to the player */
	private int totalNumCards; /* Total Number of cards in the shoe */ 
	private Deck deck;
	private Player player;
	private Dealer dealer;
	
	/*GameManager constructor */
	public GameManager(int numDecks, int numChipsLeft){
		this.numChipsLeft = numChipsLeft;
		numCardsLeft = totalNumCards = numDecks*52;		
		numRoundsPlayed = 0;
		numRoundsPlayerWon = 0;
	}
	
	public void initialize(){
		
		deck = Deck.getInstance();
		deck.init(numCardsLeft);
		dealer = Dealer.getInstance();
		dealer.init();
		player = Player.getInstance();
		player.init();
		
	}
	
	/**
	 *  This is the main routine in the game. It displays player's available credits and prompts 
	 * the user to choose amount of credits to "bet" for each round. In each round, first the 
	 * player's hand is dealt and then the dealer's hand. dealervisibleTotal is the dealers face up card of the dealers hand.
	 * During the players turn, the player uses a private strategy to "hit" or "split" or "stand". In
	 * the dealers turn, the dealer follows the standard rule:  In this case, the dealer must continue
	 *  to take cards ("hit") until the total is 17 or greater. Next the outcome of the round is 
	 *  calculated by comparing the players hand with the dealers hand and based on win/loss/push(draw),
	 * the players available credits are updated. In case of a split, the player ends up with two 
	 * hands with a bet for each hand and both the hands are compared with the dealer for a 
	 * win/loss/push(draw) outcome and the players available credits are updated. At the end of each
	 * round, the cards dealt and the ensuing action of the player and dealer and the outcome is 
	 * displayed to the user. The user is again prompted to "bet" or to enter a "0" to end the game. 
	 * At the end of the game, a summary of the entire game with some basic statistics is displayed 
	 * to the user.
	 */
	
	public void play(){

		int bet = promptUserForBet();
		
		if(bet == 0) return;
		
        deck.shuffle();

        do{
			player.initializePlayerHand();
			dealer.initializeDealerHand();
			final int dealerVisibleTotal = dealer.getCurrentVisibleTotal();
	
			player.playerTurn(dealerVisibleTotal);
			final boolean playerHasLost = player.hasLost();
			dealer.dealerTurn(playerHasLost);
			
			numCardsLeft = totalNumCards - (deck.getCurrentCardIndex());
			numRoundsPlayed++;

			if (player.isSplit()){ //if a player chose "split"
				final int playerCurrentTotal = player.getUpdatedTotal(); 
				final int playerCurrentTotal2 = player.getUpdatedTotal2(); 
				final int dealerCurrentTotal = dealer.getUpdatedTotal(); 
				
				boolean isPlayerBlackjack = player.checkBlackJack();
				//compare dealerHand total with playerHand1 total, (blackjack hand1 after split no bonus
				determineIfPlayerOrDealerWins(playerCurrentTotal, dealerCurrentTotal, 
											  isPlayerBlackjack, bet, 1, "PLAYER HAND1");
				
				//check dealerhand total with playerHand2 total, (blackjack hand2 after split no bonus)
				isPlayerBlackjack = player.checkBlackJack2();
				determineIfPlayerOrDealerWins(playerCurrentTotal2, dealerCurrentTotal, 
											  isPlayerBlackjack, bet, 1, "PLAYER HAND2");				
			}
			else { 
				final int playerCurrentTotal = player.getUpdatedTotal(); 
				final int dealerCurrentTotal = dealer.getUpdatedTotal(); 
				
				boolean isPlayerBlackjack = player.checkBlackJack();
				determineIfPlayerOrDealerWins(playerCurrentTotal, dealerCurrentTotal, 
											  isPlayerBlackjack, bet, 1.5, "PLAYER");
			} 
			
			
			if(numChipsLeft == 0) break;
			
			player.resetPlayer();
			dealer.resetDealer();
			
			if(numCardsLeft < 4){
				deck.shuffle();
				deck.setCurrentCardIndex(0);
				numCardsLeft = totalNumCards;
				deck.resetCardCountArray();
			}
			
			bet = promptUserForBet();
			if (numChipsLeft > 2*bet)
				player.setEnoughCreditsForSplit(1);
		}while(numChipsLeft >= 0 && numCardsLeft >= 4 && bet != 0); 
	}

	
	/**
	 * prints the summary of the game
	 */
	public void printSummary(){
    	System.out.println();
    	System.out.println("************** GAME SUMMARY ***************");
    	System.out.println("	Chips Left: " + numChipsLeft + ", Cards Left: " + numCardsLeft);
    	System.out.println("	Total # of Rounds Played: " + numRoundsPlayed);
    	if(numRoundsPlayed > 0){
	    	System.out.println("	# of Rounds Player Won: " + numRoundsPlayerWon);
	    	//System.out.println("	Winning Percentage: " + ((numRoundsPlayerWon*1.0)/numRoundsPlayed)*100 + "%");
	    	System.out.print("	Winning Percentage: ");
	    	System.out.printf("%.1f", ((numRoundsPlayerWon*1.0)/numRoundsPlayed)*100);
	    	System.out.println("%");
    	}
    	System.out.println("********************************************");
    }
	
	
	/**
	 * helper function to decide outcome of the round based on players and dealers total
	 * @param playerCurrentTotal
	 * @param dealerCurrentTotal
	 * @param isPlayerBlackjack
	 * @param bet
	 * @param betMultiple
	 * @param message
	 */
	private void determineIfPlayerOrDealerWins(int playerCurrentTotal, int dealerCurrentTotal, boolean isPlayerBlackjack, 
											   int bet, double betMultiple, String message){
		if(playerCurrentTotal < 21){
			if(playerCurrentTotal > dealerCurrentTotal){
				playerWins(bet, 1, message + " WINS");
			}else if(playerCurrentTotal < dealerCurrentTotal){
				if(dealerCurrentTotal <= 21)
					playerLoses(bet, message + " LOSES");
				else
					playerWins(bet, 1, message + " WINS");
			}
			else if (playerCurrentTotal == dealerCurrentTotal){
				playerDraws(bet, message + " PUSH");
			}
		}
		
		if(playerCurrentTotal > 21){
			playerLoses(bet, "BUSTED - " + message + " LOSES");
		}
		
		if(playerCurrentTotal == 21){
			if(isPlayerBlackjack){
				determineIfPlayerWinsOrDrawsWithBlackjack(bet, betMultiple, message);
			}
			else{
				checkIfPlayerCanWinAt21WithoutBlackjack(bet, message);
			}
		}
	}

	/**
	 * helper function to check if player wins or draws/push where the players hand is a Blackjack
	 * @param bet
	 * @param betMultiple
	 * @param message
	 */
	
	private void determineIfPlayerWinsOrDrawsWithBlackjack(int bet, double betMultiple, String message){
		   if(dealer.checkDealerBlackJack()){
			   playerDraws(bet, message + ": BlackJack but PUSH");
		   }
		   else{
			   playerWins(bet, betMultiple, message + " WINS : BlackJack!!! ");
		   }		
	}
	
	/**
	 * helper function to check if player wins or draws/push where the players hand is 21 but not
	 * a Blackjack
	 * @param bet
	 * @param message
	 */
	private void checkIfPlayerCanWinAt21WithoutBlackjack(int bet, String message){
		final int dealerCurrentTotal = dealer.getCurrentTotal();
		if(dealerCurrentTotal != 21){
			playerWins(bet, 1, message + " WINS");
		}
		else{
			if(dealer.checkDealerBlackJack())
				playerLoses(bet, message + " LOSES - DEALER BLACKJACK");
			else
				playerDraws(bet, message + ": PUSH");
		}
	}
	
	private void playerWins(int bet, double betMultiple, String printMessage){
		System.out.println(printMessage);
		numChipsLeft = player.updateChipsTotal(bet, betMultiple);
		numRoundsPlayerWon++;
	}
	
	private void playerLoses(int bet, String printMessage){
		System.out.println(printMessage);
		numChipsLeft = player.updateChipsTotal(bet, -1);
	}
	
	private void playerDraws(int bet, String printMessage){
		System.out.println(printMessage);
		numChipsLeft = player.updateChipsTotal(bet, 0);
	}
	
	
	/**
	 * helper function to collect bets from the user
	 * @return
	 */
	
	private int promptUserForBet(){
		int bet = howManyChipsToBet();
		while((bet > numChipsLeft) || (bet < 0)){
			System.out.println("Please bet 1 to " + numChipsLeft + " credits.");
			bet = howManyChipsToBet();
		}
		return bet;
	}
	
	private int howManyChipsToBet(){
			int num = -1;
				System.out.println();
			    System.out.println("---------------- ROUND " + (numRoundsPlayed+1) + " -----------------------------");
				System.out.println("You currently have " + numChipsLeft + " chips and " + numCardsLeft + " cards left in the deck.");
				System.out.println("How many chips do you want to bet? Enter 0 to end the game");
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				num = Integer.parseInt(br.readLine());
			}
			catch(IOException e){
				System.err.println("Not a valid input ");
			}
			return num;
	}
   
}
