package fx;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A JavaFX UI that represents the Board.
 * @author denzelhall
 *
 */
public class BoardPane extends GridPane{
	private StackPane[][] board;
	private final double BLOCK_SIZE;
	private final int numRows;
	private final int numCols;
	private static final Color[] fills = {Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON, Color.TURQUOISE, Color.BLACK, Color.GRAY};
	
	/**
	 * Returns a BoardPane object with a given number of rows, columns, and block size;
	 * @param numRows the number of rows on the board.
	 * @param numCols the number of columns on the board.
	 * @param blockSize the size of each individual block.
	 */
	public BoardPane(int numRows, int numCols, double blockSize) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
		this.BLOCK_SIZE = blockSize;
		
		this.setHgap(-2);
		this.setVgap(-3);
		board = new StackPane[numRows][numCols];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				board[row][col] = getBlock(row, col);
				this.add(board[row][col], col, row);
			}
		}
	}
	
	private StackPane getBlock(int row, int col) {
		StackPane block = new StackPane();
		block.setPrefSize(BLOCK_SIZE, BLOCK_SIZE);
		
		Rectangle lowerRec = getRectangle(Color.GRAY,
										  Color.SILVER,
										  block.getPrefWidth());
		lowerRec.setStrokeWidth(2);
		
		Button upperRec =  new Button();
		upperRec.getStylesheets().add(getClass().getResource("/block.css").toExternalForm());
		
		//Revents resizing.
		upperRec.setMinSize(block.getPrefWidth(), block.getPrefHeight());
		upperRec.setPrefSize(block.getPrefWidth(), block.getPrefHeight());
		upperRec.setMaxSize(block.getPrefWidth(), block.getPrefHeight());

		block.getChildren().addAll(lowerRec, upperRec);
		return block;
	}
	
	/**
	 * Returns a Rectangle object with a given stroke, fill, and size.
	 * @param stroke the stroke of the Rectangle object.
	 * @param fill the fill of the Rectangle object.
	 * @param size
	 * @return
	 */
	public static Rectangle getRectangle( Color stroke, Color fill, double size) {
		Rectangle rec = new Rectangle();
		rec.setWidth(size);
		rec.setHeight(size);
		rec.setStroke(stroke);
		rec.setFill(fill);
		return rec;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	/**
	 * Returns the StackPane block at position (row, col) on the board.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 * @return the StackPane block at position (row, col) on the board.
	 */
	public StackPane blockAt(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Checks if the StackPane block at position (row, col) can be disabled from user interaction.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 * @return true if the StackPane block at position (row, col) can be disabled from user interaction.
	 */
	public boolean canDisable(int row, int col) {
		//StackPane can be disabled if it has been flagged (getChildren().size() > 2)
		//or it has not been flagged or revealed (getChildren().size() == 2 && getChildren().get(1) instanceof Button).
		return board[row][col].getChildren().size() >= 2 && board[row][col].getChildren().get(1) instanceof Button;
	}
	
	/**
	 * Checks if the StackPane block at position (row, col) contains only 2 children with a Button as the top child.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 * @return true if the StackPane block at position (row, col) contains only 2 children with a Button as the top child.
	 */
	public boolean hasForeground(int row, int col) {
		return board[row][col].getChildren().size() == 2 && board[row][col].getChildren().get(1) instanceof Button;
	}
	
	/**
	 * Adds a flag to the StackPane at position (row, col).
	 * @param row the row position of the StackPane.
	 * @param col the columnn position of the StackPane.
	 */
	public void displayFlag(int row, int col) {
		Button block = (Button) board[row][col].getChildren().get(1);
		block.getStylesheets().clear();
		block.getStylesheets().add(getClass().getResource("/staticblock.css").toExternalForm());
		board[row][col].getChildren().add(getImage("file:./assets/flagDisplay.png",  BLOCK_SIZE));
	}
	
	/**
	 * Removes the top child of the StackPane block at position (row, col) if it has a foreground.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 */
	public void removeForeground(int row, int col) {
		if (hasForeground(row, col)) {
			board[row][col].getChildren().remove(1);
		}
	}
	
	/**
	 * Adds a display of a bomb to the StackPane at position (row, col).
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 */
	public void displayBomb(int row, int col) {
		board[row][col].getChildren().add(getImage("file:./assets/bombDisplay.png",  BLOCK_SIZE));
	}
	
	/**
	 * Retrieves a ImageView from a given location at a given size.
	 * @param location the location of the image that will be on the ImageView.
	 * @param size the size of the ImageView.
	 * @return a ImageView from a given location at a given size.
	 * @throws IllegalArgumentException if the given location is invalid or unsupported.
	 */
	public static ImageView getImage(String location, double size) throws IllegalArgumentException{
		//IllegalAgrumentException is thrown in the constructor of the ImageView.
		ImageView res = new ImageView(location);
		res.setFitWidth(size);
		res.setFitHeight(size);
		return res;
	}
	
	/**
	 * Adds a display of a bomb being chosen to the StackPane block at position (row, col).
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 */
	public void displayBombChosen(int row, int col) {
		board[row][col].getChildren().add(new StackPane(getRectangle(Color.GRAY,
				   						  Color.RED,
				   						  BLOCK_SIZE),
										  getImage("file:./assets/bombDisplay.png",  BLOCK_SIZE))
										 );
	}
	
	/**
	 * Adds a display of a bad flag to the Stackpane block at position (row, col).
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 */
	public void displayBadFlag(int row, int col) {
		StackPane block = board[row][col];
		block.getChildren().add(getBadFlagDisplay(block.getLayoutX(), block.getLayoutY()));
	}

	private StackPane getBadFlagDisplay(double x, double y) {
		StackPane res = new StackPane();
		ImageView translucentBomb = getImage("file:./assets/bombDisplay.png",  BLOCK_SIZE);
		translucentBomb.setOpacity(0.25);
		Line rightDiagonal = new Line(x,
				   					  y,
				   					  x + BLOCK_SIZE,
				   					  y + BLOCK_SIZE);
		rightDiagonal.setStroke(Color.RED);
		
		Line leftDiagonal = new Line(x + BLOCK_SIZE,
				   					 y,
				   					 x,
				   					 y + BLOCK_SIZE);
		leftDiagonal.setStroke(Color.RED);
		res.getChildren().addAll(translucentBomb, rightDiagonal, leftDiagonal);
		
		return res;
	}
	
	/**
	 * Checks if the StackPane block at position (row, col) has a flag being displayed.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 * @return true if the StackPane block at position (row, col) has a flag being displayed.
	 */
	public boolean hasFlag(int row, int col) {
		return board[row][col].getChildren().size() > 2 && board[row][col].getChildren().get(2) instanceof ImageView;
	}
	
	/**
	 * Removes the flag display from the StackPane block at position (row, col) if it has one.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 */
	public void removeFlag(int row, int col) {
		if (hasFlag(row, col)) {
			Button block = (Button) board[row][col].getChildren().get(1);
			block.getStylesheets().clear();
			block.getStylesheets().add(getClass().getResource("/block.css").toExternalForm());
			board[row][col].getChildren().remove(2);
		}
	}
	
	/**
	 * Adds a display of a numerical value to the StackPane block at position (row, col) based on a given value.
	 * @param row the row position of the StackPane.
	 * @param col the column position of the StackPane.
	 * @param value the value that the display will correspond to.
	 */
	public void addValue(int row, int col, int value) {
		board[row][col].getChildren().add(getText(value, fills[value - 1]));
	}
	
	private Text getText(int value, Color fill) {
		Text res = new Text("" + value);
		res.setFont(new Font(BLOCK_SIZE * 0.6));
		res.setTextAlignment(TextAlignment.CENTER);
		res.setFill(fill);
		return res;
	}
}