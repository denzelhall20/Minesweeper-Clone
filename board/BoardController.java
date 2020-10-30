package board;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * A class representation of a BoardController that handles events on the board before the board has been created and encapsulates interaction with the board.
 * @author denzelhall
 *
 */
public class BoardController implements BoardSubject {
	private BoardModel model;
	private BoardBuilder builder;
	private List<BoardObserver> observers;
	private Queue<Pair> flagQueue;
	
	/**
	 * Creates a BoardController that creates a Board based on the given builder.
	 * @param builder the given builder that is used to create the board.
	 */
	public BoardController(BoardBuilder builder) {
		this.builder = builder;
		model = null;
		observers = new ArrayList<>();
		flagQueue = new ArrayDeque<>();
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
		if (flagQueue.contains(new Pair(row, col))) {
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
		
		for (BoardObserver observer: observers) {
			model.attach(observer);
		}
		
		//Send an UNFLAG BoardEvent to all observers to remove their flags in preparation for the model flagging those blocks.
		for (Pair pair: flagQueue) {
			notifyObservers(new BlockState(pair, -2, BoardEvent.UNFLAG));
		}
		
		//Flag all blocks that were flagged before the start of the game.
		while (flagQueue.size() > 0) {
			Pair pair = flagQueue.poll();
			model.flag(pair.getRow(), pair.getCol());
		}
		notifyObservers(new BlockState(0, 0, -3, BoardEvent.GAMESTART));

	}
	
	/**
	 * Flags the block at a given position.
	 * @param row the row position of the block that is being flagged.
	 * @param col the column position of the block that is being flagged.
	 */
	public void flag(int row, int col) {
		//If the game is not started yet then all flags are added to a queue and all observers are notified.
		if (!isGameStarted()) {
			flagQueue.offer(new Pair(row, col));
			notifyObservers(new BlockState(row, col, -3, BoardEvent.FLAG));
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
			if (flagQueue.contains(pair)) {
				flagQueue.remove(pair);
				notifyObservers(new BlockState(row, col, -2, BoardEvent.UNFLAG));
			}
		} else {
			model.unflag(row, col);
		}
	}
	
	public int getNumRows() {
		return builder.getNumRows();
	}
	
	public int getNumCols() {
		return builder.getNumCols();
	}
	
	@Override
	public void attach(BoardObserver observer) {
		observers.add(observer);
	}
	
	@Override
	public void dettach(BoardObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(BlockState state) {
		for (BoardObserver observer: observers) {
			observer.update(state);
		}
	}
}