package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import board.BoardModel;
import board.Pair;

class BoardModelTest {
	Random rand = new Random();

	@Test
	void checkArgumentsOutOfBoundsLowerBoundsTest() {	
		assertAll(
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(-1, -1, 10, 10);
					});
					assertEquals("Retrieved (-1, -1) but expected something between (0, 0) and (9, 9).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(-5, 2, 10, 10);
					});
					assertEquals("Retrieved (-5, 2) but expected something between (0, 0) and (9, 9).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(2, -10, 10, 10);
					});
					assertEquals("Retrieved (2, -10) but expected something between (0, 0) and (9, 9).", e.getMessage());
				}
		);	

	}
	
	@Test
	void checkAgrumentsOutOfBoundsUpperBoundsTest() {
		assertAll(
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(10, 10, 10, 10);
					});
					assertEquals("Retrieved (10, 10) but expected something between (0, 0) and (9, 9).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(15, 5, 10, 10);
					});
					assertEquals("Retrieved (15, 5) but expected something between (0, 0) and (9, 9).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.checkArgumentsOutOfBounds(5, 100, 10, 10);
					});
					assertEquals("Retrieved (5, 100) but expected something between (0, 0) and (9, 9).", e.getMessage());
				}
		);	
	}

	@Test
	void checkArgumentsOutOfBoundsValidTest() {
		assertDoesNotThrow(() -> BoardModel.checkArgumentsOutOfBounds(5, 3, 10, 10));
	}
	
	@Test 
	void getNeighborsTopLeftBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(0, 0, 10, 10), Arrays.asList(new Pair(0, 1), new Pair(1, 0), new Pair(1, 1)));
	}
	
	@Test 
	void getNeighborsTopRightBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(0, 9, 10, 10), Arrays.asList(new Pair(0, 8), new Pair(1, 8), new Pair(1, 9)));
	}
	
	@Test 
	void getNeighborsBottomLeftBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(9, 0, 10, 10), Arrays.asList(new Pair(8, 0), new Pair(8, 1), new Pair(9, 1)));
	}
	
	@Test 
	void getNeighborsBottomRightBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(9, 9, 10, 10), Arrays.asList(new Pair(8, 8), new Pair(8, 9), new Pair(9, 8)));
	}
	
	@Test 
	void getNeighborsTopCenterBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(0, 5, 10, 10), Arrays.asList(new Pair(0, 4),
																				  new Pair(0, 6),
																				  new Pair(1, 4),
																				  new Pair(1, 5),
																				  new Pair(1, 6)));
	}
	
	@Test 
	void getNeighborsBottomCenterBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(9, 5, 10, 10), Arrays.asList(new Pair(8, 4),
																				  new Pair(8, 5),
																				  new Pair(8, 6),
																				  new Pair(9, 4),
																				  new Pair(9, 6)));
	}
	
	@Test 
	void getNeighborsLeftCenterBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(5, 0, 10, 10), Arrays.asList(new Pair(4, 0),
																				  new Pair(4, 1),
																				  new Pair(5, 1),
																				  new Pair(6, 0),
																				  new Pair(6, 1)));
	}
	
	@Test 
	void getNeighborsRightCenterBoundsTest() {
		assertIterableEquals(BoardModel.getNeighbors(5, 9, 10, 10), Arrays.asList(new Pair(4, 8),
																				  new Pair(4, 9),
																				  new Pair(5, 8),
																				  new Pair(6, 8),
																				  new Pair(6, 9)));
	}
	
	@Test
	void getNeighborsCenterTest() {
		assertIterableEquals(BoardModel.getNeighbors(5, 5, 10, 10), Arrays.asList(new Pair(4, 4),
																				  new Pair(4, 5),
																				  new Pair(4, 6),
																				  new Pair(5, 4),
																				  new Pair(5, 6),
																				  new Pair(6, 4),
																				  new Pair(6, 5),
																				  new Pair(6, 6)));
	}
	
	@Test
	void getNeighborsBadBoundaryTest() {	
		assertAll(
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.getNeighbors(3, 4, -1, -2);
					});
					assertEquals("The given position (3, 4) does not fall in the given boundary (-1, -2).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.getNeighbors(3, 4, 10, -2);
					});
					assertEquals("The given position (3, 4) does not fall in the given boundary (10, -2).", e.getMessage());
				},
				() -> {
					Exception e = assertThrows(IllegalArgumentException.class, () -> {
						BoardModel.getNeighbors(3, 4, -1, 10);
					});
					assertEquals("The given position (3, 4) does not fall in the given boundary (-1, 10).", e.getMessage());
				}
		);
	}
	
	@Test
	void getNeighborsNegativePositionTest() {
		assertIterableEquals(BoardModel.getNeighbors(-1, 5, 10, 10), Arrays.asList(new Pair(0, 4),
																				   new Pair(0, 5),
																				   new Pair(0, 6)));
	}
	
	@Test
	void containsNullTest() {
		assertFalse(BoardModel.contains(null, 2, 2));
	}
	
	@Test
	void containsSizeLessThanOneTest() {
		assertFalse(BoardModel.contains(Arrays.asList(), 2, 2));
	}
	
	@Test
	void containsSizeEqualsOneValidTest() {
		assertTrue(BoardModel.contains(Arrays.asList(new Pair(2, 2)), 2, 2));
	}
	
	@Test 
	void containsSizeEqualsOneInvalidTest() {
		assertFalse(BoardModel.contains(Arrays.asList(new Pair(2, 2)), 3, 2));
	}
	
	@Test
	void containsSizeGreaterThanOneValidTest() {
		assertTrue(BoardModel.contains(Arrays.asList(new Pair(1, 1),
													 new Pair(2, 2),
													 new Pair(3, 4),
													 new Pair(5, 6)),
									   3, 4));
	}
	
	@Test
	void containsSizeGreaterThanOneInvalidTest() {
		assertFalse(BoardModel.contains(Arrays.asList(new Pair(1, 1),
													  new Pair(2, 2),
													  new Pair(3, 4),
													  new Pair(5, 6)),
									    4, 4));
	}
	
	@Test
	void isAdjacentValidTest() {
		assertAll(
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(0, 0))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(0, 1))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(0, 2))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(1, 0))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(1, 1))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(1, 2))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(2, 0))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(2, 1))),
			() -> assertTrue(BoardModel.isAdjacent(new Pair(1, 1), new Pair(2, 2)))
		);
		
	}
	
	@Test
	void isAdjacentInvalidTest() {
		assertFalse(BoardModel.isAdjacent(new Pair(1, 1), new Pair(9, 9)));
	}
}
