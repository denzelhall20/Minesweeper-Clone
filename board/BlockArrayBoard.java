package board;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * An implementation of the Board interface that uses a 2D array of Block objects to represent the board.
 * @author denzelhall
 *
 */
public class BlockArrayBoard implements BoardModel{
	private Block[][] board;
	private List<BoardObserver> observers;
	private List<Pair> bombLocations;				//keeps track of the bombs in the board so no need to look for them
	private int numBombs;
	private int numRows;
	private int numCols;
	private boolean lost;
	private int numUnRevealedBlocks;
	private String state;							//Stores the values of the blocks for debugging purposes.
	
	public static class Builder extends BoardBuilder{
		
		/**
		 * Creates a Builder object that contains the number of Rows, the number of Columns, the number of bombs.
		 * @param numRows the number of rows on the board.
		 * @param numCols the number of columns on the board.
		 * @param numBombs the number of bombs on the board.
		 * @throws IllegalArgumentException if any of the parameters are negative of the number of bombs exceeds the number of blocks.
		 */
		public Builder(int numRows, int numCols, int numBombs) throws IllegalArgumentException {
			super(numRows, numCols, numBombs);
		}
		
		@Override
		public BoardModel build() {
			return new BlockArrayBoard(this);
		}
	}
	
	/**
	 * Creates a numRows by numCols Board with numBombs amount of bombs.
	 * @param numRows the number of rows on the Board.
	 * @param numCols the number of columns on the Board.
	 * @param numBombs the number of bombs on the Board.
	 */
	private BlockArrayBoard(Builder builder) {
		this.numRows = builder.getNumRows();
		this.numCols = builder.getNumCols();
		this.numBombs = builder.getNumBombs();
		this.numUnRevealedBlocks = (this.numRows * this.numCols) - this.numBombs;
		this.bombLocations = new ArrayList<>(numBombs);
		this.observers = new ArrayList<>();
		this.lost = false;
		this.state = "";
		
		Random rand = new Random();
		//if startRowPos == -1 and startColPos == -1 then a starting position was not set.
		//Assign a random start position if that's the case
		if (builder.startRowPos == -1 && builder.startColPos == -1) {
			builder.setStartPosition(rand.nextInt(this.numRows), rand.nextInt(this.numCols));
		}
		configure(builder.startRowPos, builder.startColPos);
	}
	
