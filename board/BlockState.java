package board;

/**
 * A BlockState data structure that stores the (row, col) Pair for a Block, it's value, and a BoardEvent associated with the block.
 * BlockState objects should follow the convention that:
 * 1. Any BoardEvent corresponding to a reveal(SAFE_REVEAL, BOMB_CHOSEN, BOMB_REVEAL, BAD_FLAG) should contain the value of the block that is being revealed.
 * 2. Any BoardEvent corresponding to a return to default(UNFLAG) should have -2 as it's value.
 * 3. Any BoardEvent corresponding with a flag (FLAG) should have -3 as it's value. 
 * @author denzelhall
 *
 */
public class BlockState {
	private BoardEvent state;
	private int value;
	private Pair pair;
	
	public BlockState(int row, int col, int value, BoardEvent state) {
		this(new Pair(row, col), value, state);
	}
	
	public BlockState(Pair pair, int value, BoardEvent state) {
		this.pair = pair;
		this.value = value;
		this.state = state;
	}
	
	public BoardEvent getState() {
		return state;
	}
	
	public Pair getPair() {
		return pair;
	}
	
	public int getRow() {
		return pair.getRow();
	}
	
	public int getCol() {
		return pair.getCol();
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return pair.toString() + " value:" + value + " state: " + state.toString();
	}

}
