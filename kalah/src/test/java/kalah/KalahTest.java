package kalah;

import java.util.logging.Logger;

import org.junit.Test;

public class KalahTest {
	
	private static Logger log=Logger.getLogger(KalahTest.class.getName());

	@Test
	public void test() {

		Kalah kalah = new Kalah();
		kalah.setFirstTurn(Player.PLAYER1);
		int counter=0;
		Board board = null;
		while(!kalah.isFinished()) {
			
			log.info("Play: "+counter++);
			int houseNumber=kalah.getRandomHouseNumber();
			board = kalah.play(houseNumber);
			
		}
		
		Player player=kalah.getWinner();
		log.info("Winner player: "+player);
		log.info(board.toString());
	}


}
