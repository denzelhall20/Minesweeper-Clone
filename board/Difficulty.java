package board;

public enum Difficulty {
	BEGINNER(9, 9, 10),
	INTERMEDIATE(16, 16, 40),
	EXPERT(16, 30, 99);
	
	private int numRows;
	private int numCols;
	private int numBombs;
	
	private Difficulty(int numRows, int numCols, int numBombs) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.numBombs = numBombs;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
}
