package ms.model;

import lombok.Getter;

/**
 * An abstract class for the creation of a Board object before or after the specification of a starting position.
 * @author denzelhall
 *
 */
public abstract class BoardBuilder {
	@Getter
	private final int numRows;
	@Getter
	private final int numCols;
	@Getter
	private final int numBombs;
	protected int startRowPos;
	protected int startColPos;
	
	/**
	 * Creates a Builder object that contains the number of Rows, the number of Columns, the number of bombs.
	 * @param numRows the number of rows on the board.
	 * @param numCols the number of columns on the board.
	 * @param numBombs the number of bombs on the board.
	 * @throws IllegalArgumentException if any of the parameters are negative of the number of bombs exceeds the number of blocks.
	 */
	public BoardBuilder(int numRows, int numCols, int numBombs) throws IllegalArgumentException {
		if (numRows <= 0 || numCols <= 0 || numBombs <= 0) {
			throw new IllegalArgumentException("Parameters must be greater than zero. Entered numRows: " + numRows +
												"numCols: " + numCols +  " numBombs: " + numBombs + ".");
		}
		
		if (numBombs > numRows * numCols) {
			throw new IllegalArgumentException("The number of bombs cannot exceed the number of total blocks. Entered numRows: " +
												numRows + " numCols: " + numCols + " total blocks: " + (numRows * numCols) +
												" numBombs: " + numBombs + ".");			
		}
		this.numRows = numRows;
		this.numCols = numCols;
		this.numBombs = numBombs;
		startRowPos = -1;
		startColPos = -1;
	}
	
	/**
	 * Stores the starting position of the Board in a Builder object that is returned.
	 * @param row the row component of the starting position.
	 * @param col the column component of the starting position.
	 * @return a Builder object that stores the starting position of the Board.
	 * @throws IndexOutOfBoundsException if row, col is out of the bounds of the Board.
	 */
	public BoardBuilder setStartPosition(int row, int col) throws IndexOutOfBoundsException {
		if (row >= numRows || row < 0 || col < 0 || col > numCols ) {
			throw  new IndexOutOfBoundsException("Position (" + row + ", " + col +") is out of bounds.");
		}
		this.startRowPos = row;
		this.startColPos = col;
		return this;
	}
	
	/**
	 * Builds a Board object from the information stored in the Builder Object.
	 * @return a Board object created from the information stored in the Builder Object.
	 */
	public abstract BoardModel build();
}
