package board;

/**
 * A class representing a BlockStateObserver that calls boardView.onBadFlag() in its update function.
 * @author denzelhall
 *
 */
public class BadFlagObserver extends BlockStateObserver {

	public BadFlagObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.BAD_FLAG) {
			boardView.onBadFlag(state);
		}
	}

}
