package board;

/**
 * An interface modeling the functions of the UI for a board.
 * @author denzelhall
 *
 */
public interface BoardView extends BoardObserver {
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a SAFE_REVEAL BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.SAFE_REVEAL.
	 */
	public void onSafeReveal(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a FLAG BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.FLAG.
	 */
	public void onFlag(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a UNFLAG BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.UNFLAG.
	 */
	public void onUnFlag(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a BAD_FLAG BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.BAD_FLAG.
	 */
	public void onBadFlag(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a BOMB_CHOSEN BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.BOMB_CHOSEN.
	 */
	public void onBombChosen(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a BOMB_REVEAL BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.BOMB_REVEAL.
	 */
	public void onBombReveal(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a GAMEWON BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.GAMEWON.
	 */
	public void onGameWon(BlockState state) throws IllegalStateException;
	
	/**
	 * Contains the functionality that should be executed on emission of a BlockState that contains a GAMELOST BoardEvent.
	 * @param state a container for all the necessary information about the changed block.
	 * @throws an IllegalStateException if state is not BoardEvent.GAMELOST.
	 */
	public void onGameLost(BlockState state) throws IllegalStateException;
	
}