	/**
	 * Configures the board in such a way that the Block at position row, col is not a bomb and is not adjacent to any bomb.
	 * @param row the row position of the starting Block.
	 * @param col the column position of the starting Block.
	 */
	private void configure(int row, int col) {
		//temporary board is need to place bombs and calculate values of blocks
		int[][] tempBoard = new int[numRows][numCols];
		addBombs(tempBoard, row, col);
		
		board = new Block[numRows][numCols];
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				board[r][c] = new Block(tempBoard[r][c]);
				state += String.format("%2d ", tempBoard[r][c]);
			}
			state += "\n";
		}
	}
	
	/**
	 * Randomly adds bombs throughout the board.
	 * @param board the board that the bombs will be added to.
	 * @param row the row component of the start position.
	 * @param col the column component of the start position.
	 */
	private void addBombs(int[][] board, int row, int col) {
		Random rand = new Random();

		//Create list of all potential bomb positions
		List<Pair> potentialBombPos = new ArrayList<>(numRows * numCols);
		
		for(int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				potentialBombPos.add(new Pair(i, j));
			}
		}
		
		/* Don't place a bomb in a position if:
		 * 1. a bomb has already been placed in that position.
		 * 2. the position is the start position.
		 * 3. the position is not adjacent to the starting position.
		 */
		
		//this takes care of #2.
		potentialBombPos.remove(new Pair(row, col));
		
		//this takes care of #3.
		for (Pair pair: BoardModel.getNeighbors(row, col, numRows, numCols)) {
			potentialBombPos.remove(pair);
		}
		
		for (int bombsPlaced = 0; bombsPlaced < numBombs; bombsPlaced++) {
			Pair bombPos = potentialBombPos.get(rand.nextInt(potentialBombPos.size()));
			board[bombPos.getRow()][bombPos.getCol()] = -1;
			
			//this takes care of #1
			potentialBombPos.remove(bombPos);
			
			bombLocations.add(bombPos);
			for (Pair pair: BoardModel.getNeighbors(bombPos.getRow(), bombPos.getCol(), board.length, board[row].length)) {
				if (board[pair.getRow()][pair.getCol()] > -1)
					board[pair.getRow()][pair.getCol()]++;
			}
		}
	}
	
	
	@Override
	public int getValue(int row, int col) throws IllegalStateException, IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		if (!isRevealed(row, col)) {
			throw new IllegalStateException("Tried to get value from block at (" + row + ", " + col + " when it is has not been revealed yet.");
		}
		return board[row][col].getValue();
	}

	@Override
	public void flag(int row, int col) throws IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		if (isGameOver() || isFlagged(row, col) || isRevealed(row, col)) {
			return;
		}
		board[row][col].flag();
		notifyObservers(new BlockState(row, col, -3, BoardEvent.FLAG));
	}
	
	@Override
	public void unflag(int row, int col) throws IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		if (!isFlagged(row, col) || isGameOver()){ 
			return;
		}
		board[row][col].unflag();
		notifyObservers(new BlockState(row, col, -2, BoardEvent.UNFLAG));
	}

	@Override
	public boolean isFlagged(int row, int col) throws IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		return board[row][col].isFlagged();
	}

	@Override
	public boolean isRevealed(int row, int col) throws IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		return board[row][col].isRevealed();
	}

	@Override
	public boolean isBomb(int row, int col) throws IllegalStateException, IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		if (isRevealed(row, col)) {
			throw new IllegalStateException("Tried to check if block at (" + row + ", " + col + " is a bomb when it is has not been revealed yet.");
		}
		return board[row][col].isBomb();
	}

	@Override
	public void reveal(int row, int col) throws IllegalArgumentException {
		BoardModel.checkArgumentsOutOfBounds(row, col, numRows, numCols);		//throws IllegalArgumentException
		/* Do not reveal if:
		 * 1. game is over or
		 * 2. block at that position has been flagged or
		 * 3. the block has already been revealed.
		 */
		if (isFlagged(row, col) || isGameOver() || isRevealed(row, col)) {			
			return;
		}
		
		board[row][col].reveal();
		numUnRevealedBlocks--;
		
		if (board[row][col].isBomb()) {
			//If the block is a bomb then reveal all bombs and set lost = true
			notifyObservers(new BlockState(row, col, -1, BoardEvent.BOMB_CHOSEN));
			for (Pair pair: bombLocations) {
				if (!pair.equals(row, col)) {
					if (!isFlagged(pair.getRow(), pair.getCol())) {
						board[pair.getRow()][pair.getCol()].reveal();
						notifyObservers(new BlockState(pair, -1,  BoardEvent.BOMB_REVEAL));
					}
				}
			}
			//Then unflag all non bombs that were incorrectly flagged.
			unFlagBadFlags();
			lost = true;
			notifyObservers(new BlockState(row, col, board[row][col].getValue(), BoardEvent.GAMELOST));
		} else if (isGameOver()) {
			notifyObservers(new BlockState(row, col, board[row][col].getValue(), BoardEvent.SAFE_REVEAL));
			//If the block isn't a bomb and game is over then the player has won.
			//So flag all unflagged bombs.
			for (Pair pair: bombLocations) {
				Block bomb = board[pair.getRow()][pair.getCol()];
				//Calling flag directly from Block because game is over so this.flag() won't do anything.
				if (!bomb.isFlagged()) {
					bomb.flag();
					notifyObservers(new BlockState(pair, -3, BoardEvent.FLAG));
				}
			}
			notifyObservers(new BlockState(row, col, board[row][col].getValue(), BoardEvent.GAMEWON));
		} else {
			notifyObservers(new BlockState(row, col, board[row][col].getValue(), BoardEvent.SAFE_REVEAL));
			//If the block is empty the reveal all of it's neighboring blocks
			if (board[row][col].getValue() == 0) {
				for (Pair pair: BoardModel.getNeighbors(row, col, numRows, numCols)) {
					if (!board[pair.getRow()][pair.getCol()].isRevealed()) {
						reveal(pair.getRow(), pair.getCol());
					}
				}
			}
		}
		
	}
	
	private void unFlagBadFlags() {
		for (int row = 0; row < board.length; row++) {
			for (int col  = 0; col < board[row].length; col++) {
				if (!BoardModel.contains(bombLocations, row, col) && board[row][col].isFlagged()) {
					//unflag() is safe to use because lost has not been set to true yet.
					unflag(row, col);
					notifyObservers(new BlockState(row, col, -2, BoardEvent.BAD_FLAG));
				}
			}
		}
	}
	
	@Override
	public boolean isGameOver() {
		/**Game is over if:
		 * 1. All Blocks that are not bombs have been revealed.
		 * 2. A Block that is a bomb as been revealed (lost == true).
		 */
		return lost || (numUnRevealedBlocks == 0);
	}
	
	@Override
	public void attach(BoardObserver observer) {
		observers.add(observer);
	}
	
	@Override
	public void dettach(BoardObserver observer) {
		observers.remove(observer);
	}
	
	@Override
	public void notifyObservers(BlockState state) {
		for (BoardObserver observer: observers) {
			observer.update(state);
		}
	}
	
	@Override
	public String toString() {
		return state;
	}
}