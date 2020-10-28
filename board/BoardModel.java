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
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void flag(int row, int col) throws IllegalArgumentException;

	/**
	 * Unflags the Block. If the Block is revealed then nothing changes.
	 * @param row the row position of the Block that is being unflagged.
	 * @param col the column position of the Block that is being unflagged.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void unflag(int row, int col) throws IllegalArgumentException;

	/**
	 * Reveals the Block at position row, col on the board.
	 * If the Game is over then reveal does nothing.
	 * @param row the row position of the Block that is being revealed.
	 * @param col the columb position of the Block that is being revealed.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void reveal(int row, int col) throws IllegalArgumentException;
	
	/**
	 * Gets the value of the Block at position row, col on the board.
	 * @param row the row position of the Block whose value is being retrieved.
	 * @param col the column position of the Block whose value is being retrieved.
	 * @return the value of the Block at position row, col on the board.
	 * @throws IllegalStateException if the Block had not been revealed.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	public int getValue(int row, int col) throws IllegalStateException, IllegalArgumentException;
	
	/**
	 * Checks if the Block at position row, col is flagged.
	 * @param row the row position of the Block that is being checked.
	 * @param col the column position of the Block that is being checked.
	 * @return true if the Block is flagged.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	public boolean isFlagged(int row, int col) throws IllegalArgumentException;
	
	/**
	 * Checks if the Block at position row, col is revealed.
	 * @param row the row position of the Block that is being checked.
	 * @param col the column position of the Block that is being checked.
	 * @return true if the block is revealed.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	public boolean isRevealed(int row, int col) throws IllegalArgumentException; 
	
	
	/**
	 * Checks if the Block is a bomb. A block is a bomb if it's value == -1.
	 * @return true if the Block is a bomb(value == -1).
	 * @throws IllegalStateException if the Block has not been revealed yet.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	public boolean isBomb(int row, int col) throws IllegalStateException, IllegalArgumentException;
	
	/**
	 * Checks if the game is completed.
	 * @return true if the game is completed.
	 */
	public boolean isGameOver();
	
	/**
	 * Checks if 2 given pairs are adjacent to each other.
	 * @param pair1 the first of the given pairs.
	 * @param pair2 the second of the given pairs.
	 * @return true if the row and column components of a given pair are 1 distance away from each other.
	 */
	public static boolean isAdjacent(Pair pair1, Pair pair2) {	
		return (pair1.getRow() == pair2.getRow() - 1 || pair1.getRow() == pair2.getRow() + 1 || pair1.getRow() == pair2.getRow()) &&
				(pair1.getCol() == pair2.getCol() - 1 || pair1.getCol() == pair2.getCol() + 1 || pair1.getCol() == pair2.getCol());
	}
	
	/**
	 * Gets a list of pairs that correspond with the row, col positions of the neighbors of given position.
	 * @param row the row component of the position whose neighbors are being found.
	 * @param col the column component of the position whose neighbors are being found.
	 * @param numRows the boundary for the row component of the neighbor's position.
	 * @param numCols the boundary for the column component of the neighbor's position
	 * @return a list of pairs that correspond with the row, col positions of the neighbors of a given position.
	 * @throws IllegalArgumentException if numRows and numCols are less than row and col
	 */
	public static List<Pair> getNeighbors(int row, int col, int numRows, int numCols) throws IllegalArgumentException {
		if (numRows <= row || numCols <= col) {
			throw new IllegalArgumentException("The given position (" + row + ", " + col + ") does not fall in the given boundary (" + numRows + ", " + numCols + ").");
		}
		
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
	
	/**
	 * Checks if a given (row, col) position is within a given boundary and throws an exception if it is not.
	 * @param row the row component of the given position.
	 * @param col the column component of the given position.
	 * @param numRows the boundary for the row component.
	 * @param numCols the boundary for the column component.
	 * @throws IllegalArgumentException if (row, col) are not with a range of (0, 0) to (numRows - 1,  numCols - 1).
	 */
	public static void checkArgumentsOutOfBounds(int row, int col, int numRows, int numCols) throws IllegalArgumentException {
		if (row < 0 || col < 0 || row >= numRows || col >= numCols) {
			throw new IllegalArgumentException("Retrieved (" + row + ", " + col + ") but expected something between " +
												"(0, 0) and (" + (numRows - 1) + ", " + (numCols - 1) + ").");
		}
	}
	
	/**
	 * Checks if a list of Pairs contains a given (row, col) pair.
	 * @param list a given list of Pairs.
	 * @param row the row position that is being checked.
	 * @param col the column position that is being checked.
	 * @return true if there exists a pair p in list that satisfies (p.equals(row, col)).
	 */
	public static boolean contains(List<Pair> list, int row, int col) {
		if (list == null || list.size() < 1) {
			return false;
		}
		
		for (Pair pair: list) {
			if (pair.equals(row, col)) {
				return true;
			}
		}
		
		return false;
	}
}
