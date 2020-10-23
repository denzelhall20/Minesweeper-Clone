package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the SAFE_REVEAL BoardEvent.
 * @author denzelhall
 *
 */
public class SafeRevealObserver extends BlockStateObserver {
	
	public SafeRevealObserver(BoardView boardView) {
		super(boardView);
	}

	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.SAFE_REVEAL) {
			boardView.onSafeReveal(state);
		}
	}

}
