package ms.model;

import ms.events.Difficulty;

/**
 * An interface for the creation of a BoardInteractionService based on a subclass of the BoardBuilder abstract class.
 * @author denzelhall
 *
 */
public interface BoardInteractionFactory {
	
	public BoardInteractionService getService(Difficulty difficulty);
	
	public BoardInteractionService getService(int numRows, int numCols, int numBombs);
}
