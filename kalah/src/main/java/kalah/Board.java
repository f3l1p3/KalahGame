package kalah;

public class Board {
	
	private static final int KALAH_ROW = 0;

	private static final int PLAYER2_KALAH_INDEX = 7;

	private static final int PLAYER1_KALAH_INDEX = 0;

	private static final int DEFAULT_STONES_NUMBER = 1;

	private static final int HOUSES_NUMBER = 6;
	/*
	 *  we assumed this board layout
	 *  player1: 1  2  3   4   5  6 
	 *  player2: 7  8  9  10  11 12
	 * 
	 */

	private static final int PLAYERS = 2;
	private static final int KALAH_PLUS_HOUSES = 8;
	private int[][] board;

	public Board(int stones) {
		board=new int[PLAYERS][KALAH_PLUS_HOUSES];
		for(int i=0;i<PLAYERS;i++) {
			for(int j=1;j<KALAH_PLUS_HOUSES-1;j++) {
				board[i][j]=stones;
			}
		}
	}

	public Player move(int houseNumber, Player player) {
		if(houseNumber<1 && houseNumber>12) throw new IllegalArgumentException("HouseNumber out of bounds");
		int stones=getAllStonesFromHouse(houseNumber);
		Move move=new Move(player,houseNumber,false);
		boolean wasLastOwnHouseEmpty=false;
		for(int i=0;i<stones;i++) {
			
			move=move.moveToNextHouse();
			
			houseNumber=move.getNextHouseNumber();
			
			/*
			 *check whether next house is empty 
			 */
			wasLastOwnHouseEmpty=peekStonesFromHouse(houseNumber)==0 && houseBelongsToPlayer(player,houseNumber);
			
			/*
			 * add stones into next house
			 */
			if(move.isInKalah()) {
				addStonesToKalah(player,DEFAULT_STONES_NUMBER);
			}else {
				addStoneToHouse(houseNumber,DEFAULT_STONES_NUMBER);
			}
			
		}
		
		/*
		 * if last house was empty
		 * then 
		 */
		if(wasLastOwnHouseEmpty) {
			int oppositeHouseStones=getAllStonesFromHouse(getOppositeHouseNumber(houseNumber));
			/*
			 * add those stones to player's kalah
			 * and the ones from last house too
			 */
			addStonesToKalah(player,oppositeHouseStones+getAllStonesFromHouse(houseNumber));
		}
		
		/*
		 * calculate next player
		 */
		Player nextPlayer=move.getNextPlayer();
		
		return nextPlayer;
	}

	private int getOppositeHouseNumber(int houseNumber) {
		return (houseNumber<=6)?houseNumber+HOUSES_NUMBER:houseNumber-HOUSES_NUMBER; 
	}

	private boolean houseBelongsToPlayer(Player player, int houseNumber) {
		if(player==Player.PLAYER1 && houseNumber>=1 && houseNumber<=6)
			return true;
		else if(player==Player.PLAYER2 && houseNumber>=7 && houseNumber<=12)
			return true;
		
		return false;
	}

	private int removeStonesFromHouse(int houseNumber, int stones) {
		int row = getRow(houseNumber);
		int houseStones = board[row][getColumn(houseNumber, row)];
		return board[row][getColumn(houseNumber, row)]=houseStones-stones;
	}
	
	private int getAllStonesFromHouse(int houseNumber) {
		int stones=peekStonesFromHouse(houseNumber);
		removeStonesFromHouse(houseNumber,stones);
		return stones;
	}

	private void addStonesToKalah(Player player, int stones) {
		int kalahStones = peekStonesFromKalah(player);
		board[KALAH_ROW][player==Player.PLAYER1?PLAYER1_KALAH_INDEX:PLAYER2_KALAH_INDEX]=kalahStones+stones;
	}

	private int peekStonesFromKalah(Player player) {
		return board[KALAH_ROW][player==Player.PLAYER1?PLAYER1_KALAH_INDEX:PLAYER2_KALAH_INDEX];
	}

	private void addStoneToHouse(int nextHouseNumber, int stones) {
		int row = getRow(nextHouseNumber);
		int houseStones = board[row][getColumn(nextHouseNumber, row)];
		board[row][getColumn(nextHouseNumber, row)]=houseStones+stones;
	}

