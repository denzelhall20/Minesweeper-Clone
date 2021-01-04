package ms.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Difficulty {
	
	@Getter
	private final int numRows;
	@Getter
	private final int numCols;
	@Getter
	private final int numBombs;
}
