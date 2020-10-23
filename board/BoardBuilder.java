package board;

/**
 * An abstract class for the creation of a Board object before or after the specification of a starting position.
 * @author denzelhall
 *
 */
public abstract class BoardBuilder {
	private final int numRows;
	private final int numCols;
	private final int numBombs;
	protected int startRowPos;
	protected int startColPos;
	
	/**
	 * Creates a BoardBuilder Object that contains the number of rows, columns, and bombs on the board.
	 * @param numRows the number of rows on the board.
	 * @param numCols the number of columns on the board.
	 * @param numBombs the number of bombs on the board.
	 */
	protected BoardBuilder(int numRows, int numCols, int numBombs) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.numBombs = numBombs;
		startRowPos = -1;
		startColPos = -1;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public int getNumBombs() {
		return numBombs;
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
