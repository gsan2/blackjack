package blackjackgame;

/**
 * 
 * @author gopal
 *
 */

public class Deck {
	private static Deck sInstance = null;
	private Card[] cards;
	private String[] suits = {"Spades","Clubs","Hearts","Diamonds"};
	private int currIndex;
	int cardCountArray[]; // keep count of cards
	int numCards;
	
	private Deck(){
		
	}
	
	public static Deck getInstance(){
		if(sInstance == null)
			sInstance = new Deck();
		return sInstance;
	}	

	public void init(int numCardsLeft){
		  cards = new Card[numCardsLeft];
		  cardCountArray = new int[13];
		  currIndex = 0;
		  numCards = numCardsLeft;
		  int num = 0;
		  
		  // Initialize the cards 
		  for(int i=0;i<suits.length;i++){
			  for(int j=0;j<cardCountArray.length;j++){
				  this.cards[num] = new Card(suits[i],(j+1));
				  num++;
			  }
		  }
		  // Initialize the cardCoountArray  
		  for (int i=0;i<13;i++){
			  cardCountArray[i] = numCardsLeft/13; // initializing to 4 if deck is 1 as in our case
		  }
	}
	

	/* Random shuffling of cards in the deck */ 
	public void shuffle(){
		for(int i=0;i<numCards;i++){
				int indexToSwap = (int) Math.floor(Math.random()*52);
				Card temp = new Card("",0); 
				temp.setVal(cards[i].getVal());
				temp.setSuit(cards[i].getSuit());
				cards[i].setVal(cards[indexToSwap].getVal());
				cards[i].setSuit(cards[indexToSwap].getSuit());
				cards[indexToSwap].setVal(temp.getVal());
				cards[indexToSwap].setSuit(temp.getSuit());
		}
	}
	  
	public Card[] getCards(){
		  return cards;
	}
	  	  
	  
	public int getCurrentCardIndex(){
		  return currIndex;
	}
	  
	public void setCurrentCardIndex(int index){
		  currIndex = index;
	}
	  
	  
    /* other functions to access and modify the countCardArray */
	public void  printCardCountArray(){
		  for (int i=0;i<13;i++){
			  System.out.println("cardCountArray["+i+"]:"+ cardCountArray[i]);
		  }
	}
	  
	public void resetCardCountArray(){
	    for (int i=0;i<13;i++){
		  cardCountArray[i] = numCards/13; // intializing to 4 if deck is 1
	    }
	}
	
	public int getCardCountArrayVal(int i){
		  return cardCountArray[i];
	}
}
		