	private int getHouseNumber(int row, int column) {
		int houseNumber=row==1?column+HOUSES_NUMBER:column;
		return houseNumber;
	}

	public int peekStonesFromHouse(int houseNumber) {
		int row = getRow(houseNumber);
		return board[row][getColumn(houseNumber, row)];
	}

	private int getColumn(int houseNumber, int row) {
		return (row==1)?houseNumber-HOUSES_NUMBER:houseNumber;
	}

	private int getRow(int houseNumber) {
		return (houseNumber>HOUSES_NUMBER)?1:0;
	}
	
	@Override
	public String toString() {
		String template =  "                          \n" + 
						   " ------------------------ \n" + 
						   "  %d  %d  %d  %d  %d  %d  \n" + 
						   "                          \n" + 
						   "  %d             %d  \n" + 
						   "                          \n" + 
						   "  %d  %d  %d  %d  %d  %d  \n" + 
						   " ------------------------ ";
		return String.format(template,   board[0][1], board[0][2], board[0][3], board[0][4], board[0][5], board[0][6]
										,board[0][0],board[0][7]
									    ,board[1][1], board[1][2], board[1][3], board[1][4], board[1][5], board[1][6]);
	}
	
	
	class Move {

		private int houseNumber;
		private Player player;
		private boolean inKalah;

		public Move(Player player, int houseNumber, boolean isInKalah) {
			this.player=player;
			this.houseNumber=houseNumber;
			this.inKalah=isInKalah;
		}

		public int getNextHouseNumber() {
			return houseNumber;
		}

		public Player getPlayer() {
			return player;
		}

		public boolean isInKalah() {
			return inKalah;
		}

		public Player getNextPlayer() {
			return isInKalah()?player:player.getOtherPlayer();
		}

		public Move moveToNextHouse() {
			boolean isInKalah=false;
			int row = getRow(houseNumber);
			int column = getColumn(houseNumber, row);
			/*
			 * move column index, according
			 * to row index
			 */
			column=row==0?column-1:column+1;
			if(column==0) {
				if(player==Player.PLAYER2) {
					/*
					 * move to the 
					 * second row, first house
					 */
					column=1;
					row=1;
				}else {
					isInKalah=true;
				}
			}else if(column==7) {
				
				if(player==Player.PLAYER1) {
					/*
					 * move to the 
					 * last house in first row
					 */
					column=6;
					row=0;
				}else {
					isInKalah=true;
				}
			}
			
			int nextHouseNumber = getHouseNumber(row,column);
			
			Move move=new Move(player,nextHouseNumber,isInKalah);
			
			return move;
		}

		private int getColumn(int houseNumber, int row) {
			if(isInKalah()) {
				return player==Player.PLAYER1?0:7; 
			}
			return Board.this.getColumn(houseNumber,row);
		}

		private int getRow(int houseNumber) {
			if(isInKalah()) {
				return player==Player.PLAYER1?1:0; 
			}
			return Board.this.getRow(houseNumber);
		}
		
	}

	public boolean isFinished() {
		return getWinner()!=null;
	}

	public Player getWinner() {
		
		for(Player player:Player.values()) {
			boolean isPlayerRowEmpty=true;
			int min=player==Player.PLAYER1?1:6;
			int max=player==Player.PLAYER1?6:12;
			for(int houseNumber=min;houseNumber<=max;houseNumber++) {
				if(peekStonesFromHouse(houseNumber)!=0) {
					isPlayerRowEmpty=false;
					break;
				}
			}
			if(isPlayerRowEmpty) {
				addOtherPlayerStonesToOwnKalah(player.getOtherPlayer());
				return peekStonesFromKalah(Player.PLAYER1)>peekStonesFromKalah(Player.PLAYER2)?Player.PLAYER1:Player.PLAYER2;
			}
		}
		
		return null;
	}

	private void addOtherPlayerStonesToOwnKalah(Player player) {
		int min=player==Player.PLAYER1?1:6;
		int max=player==Player.PLAYER1?6:12;
		int stones=0;
		for(int houseNumber=min;houseNumber<=max;houseNumber++) {
			stones+=getAllStonesFromHouse(houseNumber);
		}
		
		addStonesToKalah(player.getOtherPlayer(), stones);
	}

}
