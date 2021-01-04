package ms.model;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ms.events.Difficulty;

@Component
public class BlockArrayBoardInteraction implements BoardInteractionFactory {
	private ApplicationContext ac;
	
	public BlockArrayBoardInteraction(ApplicationContext ac) {
		this.ac = ac;
	}
	
	@Override
	public BoardInteractionService getService(Difficulty difficulty) {
		return getService(difficulty.getNumRows(), difficulty.getNumCols(), difficulty.getNumBombs());
	}

	@Override
	public BoardInteractionService getService(int numRows, int numCols, int numBombs) {
		return ac.getBean(BoardInteractionService.class, ac.getBean(BlockArrayBoard.Builder.class, numRows, numCols, numBombs));
	}

}
