package kalah;

import java.util.Random;
import java.util.logging.Logger;

public class Kalah {
	
	private static Logger log=Logger.getLogger(Kalah.class.getName());
	private int stones;
	private Player nextTurn;
	private Board board;

	public Kalah() {
		stones=6;
		board=new Board(stones);
	}
	
	public void setFirstTurn(Player player) {
		nextTurn=player;
	}

	public boolean isFinished() {
		return board.isFinished();
	}

	public Player getNextTurn() {
		return nextTurn;
	}

	public Board play(int houseNumber) {
		log.info("Turn: "+getNextTurn() +", HouseNumber: "+houseNumber);
		nextTurn = board.move(houseNumber,nextTurn);
		log.info(board.toString());
		return board;
	}

	public int getRandomHouseNumber() {
		int max=getNextTurn()==Player.PLAYER1?6:12;
		int min=getNextTurn()==Player.PLAYER1?1:6;
		Random random = new Random();
		
		int houseNumber=0;
		do {
			houseNumber=random.nextInt(max - min + 1) + min;
		}while(board.peekStonesFromHouse(houseNumber)==0);
		
		return houseNumber;
	}

	public Player getWinner() {
		return board.getWinner();
	}

}

enum Player {
	PLAYER1,PLAYER2;
	
	public Player getOtherPlayer() {
		return this==Player.PLAYER1?PLAYER2:PLAYER1;
	}

}
