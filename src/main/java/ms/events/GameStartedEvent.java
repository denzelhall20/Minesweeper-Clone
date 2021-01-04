package ms.events;

import ms.model.Block;

public class GameStartedEvent extends BlockSourceEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795452784541947494L;

	public GameStartedEvent(Block source) {
		super(source);
	}

}
