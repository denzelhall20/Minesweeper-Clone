package ms.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ms.events.BlockEvent;
import ms.events.GameStartedEvent;

/**
 * A class encapsulating all the interactions that happens with a BoardModel before its creation.
 * @author denzelhall
 *
 */
@Component
@Scope("prototype")
public class BoardInteractionService {

	private BoardModel model;
	private BoardBuilder builder;
	private List<Pair> flagList;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	/**
	 * Creates a BoardController that creates a Board based on the given builder.
	 * @param builder the given builder that is used to create the board.
	 */
	public BoardInteractionService (BoardBuilder builder) {
		this.builder = builder;
		model = null;
		flagList = new ArrayList<>();
	}
	
	/**
	 * Checks if that game has been started. More specifically if model != null.
	 * @return true if game has been started(model != null)
	 */
	public boolean isGameStarted() {
		return model != null;
	}
	
	/**
	 * Reveals the Block on the board at position (row, col).
	 * @param row the row position of the Block that is being revealed.
	 * @param col the column position of the Block that is being revealed.
	 */
	public void reveal(int row, int col) {
		if (flagList.contains(new Pair(row, col))) {
			return;
		}
		if (!isGameStarted()) {
			startGame(row, col);
		}
		model.reveal(row, col);
	}
	
	/**
	 * Creates the board model with a given starting location and flags any blocks that were flagged before the start of the game.
	 * @param row the row position of the starting location.
	 * @param col the column position of the starting location.
	 */
	private void startGame(int row, int col) { 
		model = builder.setStartPosition(row, col).build();
		
		//Send an UNFLAG BoardEvent to all observers to remove their flags in preparation for the model flagging those blocks.
		flagList.stream().forEach(pair -> publisher.publishEvent(
														new BlockEvent(
															new Block(pair.getRow(), pair.getCol(), -3), false, false)));
		
		//Flag all blocks that were flagged before the start of the game.
		while (flagList.size() > 0) {
			Pair pair = flagList.remove(0);
			model.flag(pair.getRow(), pair.getCol());
		}
		publisher.publishEvent(new GameStartedEvent(new Block(row, col, -3)));

	}
	
	/**
	 * Flags the block at a given position.
	 * @param row the row position of the block that is being flagged.
	 * @param col the column position of the block that is being flagged.
	 */
	public void flag(int row, int col) {
		//If the game is not started yet then all flags are added to a queue and all observers are notified.
		if (!isGameStarted()) {
			flagList.add(new Pair(row, col));
			Block block = new Block(row, col, -3);
			block.flagged = true;
			publisher.publishEvent(new BlockEvent(block, false, false));
		} else {
			model.flag(row, col);
		}
	}
	
	/**
	 * UnFlags the block at a given position.
	 * @param row the row position of the block that is being unflagged.
	 * @param col the column position of the block that is being unflagged.
	 */
	public void unFlag(int row, int col) {
		//If the game is not started yet then remove the flag from the queue if it exists and notify all observers.
		if (!isGameStarted()) {
			Pair pair = new Pair(row, col);
			if (flagList.contains(pair)) {
				flagList.remove(pair);
				//A BlockEvent with a default block represents an unflagged block.
				publisher.publishEvent(new BlockEvent(new Block(row, col, -3), false, false));
			}
		} else {
			model.unFlag(row, col);
		}
	}
}