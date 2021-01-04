package ms.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Block {
	@Getter
	private final int row;	
	@Getter
	private final int col;
	@Getter
	private final int val;
	@Getter
	protected boolean flagged = false;
	@Getter
	protected boolean revealed = false;

}
