package ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ms.events.BlockEvent;
import ms.events.GameOverEvent;

/**
 * An implementation of the BoardModel interface that uses a 2D array of Block objects to represent the board.
 * @author denzelhall
 *
 */

@Component
@Scope("prototype")
public class BlockArrayBoard implements BoardModel{
	private Block[][] board;
	private List<Pair> bombLocations;				//keeps track of the bombs in the board so no need to look for them
	private boolean lost;
	private int numUnRevealedBlocks;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Component
	@Scope("prototype")
	public static class Builder extends BoardBuilder{
		@Autowired
		private ApplicationContext ac;
		
		public Builder(int numRows, int numCols, int numBombs) throws IllegalArgumentException {
			super(numRows, numCols, numBombs);
		}
		
		@Override
		public BoardModel build() {
			return ac.getBean(BlockArrayBoard.class, this);
		}
	}
	
	/**
	 * Creates a numRows by numCols Board with numBombs amount of bombs.
	 * @param numRows the number of rows on the Board.
	 * @param numCols the number of columns on the Board.
	 * @param numBombs the number of bombs on the Board.
	 */
	private BlockArrayBoard(Builder builder) {
		this.numUnRevealedBlocks = (builder.getNumRows() * builder.getNumCols()) - builder.getNumBombs();
		this.bombLocations = new ArrayList<>(builder.getNumBombs());
		this.lost = false;
		
		Random rand = new Random();
		//if startRowPos == -1 and startColPos == -1 then a starting position was not set.
		//Assign a random start position if that's the case
		if (builder.startRowPos == -1 && builder.startColPos == -1) {
			builder.setStartPosition(rand.nextInt(builder.getNumRows()), rand.nextInt(builder.getNumCols()));
		}
		board = new Block[builder.getNumRows()][builder.getNumCols()];
		int[][] config = BoardConfiguration.getConfiguration(builder);
		for (int i = 0; i < builder.getNumRows(); i++) {
			for (int j = 0; j < builder.getNumCols(); j++) {
				board[i][j] = new Block(i, j, config[i][j]);
				if (config[i][j] == -1) {
					bombLocations.add(new Pair(i,j));
				}
			}
		}
	}

	@Override
	public void flag(int row, int col) throws IllegalArgumentException {
		BoardConfiguration.checkArgumentsOutOfBounds(row, col, board.length, board[0].length);		//throws IllegalArgumentException
		if (isGameOver() || board[row][col].isFlagged() || board[row][col].isRevealed()) {
			return;
		}
		board[row][col].flagged = true;
		publisher.publishEvent(new BlockEvent(board[row][col], false, false));
	}
	
	@Override
	public void unFlag(int row, int col) throws IllegalArgumentException {
		BoardConfiguration.checkArgumentsOutOfBounds(row, col,board.length, board[0].length);		//throws IllegalArgumentException
		if (!board[row][col].isFlagged() || isGameOver()){ 
			return;
		}
		board[row][col].flagged = false;
		publisher.publishEvent(new BlockEvent(board[row][col], false, false));
	}

	@Override
	public void reveal(int row, int col) throws IllegalArgumentException {
		BoardConfiguration.checkArgumentsOutOfBounds(row, col, board.length, board[0].length);		//throws IllegalArgumentException
		/* Do not reveal if:
		 * 1. game is over or
		 * 2. block at that position has been flagged or
		 * 3. the block has already been revealed.
		 */
		if (board[row][col].isFlagged() || isGameOver() || board[row][col].isRevealed()) {			
			return;
		}
		
		board[row][col].revealed = true;
		numUnRevealedBlocks--;
		
		if (board[row][col].getVal() == -1) {
			//If the block is a bomb then reveal all bombs and set lost = true
			publisher.publishEvent(new BlockEvent(board[row][col], true, false));
			board[row][col].revealed = true;
			bombLocations.stream()
						 .filter(p -> !board[p.getRow()][p.getCol()].revealed)			//only reveal unrevealed bombs.
						 .filter(p -> !board[p.getRow()][p.getCol()].isFlagged())		//only reveal unflagged bombs.
						 .forEach(this::reveal);
			//Then unflag all non bombs that were incorrectly flagged.
			unFlagBadFlags();
			lost = true;
			publisher.publishEvent(new GameOverEvent(board[row][col], false, true));
		} else if (isGameOver()) {
			//If the block isn't a bomb and game is over then the player has won.
			//So reveal the final block.
			publisher.publishEvent(new BlockEvent(board[row][col], false, false));
			//And flag all unflagged bombs.
			bombLocations.stream()
						 .filter(p -> !board[p.getRow()][p.getCol()].isFlagged())
						 .forEach(this::flag);
			publisher.publishEvent(new GameOverEvent(board[row][col], true, false));
		} else {
			publisher.publishEvent(new BlockEvent(board[row][col], false, false));
			//If the block is empty the reveal all of it's neighboring blocks
			if (board[row][col].getVal() == 0) {
				BoardConfiguration.getNeighbors(row, col, board.length, board[0].length)
								  .stream()
								  .filter(p -> !board[p.getRow()][p.getCol()].isRevealed())		//only reveal unrevealed blocks
								  .forEach(p -> reveal(p.getRow(), p.getCol()));
			}
		}
	}
	
	private void reveal(Pair pair) {
		board[pair.getRow()][pair.getCol()].revealed = true;
		publisher.publishEvent(new BlockEvent(board[pair.getRow()][pair.getCol()], false, false));
	}
	
	private void flag(Pair pair) {
		board[pair.getRow()][pair.getCol()].flagged = true;
		publisher.publishEvent(new BlockEvent(board[pair.getRow()][pair.getCol()], false, false));
	}
	
	private void unFlagBadFlags() {
		for (int row = 0; row < board.length; row++) {
			for (int col  = 0; col < board[row].length; col++) {
				if (!bombLocations.contains(new Pair(row, col)) && board[row][col].isFlagged()) {
					//unflag() is safe to use because lost has not been set to true yet.
					unFlag(row, col);
					publisher.publishEvent(new BlockEvent(board[row][col], false, true));
				}
			}
		}
	}
	
	private boolean isGameOver() {
		/**Game is over if:
		 * 1. All Blocks that are not bombs have been revealed.
		 * 2. A Block that is a bomb as been revealed (lost == true).
		 */
		return lost || numUnRevealedBlocks == 0;
	}
}