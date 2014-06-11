package blackjackgame;

/**
 * 
 * @author gopal
 *
 */

public class Card {
	private int val; // holds the card value
	private String suit; // holds the suit
	
	public Card(String suit,int num){
		this.suit = suit;
		this.val = num;
	}	 
	
	public int getVal(){
		return val;
	}
	public void setVal(int val){
		this.val = val;
	}
	public void setSuit(String suit){
		this.suit = suit;
	}
	
	public String getSuit(){
		return suit;
	}
	
}
