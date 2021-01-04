package ms.events;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

public class StageReadyEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Stage getStage() {
		return (Stage) getSource();
	}
	
	public StageReadyEvent(Stage source) {
		super(source);
	}
}