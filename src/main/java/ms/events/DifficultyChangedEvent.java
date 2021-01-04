package ms.events;

import org.springframework.context.ApplicationEvent;

public class DifficultyChangedEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1467025523102283372L;

	public DifficultyChangedEvent(Difficulty source) {
		super(source);
	}

	public Difficulty getDifficulty() {
		return (Difficulty) getSource();
	}
}
