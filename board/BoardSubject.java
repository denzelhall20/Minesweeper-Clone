package board;

/**
 * An interface for a BoardSubject that emits BlockState events whenever the State of a Block is changed.
 * @author denzelhall
 *
 */
public interface BoardSubject {
	
	/**
	 * Attaches the given observer to the BoardSubject.
	 * @param observer the given observer that is being attached to the BoardSubject.
	 */
	public void attach(BoardObserver observer);
	
	/**
	 * Dettaches a given observer from the BoardSubject.
	 * @param observer the given observer that is being dettached from the BoardSubject.
	 */
	public void dettach(BoardObserver observer);
	
	/**
	 * Notifies all of the observers of the BoardSubject whenever the state of a Block on the Board has changed.
	 * BlockState objects should follow the convention that:
	 * 1. Any BoardEvent corresponding to a reveal(SAFE_REVEAL, BOMB_CHOSEN, BOMB_REVEAL, BAD_FLAG) should contain the value of the block that is being revealed.
	 * 2. Any BoardEvent corresponding to a return to default(UNFLAG) should have -2 as it's value.
	 * 3. Any BoardEvent corresponding with a flag (FLAG) should have -3 as it's value.
	 * @param state a container with all the necessary information for the changed block.
	 */
	public void notifyObservers(BlockState state);
}
