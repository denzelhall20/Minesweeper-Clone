package ms.model;

/**
 * A Board interface that models interactions with a Board.
 * @author denzelhall
 *
 */
public interface BoardModel {

	/**
	 * Flags the Block.
	 * @param row the row position of the Block that is being flagged.
	 * @param col the column position of the Block that is being flagged.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void flag(int row, int col) throws IllegalArgumentException;

	/**
	 * Unflags the Block.
	 * @param row the row position of the Block that is being unflagged.
	 * @param col the column position of the Block that is being unflagged.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void unFlag(int row, int col) throws IllegalArgumentException;

	/**
	 * Reveals the Block at position row, col on the board.
	 * @param row the row position of the Block that is being revealed.
	 * @param col the columb position of the Block that is being revealed.
	 * @throws IllegalArgumentException if position (row, col) is out of bounds of the board.
	 */
	void reveal(int row, int col) throws IllegalArgumentException;
}
