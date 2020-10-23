package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the BOMB_CHOSEN BoardEvent.
 * @author denzelhall
 *
 */
public class BombChosenObserver extends BlockStateObserver {

	public BombChosenObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.BOMB_CHOSEN) {
			boardView.onBombChosen(state);
		}
	}

}
