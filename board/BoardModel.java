package board;
import java.util.ArrayList;
import java.util.List;


/**
 * A Board interface that models interactions with a Board.
 * @author denzelhall
 *
 */
public interface BoardModel extends BoardSubject{
	

	/**
	 * Flags the Block. If the Block is revealed then nothing changes.
	 * If the Game is over then flag does nothing.
	 * @param row the row position of the Block that is being flagged.
	 * @param col the column position of the Block that is being flagged.
	 */
	void flag(int row, int col);

	/**
	 * Unflags the Block. If the Block is revealed then nothing changes.
	 * @param row the row position of the Block that is being unflagged.
	 * @param col the column position of the Block that is being unflagged.
	 */
	void unflag(int row, int col);

	/**
	 * Reveals the Block at position row, col on the board.
	 * If the Game is over then reveal does nothing.
	 * @param row the row position of the Block that is being revealed.
	 * @param col the columb position of the Block that is being revealed.
	 */
	void reveal(int row, int col);
	
	/**
	 * Gets the value of the Block at position row, col on the board.
	 * @param row the row position of the Block whose value is being retrieved.
	 * @param col the column position of the Block whose value is being retrieved.
	 * @return the value of the Block at position row, col on the board.
	 * @throws IllegalStateException if the Block had not been revealed.
	 */
	public int getValue(int row, int col) throws IllegalStateException;
	
	/**
	 * Checks if the Block at position row, col is flagged.
	 * @param row the row position of the Block that is being checked.
	 * @param col the column position of the Block that is being checked.
	 * @return true if the Block is flagged.
	 */
	public boolean isFlagged(int row, int col);
	
	/**
	 * Checks if the Block at position row, col is revealed.
	 * @param row the row position of the Block that is being checked.
	 * @param col the column position of the Block that is being checked.
	 * @return true if the block is revealed.
	 */
	public boolean isRevealed(int row, int col); 
	
	
	/**
	 * Checks if the Block is a bomb. A block is a bomb if it's value == -1.
	 * @return true if the Block is a bomb(value == -1).
	 * @throws IllegalStateException if the Block has not been revealed yet.
	 */
	public boolean isBomb(int row, int col) throws IllegalStateException;
	
	/**
	 * Checks if the game is completed.
	 * @return true if the game is completed.
	 */
	public boolean isGameOver();
	
	/*
	 * Checks if row1, col1 is adjacent to row2, col2
	 */
	public static boolean isAdjacent(Pair pair1, Pair pair2, int numRows, int numCols) {
		for (Pair adjPair: BoardModel.getNeighbors(pair2.getRow(), pair2.getCol(), numRows, numCols )) {
			if (pair1.equals(adjPair)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a list of pairs that correspond with the row, col positions of the neighbors of given position.
	 * @param row the row component of the position whose neighbors are being found.
	 * @param col the column component of the position whose neighbors are being found.
	 * @param numRows the boundary for the row component of the neighbor's position.
	 * @param numCols the boundary for the column component of the neighbor's position
	 * @return a list of pairs that correspond with the row, col positions of the neighbors of a given position.
	 */
	public static List<Pair> getNeighbors(int row, int col, int numRows, int numCols) {
		List<Pair> res = new ArrayList<>();
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			for (int colOffset = -1; colOffset <= 1; colOffset++) {
				/* Only add an integer to the result if:
				 * 1. Its position is on the board. !(adjRowPos < 0 || adjRowPos >= numRows  || adjColPos < 0 || adjColPos >= numCols)
				 * 2. Its position is not the same as the given position. !(adjRowPos == row && adjColPos == col)
				 */
				int adjRowPos = row + rowOffset;
				int adjColPos = col + colOffset;
				if (!(adjRowPos < 0 || adjRowPos >= numRows  || adjColPos < 0 || adjColPos >= numCols) &&
					!(adjRowPos == row && adjColPos == col)) {
					res.add(new Pair(adjRowPos, adjColPos));
				}
			}
		}
		return res;
	}
}
