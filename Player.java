package blackjackgame;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author gopal
 *
 */

public class Player {
	private static Player sInstance = null; //since we have only one player 
	private List<Card> playerHand;
    private List<Card> playerHand2; //incase the player splits, for simplicity player can split only once
    private int currentTotal;
    private int currentTotal2; // incase player splits 
    private int currentCredits;
    private int split;
    private int playerBlackJack;  // set if hand1 is blackjack
    private int playerBlackJack2; // set if hand2 is blackjack
    private int numAces; //keeps track of num of Aces
    private int nonAceTotal; // keeps a running total of cards other than Aces
    private int numAces2; //keeps track of num of Aces in Hand2 after a split
    private int nonAceTotal2; //keeps a running total of cards other than Aces in Hand2
    private int enoughCreditsForSplit;
    private Deck deck;
    
    private Player(){
    	
    }
    
    public static Player getInstance(){
    	if(sInstance == null)
    		sInstance = new Player();
    	return sInstance;
    }
    
    public void init(){
    	currentTotal = 0;
	    currentTotal2 = 0;
	    currentCredits = 100;
	    split = 0; 
	    playerHand = new ArrayList<Card>();
	    deck = Deck.getInstance();
    }
    
	
	/**
	 * Deals the players hand
	 */
    public void initializePlayerHand() {
	    final Card[] cards = deck.getCards();
	    final int index = deck.getCurrentCardIndex();
		
	    nonAceTotal = 0;
	    nonAceTotal2 = 0;
	    numAces = 0;
	    numAces2 = 0;
		
	    dealCard(cards,index);
	    printCurrentCard(cards[index]); 
	    dealCard(cards,index + 1);
	    printCurrentCard(cards[index+1]); 
		deck.setCurrentCardIndex(index+2); //set the index from which the next card is dealt
	   
		playerBlackJack = 0;
	    playerBlackJack2  = 0;
    }
	
	/**
	 * Checks if the players hand is Blackjack, it returns immediately else calls playerStrategy 
	 * to decide on its action. 
	 * @param dealerVisibleTotal
	 */
	
	public void playerTurn(int dealerVisibleTotal) {
		int ret = 0;
		if (isBlackJack()){
			System.out.println("PLAYER GOT BLACKJACK!!!");
			playerBlackJack = 1; //set the variable to indicate that player hand is a blackjack
			return;
		}else{
			playerBlackJack = 0;
			while(ret != 2){			
			    ret = playerStrategy(dealerVisibleTotal); //decides what action to take
				switch(ret){
					case 1: //hit
						playerHit();
						break;
					case 2: //stand
						break;
					case 3: //split
						playerSplit(); //in our case split only once and can get a card for each hand and no further action
						ret = 2;
						break;
					default:
						break;
				}
			}
		}
		
	}
	
    /**
     * This method deals an additional card to the player from the deck. In case it runs
	 *  out of cards, the deck is shuffled and a card is dealt from the new shuffled deck.
     */
	
