package ms.events;

import lombok.Getter;
import ms.model.Block;

public class BlockEvent extends BlockSourceEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5026683629229992749L;
	@Getter
	private boolean safeReveal;
	@Getter
	private boolean bombChosen;
	@Getter
	private boolean bombReveal;
	@Getter
	private boolean flag;
	@Getter
	private boolean unFlag;
	@Getter
	private boolean badFlag;
	
	public BlockEvent(Block source, boolean bombChosen, boolean gameOver) {
		super(source);
		safeReveal = source.isRevealed() && source.getVal() >= 0;
		this.bombChosen = bombChosen;
		bombReveal = !bombChosen && !source.isFlagged() && source.isRevealed() && source.getVal() == -1;
		flag = source.isFlagged();
		unFlag = !source.isFlagged() && !source.isRevealed() && !gameOver;
		badFlag = !source.isFlagged() && gameOver;
	}
}