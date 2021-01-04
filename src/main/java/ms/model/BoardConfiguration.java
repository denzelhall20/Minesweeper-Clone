package ms.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A class encapsulating all the necessary functionality a BoardModel might need.
 * @author denzelhall
 *
 */
public class BoardConfiguration {

	/**
	 * Returns an integer array representation of the board.
	 * Bombs are -1.
	 * Empty blocks are 0.
	 * @param builder the builder containing information needed for the creation of the board.
	 * @return an integer array representation of the board.
	 */
	public static int[][] getConfiguration(BoardBuilder builder){
		int[][] config = new int[builder.getNumRows()][builder.getNumCols()];
		Random rand = new Random();
		
		/* A location is invalid to place a bomb if:
		 * 1. a bomb has already been placed in that position.
		 * 2. the position is the start position.
		 * 3. the position is adjacent to the starting position.
		 */
		List<Pair> invalidLocations = new ArrayList<>();
		
		//This takes care of #2
		invalidLocations.add(new Pair(builder.startRowPos, builder.startColPos));
		
		//This takes care of #3.
		for (Pair pair: getNeighbors(builder.startRowPos, builder.startColPos, builder.getNumRows(), builder.getNumCols())) {
			invalidLocations.add(pair);
		}
		
		for (int bombsPlaced = 0; bombsPlaced < builder.getNumBombs(); bombsPlaced++) {
			Pair bombPos = invalidLocations.get(0);
			while (invalidLocations.contains(bombPos)) {
				bombPos = new Pair(rand.nextInt(builder.getNumRows()), rand.nextInt(builder.getNumCols()));
			}
			config[bombPos.getRow()][bombPos.getCol()] = -1;
			
			//this takes care of #1
			invalidLocations.add(bombPos);
			
			//Increment the value of the non-bombs that are adjacent to the newly placed bomb
			getNeighbors(bombPos.getRow(), bombPos.getCol(), builder.getNumRows(), builder.getNumCols())
				.stream()
				.filter(p -> config[p.getRow()][p.getCol()] > -1)
				.forEach(p -> config[p.getRow()][p.getCol()]++);
		}

		return config;
	}
	
	/**
	 * Gets a list of pairs that correspond with the row, col positions of the neighbors of given position.
	 * @param row the row component of the position whose neighbors are being found.
	 * @param col the column component of the position whose neighbors are being found.
	 * @param numRows the boundary for the row component of the neighbor's position.
	 * @param numCols the boundary for the column component of the neighbor's position
	 * @return a list of pairs that correspond with the row, col positions of the neighbors of a given position.
	 * @throws IllegalArgumentException if numRows and numCols are less than row and col
	 */
	public static List<Pair> getNeighbors(int row, int col, int numRows, int numCols) throws IllegalArgumentException {
		if (numRows <= row || numCols <= col) {
			throw new IllegalArgumentException("The given position (" + row + ", " + col + ") does not fall in the given boundary (" + numRows + ", " + numCols + ").");
		}
		
		return IntStream.rangeClosed(row - 1, row + 1).boxed()
						.flatMap(r -> IntStream.rangeClosed(col - 1, col + 1)
										  .mapToObj(c -> new Pair(r, c))
										  .filter(p -> p.getRow() != row || p.getCol() != col)			//the given position is does not count as a neighbor
										  .filter(p -> p.getRow() >= 0 && p.getRow() < numRows)			//the row position must be within the given bounds.
										  .filter(p -> p.getCol() >= 0 && p.getCol() < numCols))		//the column position must be within the given bounds.
						.collect(toList());
	}
	
	/**
	 * Checks if a given (row, col) position is within a given boundary and throws an exception if it is not.
	 * @param row the row component of the given position.
	 * @param col the column component of the given position.
	 * @param numRows the boundary for the row component.
	 * @param numCols the boundary for the column component.
	 * @throws IllegalArgumentException if (row, col) are not with a range of (0, 0) to (numRows - 1,  numCols - 1).
	 */
	public static void checkArgumentsOutOfBounds(int row, int col, int numRows, int numCols) throws IllegalArgumentException {
		if (row < 0 || col < 0 || row >= numRows || col >= numCols) {
			throw new IllegalArgumentException("Retrieved (" + row + ", " + col + ") but expected something between " +
												"(0, 0) and (" + (numRows - 1) + ", " + (numCols - 1) + ").");
		}
	}
}