	private void playerHit() {
		final Card[] cards = deck.getCards();
		final int index = deck.getCurrentCardIndex();
		if (index < 52){ // as long as there is atleast 1 card remaining in the deck
			dealCard(cards, index);
			System.out.println("Player Turn:: HIT");
			printCurrentCard(cards[index]);
			//System.out.println("Player:: the current total is " + getUpdatedTotal());
			deck.setCurrentCardIndex(index + 1);
		}
		else{ // no more cards, reshuffle the deck
			System.out.println("No more cards left, cards shuffled and then hit");
			deck.shuffle();
			deck.resetCardCountArray();
			dealCard(cards, 0);
			System.out.println("Player Turn:: HIT");
			printCurrentCard(cards[0]);
			//System.out.println("Player:: the current total is " + getUpdatedTotal());
			deck.setCurrentCardIndex(1);
			
		}
	}
	
	
	/**
	 * Handles the "split" by a player, by splitting the existing hand into two hands and
	 * then drawing a card for each hand by performing a "hit" exactly once.
	 */
	private void playerSplit(){
		System.out.println("Player:: split the cards ");
		/* create two hands , bet on each hand 
		   for each hand compare player hand with dealer hand */
		final Card[] cards = deck.getCards();
		final int index = deck.getCurrentCardIndex();
	    
		playerHand2 = new ArrayList<Card>(playerHand.subList(1, 2));
		int val2 = (playerHand2.get(0)).getVal();
	    currentTotal2 += getCardPointValue(val2);
		playerHand.remove(1);
		currentTotal -= currentTotal2;
		
		if(val2 != 1){ //keeping track of nonAceTotal for each Hand
			nonAceTotal2 += getCardPointValue(val2);
			nonAceTotal -= getCardPointValue(val2);
		}
		else {
			numAces--; //keeping track of num of aces in each Hand
			numAces2++;
		}
		
		if(index < 51){ //as long as there are atleast 2 cards remaining in the deck
			dealCard(cards, index);
			System.out.println("Player:: Hand1 after SPLIT - hit with a card ");
			printCurrentCard(cards[index]);
			System.out.println("Player:: the current total is " + getUpdatedTotal());
			if(isBlackJack()){ // check for blackjack condition for the first hand
				playerBlackJack = 1;
			}
			System.out.println("Player Turn:: Hand1 STAND");
			
		    dealCardHand2(cards,index+1);
			System.out.println("Player:: Hand2 after SPLIT - hit with a card ");
			printCurrentCard(cards[index +1]);
			System.out.println("Player:: Hand2 the current total is " + getUpdatedTotal2());
			
			if(isBlackJackHand2()){ // check for blackjack condition for second Hand
				playerBlackJack2 = 1;
			}
			deck.setCurrentCardIndex(index+2);
			System.out.println("Player Turn:: Hand2 STAND");
		}
		else{
			System.out.println("No more cards left, cards shuffled and then hit");
			deck.shuffle();
			deck.resetCardCountArray();
			
			dealCard(cards, 0);
			System.out.println("Player:: Hand1 after SPLIT - hit with a card ");
			printCurrentCard(cards[0]);
			System.out.println("Player:: the current total is " + getUpdatedTotal());
			if(isBlackJack()){ // check for blackjack condition for the first hand
				playerBlackJack = 1;
			}
			System.out.println("Player Turn:: Hand1 STAND");
			
		    dealCardHand2(cards,1);
			System.out.println("Player:: Hand2 after SPLIT - hit with a card ");
			printCurrentCard(cards[1]);
			System.out.println("Player:: Hand2 the current total is " + getUpdatedTotal2());
			
			if(isBlackJackHand2()){ // check for blackjack condition for second Hand
				playerBlackJack2 = 1;
			}
			
			deck.setCurrentCardIndex(index+2);
			System.out.println("Player Turn:: Hand2 STAND");
		}
	}
	

    
    /**
     * This method implements the player strategy. In case of hands with the same face value,
     * the player can "split". (based on its hand and the dealer's face up card). Else it decides
     * to "hit" or "stand" based on the chances of busting calculated by using the value of its hand 
     * as well as using the countCardArray which keeps a count of cards of each face value already 
     * used in the game.
     * 
     */
	private int playerStrategy(int dealerVisibleTotal){
		System.out.println("Player:: the current total is " + getUpdatedTotal());
		if(split == 0 && enoughCreditsForSplit == 1){ //split only once allowed
	    	final int playerFirstCardValue = playerHand.get(0).getVal();
	    	final int playerSecondCardValue = playerHand.get(1).getVal();
	    	if (playerFirstCardValue == playerSecondCardValue){
	    		if(playerFirstCardValue == 1 || playerFirstCardValue == 8){
	    			this.split = 1;
	    			return 3;
	    		}   
	    		else if ((dealerVisibleTotal > 1 && dealerVisibleTotal < 8) && 
	    				(playerFirstCardValue == 2 || playerFirstCardValue == 3 || playerFirstCardValue == 7)){
	    			this.split = 1;
	    			return 3;
	    		}	
	    		else if ((dealerVisibleTotal > 1 && dealerVisibleTotal < 7) && playerFirstCardValue == 6){
	    			this.split = 1;		  
	    			return 3; //split
	    		}
	    		else if (((dealerVisibleTotal > 1 && dealerVisibleTotal < 7) || 
	    				(dealerVisibleTotal > 7  && dealerVisibleTotal < 10)) && 
	    				(playerFirstCardValue == 9)){
				    	this.split = 1;		  
				    	return 3; //split
				  	}
	    	}
	    }
		
	    //simple hit strategy- probability of getting under 21 based on currentCardValues vs player busting
	    //                     and if the current total is not 17 or greater similar to dealer   
	    int updatedTotal = getUpdatedTotal();
	    
	    if(updatedTotal < 17){
	    	double successProb = 0.0;
			double bustProb = 0.0;
			
			int bestCardVal = 21 - updatedTotal; // bestCardVal gives the player a total of 21	
			if (bestCardVal > 11) bestCardVal = 11; // since max value card is 11(an Ace)
			
			for(int i=1;i<=bestCardVal; i++){
				successProb += calcProbCardOccurance(i);
			}
	
			for (int i=bestCardVal+1;i<12;i++){
				bustProb += calcProbCardOccurance(i);
			}
			//System.out.println("successProb: " + successProb + "bustProb: "+bustProb);
			if( successProb > bustProb)
				  return 1; // hit
		}
	    if(updatedTotal <= 21)
	        System.out.println("Player Turn:: STAND");
	    else
	    	System.out.println("Player busted");
		return 2; //stand
	}

	
	/**
	 * helper function to calculate probability of occurance of a card based on remaining cards
	 * in the deck
	 * @param i
	 * @return
	 */
    private double calcProbCardOccurance(int i){
    	int cardProb;
    	int numRemainingCards = 52 - deck.getCurrentCardIndex();
    	if (numRemainingCards == 0)
    		numRemainingCards = 52; //shuffle will happen next time and remaining cards would be 52 
    	if (i == 10)
    	{
    	  cardProb = deck.getCardCountArrayVal(9)+  deck.getCardCountArrayVal(10) 
    			    + deck.getCardCountArrayVal(11) + deck.getCardCountArrayVal(12);  		
    	}
    	else if (i == 11){
    		cardProb = deck.getCardCountArrayVal(0);
    	}
    	else {
    	  cardProb = deck.getCardCountArrayVal(i-1); //get the number of cards remaining for that value
    	}  
    	double p = (cardProb*1.0)/(numRemainingCards);
    	return p;
    }
    
    
     /* deal a card to Hand1 */
     public void dealCard(Card[] cards,int index){
    	 int val;
         playerHand.add(cards[index]); // player draws a card
		 val = cards[index].getVal();
		 if(val != 1) 
			nonAceTotal += getCardPointValue(cards[index].getVal()); //add to nonAceTotal if the card is not an Ace
		 else
			numAces++; // card is an Ace, increment number of Aces held 
			
		 deck.cardCountArray[(val-1)]--; //tracking the cards used in countCardArray
		 //deck.setCurrentCardIndex(index+1);
		 currentTotal += getCardPointValue(val);
    	 
     }
     
