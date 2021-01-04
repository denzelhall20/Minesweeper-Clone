package ms.ui;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ms.events.Difficulty;

public class BoardUpdatingService {

	private GridPane board;
	private double blockSize;
	private StackPane[][] blocks;
	
	private static final Color[] fills = {Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON, Color.TURQUOISE, Color.BLACK, Color.GRAY};
		
	public BoardUpdatingService(GridPane board, double blockSize) {
		this.board = board;
		this.blockSize = blockSize;
	}

	public void reset(Difficulty difficulty, StackPane[][] blocks) {
		board.getChildren().clear();
		this.blocks = blocks;
		for (int i = 0; i < difficulty.getNumRows(); i++) {
			for (int j = 0; j < difficulty.getNumCols(); j++) {
				board.getChildren().add(blocks[i][j]);
			}
		}
	}
	
	public void reveal(int row, int col, int value) {
		StackPane display = blocks[row][col];
		Text text = new Text("" + value);
		text.setFont(new Font(blockSize * 0.6));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(fills[value - 1]);
		display.getChildren().add(text);
	}
	
	public void flag(int row, int col) {
		Node button = blocks[row][col].getChildren().get(0);
		button.getStyleClass().clear();
		button.getStyleClass().add("static-button");
		button.pseudoClassStateChanged(PseudoClass.getPseudoClass("flagged"), true);
	}
	
	public void unFlag(int row, int col) {
		Node button = blocks[row][col].getChildren().get(0);
		button.pseudoClassStateChanged(PseudoClass.getPseudoClass("flagged"), false);
		button.getStyleClass().clear();
		button.getStyleClass().add("button");
	}
	
	public void remove(int row, int col) {
		blocks[row][col].getChildren().clear();
	}
	
	public void displayBadFlag(int row, int col) {
		StackPane display = blocks[row][col];
		display.getStyleClass().clear();
		display.getStyleClass().add("bomb");
		display.pseudoClassStateChanged(PseudoClass.getPseudoClass("translucent"), true);
		
		double x = display.getLayoutX();
		double y = display.getLayoutY();
		Line rightDiagonal = new Line(x,
									  y,
									  x + blockSize,
									  y + blockSize);
		rightDiagonal.setStroke(Color.RED);
		
		Line leftDiagonal = new Line(x + blockSize,
									 y,
									 x,
									 y + blockSize);
		leftDiagonal.setStroke(Color.RED);
		
		display.getChildren().addAll(rightDiagonal, leftDiagonal);
	
	}
	
	public void displayBombChosen(int row, int col) {
		StackPane display = blocks[row][col];
		display.getStyleClass().clear();
		display.getStyleClass().add("bomb");
		display.pseudoClassStateChanged(PseudoClass.getPseudoClass("chosen"), true);
	}
	
	public void displayBomb(int row, int col) {
		StackPane display = blocks[row][col];
		display.getStyleClass().clear();
		display.getStyleClass().add("bomb");
	}
}