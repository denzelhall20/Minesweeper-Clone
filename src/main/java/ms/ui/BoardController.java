package ms.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import ms.events.BlockEvent;
import ms.events.Difficulty;
import ms.events.GameOverEvent;
import ms.model.Block;
import ms.model.BoardInteractionFactory;

@Component
public class BoardController {
	
	@FXML
	private GridPane board;
	private double blockSize;
	private BoardUpdatingService boardService;
	private BoardInteractionFactory interactFact;
	private BlocksConfiguration config;

	
	public BoardController(@Value("${spring.application.ui.blockSize}") double blockSize, 
						   BoardInteractionFactory interactFact,
						   BlocksConfiguration config) {
		this.blockSize = blockSize;
		this.interactFact = interactFact;
		this.config = config;
	}
	
	public void initialize() {
		boardService = new BoardUpdatingService(board, blockSize);
	}
	
	public void setup(Difficulty difficulty) {
		boardService.reset(difficulty, 
						   config.getBlocks(
								   difficulty.getNumRows(), 
								   difficulty.getNumCols(), 
								   interactFact.getService(difficulty)));
	}
	
	@EventListener(condition="#event.isSafeReveal()")
	public void onSafeReveal(BlockEvent event) {
		Block block = event.getBlock();
		boardService.remove(block.getRow(), block.getCol());
		if (block.getVal() > 0) {
			boardService.reveal(block.getRow(), block.getCol(), block.getVal());
		}
	}
	
	@EventListener(condition="#event.isFlag()")
	public void onFlag(BlockEvent event) {
		Block block = event.getBlock();
		boardService.flag(block.getRow(), block.getCol());
	}
	
	@EventListener(condition="#event.isUnFlag()")
	public void onUnFlag(BlockEvent event) {
		Block block = event.getBlock();
		boardService.unFlag(block.getRow(), block.getCol());
	}
	
	@EventListener(condition="#event.isBadFlag()")
	public void onBadFlag(BlockEvent event) {
		Block block = event.getBlock();
		boardService.remove(block.getRow(), block.getCol());
		boardService.displayBadFlag(block.getRow(), block.getCol());
	}

	@EventListener(condition="#event.isBombChosen()")
	public void onBombChosen(BlockEvent event) {
		Block block = event.getBlock();
		boardService.remove(block.getRow(), block.getCol());
		boardService.displayBombChosen(block.getRow(), block.getCol());
	}

	@EventListener(condition="#event.isBombReveal()")
	public void onBombReveal(BlockEvent event) {
		Block block = event.getBlock();
		boardService.remove(block.getRow(), block.getCol());
		boardService.displayBomb(block.getRow(), block.getCol());
		
	}

	@EventListener(condition="event.isGameWon()")
	public void onGameWon(GameOverEvent event) {
		//TODO
	}

	@EventListener(condition="event.isGameLost()")
	public void onGameLost(GameOverEvent event) {
		//TODO
	}
}