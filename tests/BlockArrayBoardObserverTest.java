package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import board.BlockArrayBoard;
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
				() -> assertTrue(o.eventHistory.size() > 9)
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
	
}