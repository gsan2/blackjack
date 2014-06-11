package blackjackgame;
import java.util.ArrayList;

/**
 * 
 * @author gopal
 *
 */

public class Dealer {
	private static Dealer sInstance = null;
	private ArrayList<Card> dealerHand = new ArrayList<Card>();
	private int currentTotal;
	private int currentVisibleTotal;
	private Deck deck;
	private int dealerBlackJack; //this is set if dealer gets Blackjack
	private int numAces; //keeps track of num of Aces
    private int nonAceTotal; // keeps a running total of cards other than Aces
	
	/* constructor */
    private Dealer(){
    	
    	
    }
    public static Dealer getInstance(){
		if(sInstance == null) {
			sInstance = new Dealer();
		}
		return sInstance;
	}
    
    public void init(){
    	currentTotal = 0;
		currentVisibleTotal = 0;
    	deck = Deck.getInstance();
    }
	
	
	/**
	 * This method deals two cards to the Dealer. and sets the value of the face up card
	 * in currentVisibleTotal
	 */
	public void initializeDealerHand(){
		final Card[] cards = deck.getCards();
		final int index = deck.getCurrentCardIndex();

		nonAceTotal = 0;
		numAces = 0;

		dealCard(cards,index);		
		printCurrentCard(cards[index]); //display the card
		currentVisibleTotal = getCardPointValue(cards[index].getVal()); //first card visible

		dealCard(cards,index+1);
		printCurrentCard(cards[index+1]);
		currentTotal += currentVisibleTotal + getCardPointValue(cards[index+1].getVal()); // Running total of all cards in dealers hand (used to check for Blackjack
                                                                                          // Aces are counted as 11 
		deck.setCurrentCardIndex(index+2); //set the index from which the next card is dealt
		dealerBlackJack = 0;
	}

	/**
	 * If the player has already busted return. Then check if dealer has a BlackJack return, else it 
	 * keeps "hitting" till the hand is 17 or greater
	 * @param playerHasLost
	 */
	public void dealerTurn(boolean playerHasLost){
		System.out.println("Dealer:: the current total is " + getUpdatedTotal());
		if(playerHasLost) return; // if player has already busted, just return;
	
		if (isDealerBlackJack()){ // check for BlackJack condition
			System.out.println("DEALER GOT BLACKJACK!!!");
			dealerBlackJack = 1;
			return;
		}else{
			dealerBlackJack = 0;
			while(getUpdatedTotal() < 17){ //standard rule for dealer 
				dealerHit();
			}
		}
		//stand
	}
	
	/**
	 *  This method deals an additional card to the dealer from the deck. In case it runs
	 *  out of cards, the deck is shuffled and a card is dealt from the new shuffled deck.
	 */
	private void dealerHit(){
		final Card[] cards = deck.getCards();
		final int index = deck.getCurrentCardIndex();
		if (index < 52){ // as long as there is atleast 1 card remaining in the deck
			dealCard(cards,index);
			currentTotal += getCardPointValue(cards[index].getVal());
			System.out.println("Dealer Turn:: HIT");
			printCurrentCard(cards[index]);	
			System.out.println("Dealer:: the current total is " + getUpdatedTotal());
			deck.setCurrentCardIndex(index+1);
		}
		else{ // no more cards, reshuffle the deck
			System.out.println("No more cards left call shuffle and then hit");
			deck.shuffle();
			deck.resetCardCountArray();
			dealCard(cards,0);
			currentTotal += getCardPointValue(cards[0].getVal());
			System.out.println("Dealer Turn:: HIT");
			printCurrentCard(cards[0]);
			System.out.println("Dealer:: the current total is " + getUpdatedTotal());
			deck.setCurrentCardIndex(1);
		}

	}
	
	/**
	 * helper function to check if the dealer hand is a Blackjack 
	 * @return
	 */
	public boolean isDealerBlackJack(){
		return (getCurrentTotal() == 21);
	}
	
	public boolean checkDealerBlackJack(){
		return(dealerBlackJack == 1);
	}
	
	
	/* deal a card to Dealer */
    public void dealCard(Card[] cards,int index){
   	 	int val;
        dealerHand.add(cards[index]); // player draws a card
		val = cards[index].getVal();
		if(val != 1) 
			nonAceTotal += getCardPointValue(cards[index].getVal()); //add to nonAceTotal if the card is not an Ace
		else
			numAces++; // card is an Ace, increment number of Aces held 
			
		deck.cardCountArray[(val-1)]--; //tracking the cards used in countCardArray
    }
    
	/* Assigns the value of Ace to be either 1 or 11 based on the total sum of the cards in the hand */
	public int getUpdatedTotal(){
	   	int total = nonAceTotal;
	   	for(int i=0;i<numAces;i++){
	    		total +=  11;
	    		if (total > 21)
	    			total -= 10;
	    }
	    return total;
	}
	
	public int getCurrentTotal(){
		return currentTotal;	
	}
	
	
	public void resetDealerHand(){
		   dealerHand.clear();
		}
	
	public void resetCurrentTotal(){
		currentTotal = 0;
	}

	public void resetDealer(){
		resetDealerHand();
		resetCurrentTotal();
	}
	
	public int getCurrentVisibleTotal(){
		return currentVisibleTotal;
	}
	
	private int getCardPointValue(int cardVal){
		if(cardVal > 10) return 10; 
		else if(cardVal == 1) return 11;
		else return cardVal;
	}
	
	/* prints the card and its suit to teh display */
	private void printCurrentCard(Card card){
		int val = card.getVal();
		String str;
		if(val == 11) 
			str = "Jack";
		else if (val == 12) 
			str = "Queen";
		else if (val == 13) 
			str = "King";
		else if (val == 1) 
			 str = "Ace";
		else
			str = "";
		
		if (val > 1 && val <= 10){
		System.out.println("The current card dealt to dealer is " +
							card.getVal() +" and suit is " + card.getSuit());
		}
		else if (val == 1 || (val > 10 && val <=13)){
			System.out.println("The current card dealt to dealer is " +
					str + " and suit is " + card.getSuit());
		}	
	}
	
}
