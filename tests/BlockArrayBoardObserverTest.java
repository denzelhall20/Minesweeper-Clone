package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import board.BlockArrayBoard;
import board.BlockState;
import board.BoardEvent;
import board.BoardModel;

class BlockArrayBoardObserverTest {

	@Test
	void flagEventTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		b.flag(5, 5);
		assertAll(
				() -> assertTrue(o.eventHistory.size() == 1),
				() -> assertTrue(o.eventHistory.get(0).getRow() == 5),
				() -> assertTrue(o.eventHistory.get(0).getCol() == 5),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.FLAG)
		);
	}
	
	@Test
	void unFlagTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		b.flag(5, 5);
		b.unflag(5, 5);
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
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		b.unflag(5, 5);
		assertTrue(o.eventHistory.size() == 0);
	}

	@Test
	void firstRevealTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(5, 5).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		b.reveal(5, 5);
		
		assertAll(
				() -> assertTrue(o.eventHistory.get(0).getValue() == b.getValue(5, 5)),
				() -> assertTrue(o.eventHistory.size() >= 9)
		);
	}
	
	@Test
	void flagThenRevealTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		b.flag(5, 5);
		b.reveal(5, 5);
		
		assertAll(
				() -> assertTrue(o.eventHistory.size() == 1),
				() -> assertTrue(o.eventHistory.get(0).getState() == BoardEvent.FLAG)
		);
	}
	
	@Test
	void endGameTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
		TestBoardObserver o = new TestBoardObserver();
		b.attach(o);
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				b.reveal(i, j);
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
