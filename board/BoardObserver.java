package board;

/**
 * An interface for BoardObservers that update whenever a BlockState event is emitted.
 * @author denzelhall
 *
 */
public interface BoardObserver {
	
	/**
	 * Performs an update operation based on the new BlockState;
	 * @param state a container that contains all information needed to represent the block state.
	 */
	public void update(BlockState state);
}
