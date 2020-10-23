package board;

/**
 * A Pair data structure that contains (row, col) pairs.
 * @author denzelhall
 *
 */
public class Pair {
	private int row;
	private int col;
	
	public Pair(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			Pair p = (Pair) o;
			if (row == p.getRow() && col == p.getCol()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(int row, int col) {
		return this.row == row && this.col == col;
	}
	
	@Override
	public String toString() {
		return "row: " + row + " col: " + col;
	}

}
