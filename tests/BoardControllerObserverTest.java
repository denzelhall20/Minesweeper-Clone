package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import board.BlockArrayBoard;
import board.BlockState;
import board.BoardController;
import board.BoardEvent;
import board.BoardModel;

class BoardControllerObserverTest {

	@Test
	void flagTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.flag(5, 5);
		assertAll(
				() -> assertTrue(o.eventHistory.size() == 1),
				() -> assertTrue(o.eventHistory.get(0).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(0).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.FLAG)
		);
	}
	
	@Test
	void unFlagTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
	
		c.flag(5, 5);
		c.unFlag(5, 5);
		assertAll(
				() -> assertTrue(o.eventHistory.size() == 2),
				() -> assertTrue(o.eventHistory.get(0).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(0).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.FLAG),
				() -> assertTrue(o.eventHistory.get(1).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(1).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(1).getState() == BoardEvent.UNFLAG)
		);
	}
	
	@Test
	void unFlagInvalidTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.unFlag(5, 5);
		assertTrue(o.eventHistory.size() == 0);
	} 
	
	@Test
	void gameStartTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.reveal(5, 5);
		assertAll(
				() -> assertTrue(o.eventHistory.size() >= 9),
				() -> assertTrue(o.eventHistory.get(0).getRow() == 0),
				() -> assertTrue(o.eventHistory.get(0).getCol() == 0),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.GAMESTART),
				() -> assertTrue(o.eventHistory.get(1).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(1).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(1).getState() == BoardEvent.SAFE_REVEAL)
		);
	}
	
	@Test
	void flagThenRevealTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.flag(5, 5);
		c.reveal(5, 5);
		assertAll(
				() -> assertTrue(o.eventHistory.size() == 1),
				() -> assertTrue(o.eventHistory.get(0).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(0).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.FLAG)
		);
	}
	
	@Test 
	void flagBeforeAfterRevealTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.flag(2, 2);
		c.reveal(5, 5);
		
		boolean firstFlag = false;
		boolean unFlag = false;
		boolean secondFlag = false;
		
		for (BlockState event: o.eventHistory) {
			if (event.getState() == BoardEvent.FLAG && !unFlag) {
				firstFlag = true;
			}
			if (event.getState() == BoardEvent.UNFLAG) {
				unFlag = true;
			}
			if (event.getState() == BoardEvent.FLAG && unFlag) {
				secondFlag = true;
			}
		}
		
		assertTrue(firstFlag && unFlag && secondFlag);
	}
	
	@Test
	void revealThenFlagTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		c.reveal(5, 5);
		c.flag(5, 5);
		assertFalse(o.eventHistory.contains(new BlockState(5, 5, -3, BoardEvent.FLAG)));
	}
	
	@Test
	void endGameTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		TestBoardObserver o = new TestBoardObserver();
		c.attach(o);
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				c.reveal(i, j);
			}
		}
		
		boolean hasBombChosen = false;
		boolean hasBombReveal = false;
		boolean hasGameLost = false;
		
		for (BlockState event: o.eventHistory) {
			if (event.getState() == BoardEvent.BOMB_CHOSEN) { 
				hasBombChosen = true;
			}
			if (event.getState() == BoardEvent.BOMB_REVEAL) { 
				hasBombReveal = true;
			}
			if (event.getState() == BoardEvent.GAMELOST) { 
				hasGameLost = true;
			}
		}
		
		assertTrue(hasBombChosen);
		assertTrue(hasBombReveal);
		assertTrue(hasGameLost);
	}

}