     /* deal a card to Hand2 */
     public void dealCardHand2(Card[] cards,int index){
    	 int val;
    	 playerHand2.add(cards[index]); // Hand2 draws a card
    	 val = (cards[index].getVal());
    	 if(val != 1)
    	 	nonAceTotal2 += getCardPointValue(val); 
    	 else
   	 		numAces2++;
    	 deck.cardCountArray[(val-1)]--;
    	 currentTotal2 += getCardPointValue(val);
    	 
     }
     
    /* returns the total using Ace value either as 1 or 11 */
    public int getUpdatedTotal(){
    	int total = nonAceTotal;
    	for(int i=0;i<numAces;i++){
    		total +=  11;
    		if (total > 21)
    			total -= 10;
    	}
    	return total;
    }
    
    /* returns the total for Hand2 using Ace value either as 1 or 11 */
    public int getUpdatedTotal2(){
    	int total = nonAceTotal2;
    	for(int i=0;i<numAces2;i++){
    		total +=  11;
    		if (total > 21)
    			total -= 10;
    	}
    	return total;
    }
    
    public int getCurrentTotal() {
		return currentTotal;
	}
    
    
    public int getCurrentTotal2(){
		return currentTotal2;
	}
    
    private boolean isBlackJack(){
		return (getCurrentTotal() == 21);
	}
	
	public boolean checkBlackJack(){
		return(playerBlackJack == 1);
	}	

	private boolean isBlackJackHand2(){
		return (getCurrentTotal2() == 21);
	}
	
	public boolean checkBlackJack2(){
		return(playerBlackJack2 == 1);
	}
	
	public void resetPlayerHand(){
		playerHand.clear();
		if(this.split == 1)
			   playerHand2.clear();
	}
		
	public void resetCurrentTotal(){
		currentTotal = 0;
	}
	
	public void resetPlayer(){
		resetPlayerHand();
		resetCurrentTotal();
		split = 0; 
		enoughCreditsForSplit = 0;

	}
	
	public boolean hasLost(){
		return currentTotal > 21;
	}
	
	public boolean isSplit(){
		return (split>0?true:false);
		
	}
	
	/* 
	 * Here J,Q,K treated as 10,
	 * A is initially treated as 11, In player strategy in deciding whether to hit/split/stand
	 * an updated hand value is calculated where A can take either 1/11 based on  whether the 
	 * sum of the rest of the hand is > 10 or <=10  
	 */
	private int getCardPointValue(int cardVal){
		if(cardVal > 10) return 10;
		else if(cardVal == 1) return 11;
		else return cardVal;
	}
	
	public int updateChipsTotal(int bet, double val) {
		currentCredits += val * bet;
		return currentCredits;
	}

	public void setEnoughCreditsForSplit(int flag){
		enoughCreditsForSplit = 1;
	}

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
		System.out.println("The current card dealt to player is " +
							card.getVal() +" and suit is " + card.getSuit());
		}
		else if (val == 1 || (val > 10 && val <=13)){
			System.out.println("The current card dealt to player is " +
					str + " and suit is " + card.getSuit());
		}
		
	}
}

