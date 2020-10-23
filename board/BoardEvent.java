package board;

/**
 * A enumeration of all possible events that can happen on a board.
 * @author denzelhall
 *
 */
public enum BoardEvent {
	SAFE_REVEAL("SAFE_REVEAL"),
	BOMB_REVEAL("BOMB_REVEAL"),
	FLAG("FLAG"),
	UNFLAG("UNFLAG"),
	BAD_FLAG("BAD_FLAG"),
	BOMB_CHOSEN("BOMB_CHOSEN"),
	GAMEWON("GAMEWON"),
	GAMELOST("GAMELOST"),
	GAMESTART("GAMESTART");
	
	private String eventString;
	
	BoardEvent(String eventString) {
		this.eventString = eventString;
	}
	
	@Override
	public String toString() {
		return eventString;
	}
}
