package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import board.BlockArrayBoard;
import board.BoardModel;
import board.Pair;

class BlockArrayBoardTest {
	List<Pair> visited = new ArrayList<>();

	@Test
	void builderNegativeTest() {
		assertAll(
				() -> {
					assertThrows(IllegalArgumentException.class, () -> {
						new BlockArrayBoard.Builder(-10, -10, -10);
					});
				},
				() -> {
					assertThrows(IllegalArgumentException.class, () -> {
						new BlockArrayBoard.Builder(10, -10, -10);
					});
				},
				() -> {
					assertThrows(IllegalArgumentException.class, () -> {
						new BlockArrayBoard.Builder(-10, 10, 10);
					});
				},
				() -> {
					assertThrows(IllegalArgumentException.class, () -> {
						new BlockArrayBoard.Builder(-10, -10, 10);
					});
				}
		);
	}
	
	@Test
	void builderBombsBoundaryTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BlockArrayBoard.Builder(10, 10, 101);
		});
	}
	
	@Test
	void builderStartPositionNegativeTest() {
		assertAll(
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(-1 , -1));
				},
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(-1 , 5));	
				},
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(5 , -1));
				}
		);
	}
	
	@Test
	void builderStartPositionUpperBoundaryTest() {
		assertAll(
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(10 , 10));
				},
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(15 , 5));	
				},
				() -> {
					assertThrows(IndexOutOfBoundsException.class, () -> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(5 , 15));
				}
		);
	}
	
	@Test
	void builderBuildTest() {
		assertAll(
				() -> assertDoesNotThrow(()-> new BlockArrayBoard.Builder(10, 10, 10).build()),
				() -> assertDoesNotThrow(()-> new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(5, 5).build())
		);
	}
	
	@Test
	void getValueTest() {
		assertAll(
				() -> assertThrows(IllegalStateException.class, () -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.getValue(5, 5); 
				}),
				() -> assertDoesNotThrow(() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.reveal(5, 5);
					b.getValue(5, 5); 
				})
		);
	}
	
	@Test
	void flagTest() {
		assertAll(
				() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.flag(5, 5);
					assertTrue(b.isFlagged(5, 5));
				},
				() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.reveal(5, 5);
					b.flag(5, 5);
					assertFalse(b.isFlagged(5, 5));
				},
				() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 10; j++) {
							if (!b.isGameOver()) {
								b.reveal(i, j);
							}
						}
					}
					
					b.flag(9, 9);
					assertFalse(b.isFlagged(9, 9));
				}
		);
	}
	
	@Test
	void unFlagTest() {
		assertAll(
				() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.flag(5, 5);
					b.unflag(5, 5);
					assertFalse(b.isFlagged(5, 5));
				},
				() -> {
					BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).build();
					b.reveal(5, 5);
					b.unflag(5, 5);
					assertFalse(b.isFlagged(5, 5));
				}
		);
	}
	
	@Test
	void firstRevealTest() {
		BoardModel b = new BlockArrayBoard.Builder(10, 10, 10).setStartPosition(4, 3).build();
		
		b.reveal(4, 3);
		assertAll(
				() -> assertTrue(b.isRevealed(4, 3)),
				() -> assertTrue(b.getValue(4, 3) == 0),
				() -> checkEmptyReveal(4, 3, 10, 10, b)
		);
	}
	
	void checkEmptyReveal(int row, int col, int numRows, int numCols, BoardModel board) {
		visited.add(new Pair(row, col));
		for (Pair pair: BoardModel.getNeighbors(row, col, numRows, numCols)) {
			assertAll(
					() -> assertTrue(board.isRevealed(pair.getRow(), pair.getCol())),
					() -> {
						if (board.getValue(pair.getRow(), pair.getCol()) == 0 && !visited.contains(pair)) {
							checkEmptyReveal(pair.getRow(), pair.getCol(), 10, 10, board);
						}
					}
			);
		}
	}

}
