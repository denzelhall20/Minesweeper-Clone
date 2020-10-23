package board;

/**
 * A class representation for a BlockStateObserver of a BoardView that is listening for the GAMELOST BoardEvent.
 * @author denzelhall
 *
 */
public class GameLostObserver extends BlockStateObserver{

	public GameLostObserver(BoardView boardView) {
		super(boardView);
	}

	@Override
	public void update(BlockState state) {
		if (state.getState() == BoardEvent.GAMELOST) {
			boardView.onGameLost(state);
		}
		
	}

}
