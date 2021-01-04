package ms.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import ms.model.BoardInteractionService;

@Component
public class BlocksConfiguration {
	
	private double blockSize;
	
	public BlocksConfiguration(@Value("${spring.application.ui.blockSize}") double blockSize) {
		this.blockSize = blockSize;
	}
	
	/**
	 * Returns a 2d array of StackPanes that contains only a Button whose events will call functions from a given BoardInteractionService.
	 * @param numRows the number of rows in the 2d array.
	 * @param numCols the number of columns in the 2d array.
	 * @param service the service whose functions will be called.
	 * @return a 2d array of StackPanes that contains only a Button whose events will call functions from a given BoardInteractionService.
	 */
	public StackPane[][] getBlocks(int numRows, int numCols, BoardInteractionService service) {
		StackPane[][] blocks = new StackPane[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				blocks[i][j] = getCell(service);
				
				//Need to set row and column index for later retrieval.
				GridPane.setRowIndex(blocks[i][j], i);
				GridPane.setColumnIndex(blocks[i][j], j);
			}
		}
		return blocks;
	}
	
	private StackPane getCell(BoardInteractionService service) {
		StackPane cell = new StackPane();
		setUnResizable(cell, blockSize, blockSize);
		Button button = new Button();
		setUnResizable(button, blockSize, blockSize);
		
		button.setOnMouseClicked(event -> {
			onCellSelected(cell, event, service);
		});
		
		//The following lines emulates a drag effect where the block looks like it's pressed if the mouse is down
		//and it is hovering over a block.
		button.setOnDragDetected(event -> {
			button.startFullDrag();
			
		});
		
		button.setOnMouseDragEntered(event -> {
			button.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);
		});
		
		button.setOnMouseDragExited(event -> {
			button.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
		});
		
		button.setOnMouseDragReleased(event -> {
			onCellSelected(cell, event, service);
		});
		cell.getChildren().add(button);
		return cell;
	}
	
	private void onCellSelected(StackPane cell, MouseEvent event, BoardInteractionService service) {
		Thread thread = new Thread(() -> {
			Platform.runLater(() -> {
				int row = GridPane.getRowIndex(cell);
				int col = GridPane.getColumnIndex(cell);
				Node block = cell.getChildren().get(0);
				if (event.isSecondaryButtonDown() || event.isControlDown()) {
					if (!block.getPseudoClassStates().contains(PseudoClass.getPseudoClass("flagged"))) {
						service.flag(row, col);
					} else {
						service.unFlag(row, col);
					} 
				} else {
					service.reveal(row, col);
				}	
			});
		});
		thread.start();
	}
	
	private void setUnResizable(Region region, double width, double height) {
		region.setMinSize(width, height);
		region.setPrefSize(width, height);
		region.setMaxSize(width, height);
	}
}