package board;

/**
 * A abstract class representing a BoardObserver that calls the functions of a BoardView in its update function.
 * @author denzelhall
 *
 */
public abstract class BlockStateObserver implements BoardObserver{
	protected BoardView boardView;
	
	public BlockStateObserver(BoardView boardView) {
		this.boardView = boardView;
	}
}
