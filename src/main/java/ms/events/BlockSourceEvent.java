package ms.events;

import org.springframework.context.ApplicationEvent;

import ms.model.Block;

public class BlockSourceEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6828420430793382378L;

	public BlockSourceEvent(Block source) {
		super(source);
	}
	
	public Block getBlock() {
		return (Block) getSource();
	}

}
