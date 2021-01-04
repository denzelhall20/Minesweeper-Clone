package ms.model;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BoardConfigurationTests {

	@ParameterizedTest
	@CsvSource({"10, 10, 9", "16, 16, 40", "16, 30, 99", "100, 100, 2500"})
	void bombsAddedTests(int numRows, int numCols, int numBombs) {
		BoardBuilder builder = new BlockArrayBoard.Builder(numRows, numCols, numBombs)
												  .setStartPosition(numRows / 2, numCols / 2);
		int[][] config = BoardConfiguration.getConfiguration(builder);
		
		int bombCount = (int) Arrays.stream(config)
									.flatMapToInt(Arrays::stream)
									.filter(x -> x == -1)
									.count();
		
		assertEquals(bombCount, numBombs);
	}
	
	@Test
	void startPosEmptyTest() {
		BoardBuilder builder = new BlockArrayBoard.Builder(10, 10, 9)
				  								  .setStartPosition(5, 5);
		int[][] config = BoardConfiguration.getConfiguration(builder);
		
		assertEquals(0, config[5][5]);
	}
	
	@Test
	void correctValueTest() {
		BoardBuilder builder = new BlockArrayBoard.Builder(10, 10, 9)
				  								  .setStartPosition(5, 5);
		int[][] config = BoardConfiguration.getConfiguration(builder);
		
		int[][] values = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (config[i][j] == -1) {
					values[i][j] = -1;
				} else {
					for (Pair pair: BoardConfiguration.getNeighbors(i, j, 10, 10)) {
						if (config[pair.getRow()][pair.getCol()] == -1) {
							values[i][j]++;
						}
					}	
				}
			}
		}
		
		assertArrayEquals(values, config);
	}
	
	@ParameterizedTest
	@CsvSource({"0, 0, 10, 10", "9, 9, 10, 10", "5, 5, 10, 10", "0, 9, 10, 10", "9, 0, 10, 10"})
	void getNeighborsTest(int row, int col, int numRows, int numCols) {
		List<Pair> neighbors = BoardConfiguration.getNeighbors(row, col, numRows, numCols);
		
		List<Pair> expected = Arrays.asList(new Pair(row - 1, col - 1),
											new Pair(row - 1, col),
											new Pair(row - 1, col + 1),
											new Pair(row , col - 1),
											new Pair(row, col + 1),
											new Pair(row + 1, col - 1),
											new Pair(row + 1, col),
											new Pair(row + 1, col + 1));
		
		expected = expected.stream()
						   .filter(p -> p.getRow() >= 0 && p.getCol() >= 0)
						   .filter(p -> p.getRow() < numRows && p.getCol() < numCols)
						   .collect(toList());
		
		assertEquals(expected, neighbors);
	}

}
