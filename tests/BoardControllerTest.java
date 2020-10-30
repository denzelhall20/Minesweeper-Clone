package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import board.BlockArrayBoard;
import board.BoardController;

class BoardControllerTest {

	@Test
	void flagTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		c.flag(5, 5);
		assertFalse(c.isGameStarted());
	}
	
	@Test
	void unFlagTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		assertAll(
				() -> {
					c.unFlag(5, 5);
					assertFalse(c.isGameStarted());
				},
				() -> {
					c.flag(5, 5);
					c.unFlag(5, 5);
					assertFalse(c.isGameStarted());
				}
		);
	}
	
	@Test
	void gameStartTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		c.reveal(5, 5);
		assertTrue(c.isGameStarted());
	}
	
	@Test
	void flagThenRevealTest() {
		BoardController c = new BoardController(new BlockArrayBoard.Builder(10, 10, 10));
		c.flag(5, 5);
		c.reveal(5, 5);
		assertFalse(c.isGameStarted());
	}

}
