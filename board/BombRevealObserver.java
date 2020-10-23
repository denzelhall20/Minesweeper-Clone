package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the BOMB_REVEAL BoardEvent.
 * @author denzelhall
 *
 */
public class BombRevealObserver extends BlockStateObserver {

	public BombRevealObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.BOMB_REVEAL) {
			boardView.onBombReveal(state);
		}
	}

}
