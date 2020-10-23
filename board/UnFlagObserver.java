package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the UNFLAG BoardEvent.
 * @author denzelhall
 *
 */
public class UnFlagObserver extends BlockStateObserver {

	public UnFlagObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.UNFLAG) {
			boardView.onUnFlag(state);
		}
	}

}
