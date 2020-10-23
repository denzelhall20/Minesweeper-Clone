package board;
/**
 * A class representing a block on a board.
 * The value of the Block indicates how many Blocks that contains bombs is adjacent to this Block.
 * If the value of the Block is -1 then it is considered to be containing a bomb.
 * @author denzelhall
 *
 */
public class Block {
	//value == 0 is an empty block.
	//value == -1 is a bomb.
	//value > 0 is adjacent to a bomb.
	private final int value;				
	private boolean revealed;
	private boolean flagged;
	
	/**
	 * Creates a Block object with a given value. If value is -1 then the Block is a bomb. 
	 * @param value the value of the block object that is being created.
	 */
	public Block(int value) {
		revealed = false;
		flagged = false;
		this.value = value;
		
	}
	
	/**
	 * Gets the value of the Block.
	 * @return the value of the Block.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Flags the Block. If the Block is revealed then nothing changes.
	 */
	public void flag() {
		flagged = true;;
	}
	
	/**
	 * Unflags the Block. If the Block is revealed then nothing changes.
	 */
	public void unflag() {
		flagged = false;
	}
	
	/**
	 * Checks if the Block is flagged.
	 * @return true if the Block is flagged.
	 */
	public boolean isFlagged() {
		return flagged;
	}
	
	/**
	 * Reveals the Block.
	 */
	public void reveal() {
		revealed = true;
	}
	
	/**
	 * Checks if the Block is revealed.
	 * @return true if the block is revealed.
	 */
	public boolean isRevealed() {
		return revealed;
	}
	
	/**
	 * Checks if the Block is a bomb. A block is a bomb if it's value == -1.
	 * @return true if the Block is a bomb(value == -1).
	 */
	public boolean isBomb() {
		return value == -1;
	}
}
