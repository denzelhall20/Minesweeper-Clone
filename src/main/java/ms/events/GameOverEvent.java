package ms.events;

import lombok.Getter;
import ms.model.Block;

public class GameOverEvent extends BlockSourceEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4283370197319120018L;
	
	@Getter
	private boolean gameWon;
	@Getter
	private boolean gameLost;

	public GameOverEvent(Block source, boolean gameWon, boolean gameLost) {
		super(source);
		this.gameWon = gameWon;
		this.gameLost = gameLost;
	}

}
