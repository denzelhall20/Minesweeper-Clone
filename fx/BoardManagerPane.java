package fx;

import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A JavaFX UI for the recording of information from the Board.
 * @author denzelhall
 *
 */
public class BoardManagerPane extends StackPane {
	public final double REC_SIZE;
	public final double DIGIT_SIZE;
	private final double PADDING;	
	
	private GridPane timerLayout;
	private DigitalNumber[] timer;
	
	private GridPane bombCountLayout;
	private DigitalNumber[] bombCounts;
	
	private Button resetButton;
	
	public BoardManagerPane(double REC_SIZE) {
		this.REC_SIZE = REC_SIZE;
		DIGIT_SIZE = REC_SIZE * 0.375;
		PADDING = REC_SIZE * 0.2;
		
		setMinHeight(REC_SIZE * 1.6);
		setPrefHeight(REC_SIZE * 1.6);
		setMaxHeight(REC_SIZE * 1.6);
		
		timer = new DigitalNumber[3];
		bombCounts = new DigitalNumber[3];
		StackPane timerStack = new StackPane();
		StackPane bombCountStack = new StackPane();
		for (int i = 0; i < 3; i++) {
			// {0, 1, 0} is the value that bombCounts needs to store
			//that corresponds with (i + 2) % 2 when i is {0, 1, 2}.
			bombCounts[i] = DigitalNumber.getDigitalNumber((i + 2) % 2, DIGIT_SIZE);
			timer[i] = DigitalNumber.zero(DIGIT_SIZE);
		}
		
		timerLayout = getDigitalLayout(timer, Pos.CENTER_RIGHT);
		bombCountLayout = getDigitalLayout(bombCounts, Pos.CENTER_LEFT);
		
		Rectangle timerBackground = getBackground(timerLayout, Pos.CENTER_RIGHT);
		timerStack.getChildren().addAll(timerBackground, timerLayout);
		timerStack.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		
		Rectangle bombCountBackground = getBackground(timerLayout, Pos.CENTER_LEFT);
		bombCountStack.getChildren().addAll(bombCountBackground, bombCountLayout);
		bombCountStack.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		
		
		resetButton = new Button();
		resetButton.getStylesheets().add(getClass().getResource("/staticblock.css").toExternalForm());
		resetButton.setMinSize(REC_SIZE, REC_SIZE);
		resetButton.setPrefSize(REC_SIZE, REC_SIZE);
		resetButton.setMaxSize(REC_SIZE, REC_SIZE);
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("default"), true);
		StackPane.setAlignment(resetButton, Pos.CENTER);
		
		timerStack.setPickOnBounds(false);
		bombCountStack.setPickOnBounds(false);
		
		getChildren().addAll(bombCountStack, resetButton, timerStack);
	}

	private GridPane getDigitalLayout(DigitalNumber[] arr, Pos pos) {
		GridPane layout = new GridPane();
		layout.setHgap(3);
		for (int i = 0; i < 3; i++) {
			layout.add(arr[i], i, 0);
		}
		layout.setMinSize(DIGIT_SIZE * 4, REC_SIZE * 0.9);
		layout.setPrefSize(DIGIT_SIZE * 4, REC_SIZE * 0.9);
		layout.setMaxSize(DIGIT_SIZE * 4, REC_SIZE * 0.9);
		layout.setPadding(new Insets(PADDING * 0.5, PADDING * 0.4, PADDING * 0.5, PADDING * 0.5));
		StackPane.setAlignment(layout, pos);
		
		return layout;
	}
	
	private Rectangle getBackground(GridPane box, Pos alignment) {
		//Used prefWidth and prefHeight because the GridPane will have a fixed size so it doesn't matter which size property is used.
		Rectangle background = BoardPane.getRectangle(Color.BLACK, Color.BLACK, box.getPrefWidth());
		background.setHeight(box.getPrefHeight());
		StackPane.setAlignment(background, alignment);
		return background;
	}
	
	public DigitalNumber[] getDigitalTimer() {
		return timer;
	}
	
	public DigitalNumber[] getDigitalBombCounts() {
		return bombCounts;
	}
	
	public GridPane getTimerLayout() {
		return timerLayout;
	}
	
	public GridPane getBombCountLayout() {
		return bombCountLayout;
	}
	
	public void changeResetButtonToLost() {
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("lost"), true);
	}
	
	public void changeResetButtonToDefualt() {
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("lost"), false);
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("default"), true);
	}
	
	public void setResetButtonOnMouseReleased(EventHandler<? super MouseEvent> event) {
		resetButton.setOnMouseReleased(event);
	}
}