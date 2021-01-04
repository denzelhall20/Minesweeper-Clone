package ms.events;

import org.springframework.context.ApplicationEvent;

public class ResetEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2869774911570832050L;
	
	public ResetEvent(Difficulty source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public Difficulty getDifficulty() {
		return (Difficulty) getSource();
	}

}
