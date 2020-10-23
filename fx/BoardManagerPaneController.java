package fx;

import board.BlockArrayBoard;
import board.BlockState;
import board.BoardController;
import board.BoardObserver;
import board.Difficulty;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * A class that observers the changes of a Board and updates a JavaFX BoardManagerPane accordingly.
 * @author denzelhall
 *
 */
public class BoardManagerPaneController implements BoardObserver {
	private final double REC_SIZE;
	
	private BoardManagerPane pane;
	
	private SimpleIntegerProperty bombsRemaining;
	private SimpleIntegerProperty time;
	private Timeline timeLine;
	
	private BoardController controller;
	
	public BoardManagerPaneController(BoardManagerPane pane) {
		REC_SIZE = pane.REC_SIZE;
		this.pane = pane;
		
		bombsRemaining = new SimpleIntegerProperty();
		time = new SimpleIntegerProperty();
		
		timeLine = new Timeline(
				new KeyFrame(Duration.seconds(999),
						new KeyValue(time, 999, Interpolator.LINEAR))
				);
		timeLine.setCycleCount(1);
		
		time.addListener((obs, oldVal, newVal) -> {
			updateTimer(newVal.intValue());
		});
		
		bombsRemaining.addListener((obs, oldVal, newVal) -> {
			int newBombCount = newVal.intValue();
			//Need to cap bombCount at -99.
			if (newBombCount <= -99) {
				return;
			}
			updateBombCount(newBombCount);
		});
	}
	
	private void updateTimer(int newTime) {
		DigitalNumber[] timer = pane.getDigitalTimer();
		GridPane timerLayout = pane.getTimerLayout();
		for (int i = 2; i >= 0; newTime /= 10, i--) {
			if (timer[i].getValue() != newTime % 10) {
				timerLayout.getChildren().remove(timer[i]);
				timer[i] = DigitalNumber.getDigitalNumber(newTime % 10, pane.DIGIT_SIZE);
				timerLayout.add(timer[i], i, 0);
			}
		}
	}
	
	private void updateBombCount(int newBombCount) {
		DigitalNumber[] bombCounts = pane.getDigitalBombCounts();
		GridPane bombCountLayout = pane.getBombCountLayout();
		int endPos = 0;
		
		if (newBombCount < 0) {
			endPos = 1;
			newBombCount = Math.abs(newBombCount);
			//This stops the updating of the first DigitalNumber if the newBombCount is still negative
			if (bombCounts[0].getValue() > -1) {
				bombCountLayout.getChildren().remove(bombCounts[0]);
				bombCounts[0] = DigitalNumber.minusSign(pane.DIGIT_SIZE);
				bombCountLayout.add(bombCounts[0], 0, 0);
			}
		}
		
		for (int i = 2; i >= endPos; newBombCount /= 10, i--) {
			if (bombCounts[i].getValue() != newBombCount % 10) {
				bombCountLayout.getChildren().remove(bombCounts[i]);
				bombCounts[i] = DigitalNumber.getDigitalNumber(newBombCount % 10, pane.DIGIT_SIZE);
				bombCountLayout.add(bombCounts[i], i, 0);
			}
		}
	}
	
	@Override
	public void update(BlockState state) {
		switch (state.getState()) {
			case FLAG:	onFlag();
						break;
			case UNFLAG:onUnFlag();
						break;
			case GAMESTART:	onGameStart();
						break;
			case GAMEWON:onGameWon();
						break;
			case GAMELOST:onGameLost();
						break;
			default:	break;
		}
	}

	
	public BoardPaneController getNewView(Difficulty difficulty) {
		return getNewView(difficulty.getNumRows(), difficulty.getNumCols(), difficulty.getNumBombs());
	}
	
	
	public BoardPaneController getNewView(int numRows, int numCols, int numBombs) {
		reset(numBombs);
		
		controller = new BoardController(new BlockArrayBoard.Builder(numRows, numCols, numBombs));
		controller.attach(this);
		
		return new BoardPaneController(controller, new BoardPane(numRows, numCols, REC_SIZE));
		
	}
	
	private void reset(int numBombs) {
		//controller can be null if no BoardPaneController has been made yet.
		if (controller != null) {
			controller.dettach(this);
		}
		
		bombsRemaining.set(numBombs);
		timeLine.stop();
		time.set(0);
		pane.changeResetButtonToDefualt();
	}
	
	
	public void onGameWon() {
		timeLine.stop();
	}

	
	public void onGameLost() {
		timeLine.stop();
		pane.changeResetButtonToLost();
	}

	
	public void onFlag() {
		bombsRemaining.set(bombsRemaining.get() - 1);		
	}

	
	public void onUnFlag() {
		bombsRemaining.set(bombsRemaining.get() + 1);
	}

	public void onGameStart() {
		timeLine.play();
	}
}