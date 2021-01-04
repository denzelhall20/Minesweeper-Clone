package ms.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Difficulties {
	BEGINNER(new Difficulty(9, 9, 10)),
	INTERMEDIATE(new Difficulty(16, 16, 40)),
	EXPERT(new Difficulty(16, 30, 99));
	
	@Getter
	private Difficulty difficulty;
	
	public static Difficulty custom(int numRows, int numCols, int numBombs) {
		return new Difficulty(numRows, numCols, numBombs);
	}
}