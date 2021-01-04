package ms.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ms.events.Difficulties;
import ms.events.Difficulty;
import ms.events.ResetEvent;
import ms.events.StageReadyEvent;

@Component
public class UIController {
	
	private Stage stage;
	private double padding;
	
	@FXML
	private VBox ui;
	@FXML
	private BoardController boardController;
	@FXML
	private MenuBar menu;
	
	public UIController(@Value("#{${spring.application.ui.blockSize} * 0.3}") double padding, ApplicationEventPublisher publisher) {
		this.padding = padding;
	}
	
	public void initialize() {
		ui.setSpacing(padding);
		ui.setPadding(new Insets(padding, padding, padding, padding));
		if ("Mac OS X".equals(System.getProperty("os.name")))
			menu.useSystemMenuBarProperty().set(true);
		onReset(new ResetEvent(Difficulties.BEGINNER.getDifficulty()));
		
	}
	
	@EventListener
	public void onReset(ResetEvent event) {
		Difficulty difficulty = event.getDifficulty();
		boardController.setup(difficulty);
		if (stage != null)
			stage.sizeToScene();
	}
	
	@EventListener
	public void onStageReady(StageReadyEvent event) {
		this.stage = event.getStage();
	}

}
