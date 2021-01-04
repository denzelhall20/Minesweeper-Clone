package ms.ui;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import ms.events.Difficulties;
import ms.events.Difficulty;
import ms.events.DifficultyChangedEvent;

@Component
public class MenuController {

	private ApplicationEventPublisher publisher;

	public MenuController(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	@FXML
	public void changeDifficulty(ActionEvent event) {
		RadioMenuItem source = (RadioMenuItem) event.getSource();
		publisher.publishEvent(new DifficultyChangedEvent(getDifficulty(source.getText())));
	}

	private Difficulty getDifficulty(String val) {
		switch(val) {
			case "Beginner":	return Difficulties.BEGINNER.getDifficulty();
			case "Intermediate":	return Difficulties.INTERMEDIATE.getDifficulty();
			case "Expert":	return Difficulties.EXPERT.getDifficulty();
		}
		return Difficulties.BEGINNER.getDifficulty();
	}
}
