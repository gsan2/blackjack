package blackjackgame;

/**
 * 
 * @author gopal
 *
 */

public class Game {

	public static void main(String[] args) {
        System.out.println("Welcome to BlackJack!");
        GameManager gm = new GameManager(1,100);
        gm.initialize();
        gm.play();
        gm.printSummary();
	}

}
