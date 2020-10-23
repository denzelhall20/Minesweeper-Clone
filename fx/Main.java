package fx;

import board.Difficulty;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{
	private static final double REC_SIZE = 31;
	private static final double PADDING = REC_SIZE * 0.3;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox(PADDING);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/borders.css");
		
		root.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		root.setStyle("-fx-background-color: silver;");
		
		BoardManagerPane manager = new BoardManagerPane(REC_SIZE);
		BoardManagerPaneController managerController = new BoardManagerPaneController(manager);
		manager.getStyleClass().add("borders");
		
		BoardPane board = managerController.getNewView(Difficulty.BEGINNER).getBoardPane();
		board.getStyleClass().add("borders");
		
		RadioMenuItem[] difficulties = {
				new RadioMenuItem(Difficulty.BEGINNER.toString()),
				new RadioMenuItem(Difficulty.INTERMEDIATE.toString()),
				new RadioMenuItem(Difficulty.EXPERT.toString()),
		};
		difficulties[0].setSelected(true);
		
		ToggleGroup difToggle = new ToggleGroup();
		difToggle.getToggles().addAll(difficulties);
		
		Menu newGame = new Menu("New Game");
		newGame.getItems().addAll(difficulties);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(newGame);
		
		if ("Mac OS X".equals(System.getProperty("os.name")))
			menuBar.useSystemMenuBarProperty().set(true);
		
		for (RadioMenuItem diff: difficulties) {
			diff.setOnAction(e -> {
				reset(root, managerController, diff, primaryStage);
			});
		}
		
		manager.setResetButtonOnMouseReleased(event -> {
			reset(root, managerController, (RadioMenuItem) difToggle.getSelectedToggle(), primaryStage);
		});
		
		root.getChildren().addAll(menuBar, manager, board);
		
		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static Difficulty getDifficulty(RadioMenuItem toggle) {
		for (Difficulty diff: Difficulty.values()) {
			if (diff.toString() == toggle.getText()) {
				return diff;
			}
		}
		return null;
	}
	
	public static void reset(VBox root, BoardManagerPaneController managerController, RadioMenuItem diff, Stage stage) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						root.getChildren().remove(2);
						BoardPane newBoard = managerController.getNewView(getDifficulty(diff)).getBoardPane();
						newBoard.getStyleClass().add("borders");
						root.getChildren().add(newBoard);
						stage.sizeToScene();
					}
				});
					
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
}
