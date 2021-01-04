package ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair {
	private int row;
	private int col;
	
	public boolean equals(int row, int col) {
		return this.row == row && this.col == col;
	}
}
