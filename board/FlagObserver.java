package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the FLAG BoardEvent.
 * @author denzelhall
 *
 */
public class FlagObserver extends BlockStateObserver {

	public FlagObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.FLAG) {
			boardView.onFlag(state);
		}
	}

}
