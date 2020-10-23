package fx;

import java.util.ArrayList;
import java.util.List;

import board.BadFlagObserver;
import board.BlockState;
import board.BlockStateObserver;
import board.BoardController;
import board.BoardEvent;
import board.BoardView;
import board.BombChosenObserver;
import board.BombRevealObserver;
import board.FlagObserver;
import board.GameLostObserver;
import board.GameWonObserver;
import board.SafeRevealObserver;
import board.UnFlagObserver;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A class that manages the interactions between the JavaFx BoardPane with a BoardController.
 * @author denzelhall
 *
 */
public class BoardPaneController implements BoardView {
	
	private List<BlockStateObserver> observers;			//list of observers for each state that is being listened for.
	private BoardPane board;
	private BoardController controller;

	public BoardPaneController(BoardController controller, BoardPane board) {
		observers = new ArrayList<>(8);
		observers.add(new SafeRevealObserver(this));
		observers.add(new BombRevealObserver(this));
		observers.add(new FlagObserver(this));
		observers.add(new UnFlagObserver(this));
		observers.add(new BadFlagObserver(this));
		observers.add(new BombChosenObserver(this));
		observers.add(new GameWonObserver(this));
		observers.add(new GameLostObserver(this));
		
		
		this.controller = controller;
		this.controller.attach(this);
		
		this.board = board;
		
		for (int row = 0; row < board.getNumRows(); row++) {
			for (int col = 0; col < board.getNumCols(); col++) {
				StackPane block = board.blockAt(row, col);
				Button foreground = (Button) block.getChildren().get(1);
				
				//BoardPane.getRowIndex() and BoardPane.getColumnIndex() are used in the following lines
				//because row and col are not final and can't be used in the anonymous functions.
				foreground.setOnMouseClicked(event -> {
					onBlockSelected(BoardPane.getRowIndex(block), BoardPane.getColumnIndex(block), event);
				});
				
				//The following lines emulates a drag effect where the block looks like it's pressed if the mouse is down
				//and it is hovering over a block.
				foreground.setOnDragDetected(event -> {
					foreground.startFullDrag();
					
				});
				
				foreground.setOnMouseDragEntered(event -> {
					foreground.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
				});
				
				foreground.setOnMouseDragExited(event -> {
					foreground.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
				});
				
				foreground.setOnMouseDragReleased(event -> {
					onBlockSelected(BoardPane.getRowIndex(block), BoardPane.getColumnIndex(block), event);
				});
			}
		}
	}
	
	private void onBlockSelected(int row, int col, MouseEvent event) {
		Thread thread = new Thread() {
			@Override
			public void run() { 
				//Need Platform.runLater because the controller methods will update the UI.
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (event.isSecondaryButtonDown() || event.isControlDown()) {
							if (board.hasForeground(row, col)) {
								controller.flag(row, col);
							} else if (board.hasFlag(row, col)) {
								controller.unFlag(row, col);
							}
						} else {
							controller.reveal(row, col);
						}				
					}			
				});
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public BoardPane getBoardPane() {
		return board;
	}

	@Override
	public void update(BlockState state) {
		//BlockState events are propagated through the list of observers so that the correct method is called.
		for (BlockStateObserver observer: observers) {
			observer.update(state);
		}
		
	}

	@Override
	public void onSafeReveal(BlockState state) throws IllegalStateException {
		//Check for correct state
		checkState(state, BoardEvent.SAFE_REVEAL);			//throws IllegalStateException
		
		board.removeForeground(state.getRow(), state.getCol());
		if (state.getValue() > 0) {
			board.addValue(state.getRow(), state.getCol(), state.getValue());
		}
	}

	@Override
	public void onFlag(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.FLAG);					//throws IllegalStateException
		board.displayFlag(state.getRow(), state.getCol());
		
	}

	@Override
	public void onUnFlag(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.UNFLAG);				//throws IllegalStateException
		board.removeFlag(state.getRow(), state.getCol());
		
	}

	@Override
	public void onBadFlag(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.BAD_FLAG);				//throws IllegalStateException
		
		board.removeForeground(state.getRow(), state.getCol());
		board.displayBadFlag(state.getRow(), state.getCol());
		
	}

	@Override
	public void onBombChosen(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.BOMB_CHOSEN);			//throws IllegalStateException
		
		board.removeForeground(state.getRow(), state.getCol());
		board.displayBombChosen(state.getRow(), state.getCol());
		
	}

	@Override
	public void onBombReveal(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.BOMB_REVEAL);			//throws IllegalStateException

		board.removeForeground(state.getRow(), state.getCol());
		board.displayBomb(state.getRow(), state.getCol());
		
	}

	@Override
	public void onGameWon(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.GAMEWON);				//throws IllegalStateException
		removeEventHandlers();
		
	}

	@Override
	public void onGameLost(BlockState state) throws IllegalStateException {
		checkState(state, BoardEvent.GAMELOST);				//throws IllegalStateException
		removeEventHandlers();
	}
	
	private void removeEventHandlers() {
		for (int row = 0; row < board.getNumRows(); row++) {
			for (int col = 0; col < board.getNumCols(); col++) {
				if (board.canDisable(row, col)) {
					Button foreground = (Button) board.blockAt(row, col).getChildren().get(1);

					foreground.setOnMouseClicked(null);
					foreground.setOnDragDetected(null);
					foreground.setOnMouseDragEntered(null);
					foreground.setOnMouseDragExited(null);
					foreground.setOnMouseDragReleased(null);
					foreground.setDisable(true);
					foreground.setOpacity(1);				}
			}
		}
	}
	
	/**
	 * Checks if state.getState() != event.
	 * @param state the given state that is being checked.
	 * @param event the event that is being checked against.
	 * @throws IllegalStateException if state.getState() != event.
	 */
	private void checkState(BlockState state, BoardEvent event) throws IllegalStateException{
		if (state.getState() != event) {
			throw new IllegalStateException("Expected " + event + " BoardEvent but retrieved " + state.getState() + " BoardEvent instead.");
		}

	}
}