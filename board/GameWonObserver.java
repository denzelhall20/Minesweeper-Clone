package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the GAMEWON BoardEvent.
 * @author denzelhall
 *
 */
public class GameWonObserver extends BlockStateObserver{

	public GameWonObserver(BoardView boardView) {
		super(boardView);
	}
	
	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.GAMEWON) {
			boardView.onGameWon(state);
		}
	}

}
