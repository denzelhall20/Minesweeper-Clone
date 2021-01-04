package ms.ui.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import ms.events.BlockEvent;
import ms.events.Difficulties;
import ms.events.Difficulty;
import ms.events.DifficultyChangedEvent;
import ms.events.GameOverEvent;
import ms.events.GameStartedEvent;
import ms.events.ResetEvent;

@Component
public class ManagerController {
	private ApplicationEventPublisher publisher;
	private double blockSize;
	private double height;
	private double digitSize;
	private double padding;
	private DigitalNumber[] bombCountDisplay;
	private DigitalNumber[] timer;
	private DigitalNumberService bombCountService;
	private DigitalNumberService timerService;
	
	private IntegerProperty bombCount;
	private IntegerProperty time;
	private Timeline timeline;
	private Difficulty difficulty;
	
	@FXML
	private BorderPane manager;
	@FXML
	private Button resetButton;
	@FXML
	private HBox bombCountBox;
	@FXML
	private HBox timerBox;
	
	public ManagerController(@Value("${spring.application.ui.blockSize}") double blockSize, ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		this.height = blockSize * 1.6;
		this.blockSize = blockSize;
		this.digitSize = blockSize * 0.35;
		this.padding = blockSize * 0.2;
		bombCountDisplay = new DigitalNumber[3];
		timer = new DigitalNumber[3];
		for (int i = 0; i < 3; i++) {
			timer[i] = DigitalNumber.zero(digitSize);
			bombCountDisplay[i] = DigitalNumber.getDigitalNumber((i + 2) % 2, digitSize);
		}
		this.difficulty = Difficulties.BEGINNER.getDifficulty();
	}

	public void initialize() {
		manager.setMinHeight(height);
		manager.setPrefHeight(height);
		manager.setMaxHeight(height);
		manager.setPadding(new Insets(padding, padding, padding, padding));
		
		resetButton.setMinSize(blockSize, blockSize);
		resetButton.setPrefSize(blockSize, blockSize);
		resetButton.setMaxSize(blockSize, blockSize);
		
		double spacing = digitSize * 0.25;
		bombCountBox.setSpacing(spacing);
		timerBox.setSpacing(spacing);
		
		bombCountBox.setPadding(new Insets(spacing, spacing, spacing, spacing));
		timerBox.setPadding(new Insets(spacing, spacing, spacing, spacing));
		bombCountBox.getChildren().addAll(bombCountDisplay);
		timerBox.getChildren().addAll(timer);
		
		bombCountService = new DigitalNumberService(bombCountDisplay, digitSize, bombCountBox);
		timerService = new DigitalNumberService(timer, digitSize, timerBox);
		
		time = new SimpleIntegerProperty();
		bombCount = new SimpleIntegerProperty(10);
		timeline = new Timeline(
					new KeyFrame(Duration.seconds(999),
							new KeyValue(time, 999, Interpolator.LINEAR))
				);
		timeline.setCycleCount(1);
		
		time.addListener((obs, oldVal, newVal) -> {
			timerService.update(newVal.intValue());
		});	
		
		bombCount.addListener((obs, oldVal, newVal) -> {
			if (newVal.intValue() >= -99)
				bombCountService.update(newVal.intValue());
		});	
	}
	
	@EventListener
	public void onGameOver(GameOverEvent event) {
		timeline.stop();
	}
	
	@EventListener(condition="#event.isGameLost()")
	public void onGameLost(GameOverEvent event) {
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("lost"), true);
	}
	
	@EventListener
	public void onGameStart(GameStartedEvent event) {
		timeline.play();
	}
	
	@EventListener(condition="#event.isFlag()") 
	public void onFlag(BlockEvent event) {
		bombCount.setValue(bombCount.getValue() - 1);
	}
	
	@EventListener(condition="#event.isUnFlag()") 
	public void onUnFlag(BlockEvent event) {
		bombCount.setValue(bombCount.getValue() + 1);
	}
	
	@EventListener
	public void onDifficultyChanged(DifficultyChangedEvent event) {
		this.difficulty = event.getDifficulty();
		handleReset();
	}
	
	@FXML
	public void handleReset() {
		timeline.stop();
		time.set(0);
		bombCount.set(difficulty.getNumBombs());
		resetButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("lost"), false);
		publisher.publishEvent(new ResetEvent(difficulty));
	}
}