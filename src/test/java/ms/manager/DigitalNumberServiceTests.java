package ms.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javafx.scene.layout.Pane;
import ms.ui.manager.DigitalNumber;
import ms.ui.manager.DigitalNumberService;

class DigitalNumberServiceTests {

	@ParameterizedTest
	@CsvSource({"3, 100", "4, 1234", "1, 2", "5, 98765"})
	void positiveUpdateTest(int numDigits, int value) {
		DigitalNumber[] digits = new DigitalNumber[numDigits];
		for (int i = 0; i < numDigits; i++) {
			digits[i] = DigitalNumber.empty(100);
		}
		Pane parent = new Pane();
		parent.getChildren().addAll(digits);
		DigitalNumberService service = new DigitalNumberService(digits, 100, parent);
		service.update(value);
		
		List<Integer> valDigits = new ArrayList<>(numDigits);
		
		while (value > 0) {
			valDigits.add(0, value % 10);
			value /= 10;
		}
		
		List<Integer> digitsVal = Stream.of(digits).map(DigitalNumber::getValue).collect(Collectors.toList());
		
		assertEquals(valDigits, digitsVal);
	}
	
	@ParameterizedTest
	@CsvSource({"3, -100", "4, -1234", "1, -2", "5, -98765"})
	void negativeUpdateTest(int numDigits, int value) {
		DigitalNumber[] digits = new DigitalNumber[numDigits + 1 ];
		for (int i = 0; i <= numDigits; i++) {
			digits[i] = DigitalNumber.empty(100);
		}
		Pane parent = new Pane();
		parent.getChildren().addAll(digits);
		DigitalNumberService service = new DigitalNumberService(digits, 100, parent);
		service.update(value);
		
		List<Integer> valDigits = new ArrayList<>(numDigits);
		value = Math.abs(value);
		while (value > 0) {
			valDigits.add(0, value % 10);
			value /= 10;
		}
		
		List<Integer> digitsVal = Stream.of(digits).map(DigitalNumber::getValue).collect(Collectors.toList());
		digitsVal.remove(0);
		
		assertAll(() -> assertEquals(digits[0].getValue(), -2),
				  () -> assertEquals(valDigits, digitsVal));
		
	}
	
	@Test
	void emptyParentTest() {
		DigitalNumber[] digits = new DigitalNumber[3];
		for (int i = 0; i < 3; i++) {
			digits[i] = DigitalNumber.empty(100);
		}
		
		assertThrows(IllegalArgumentException.class, () -> new DigitalNumberService(digits, 100, new Pane()));
	}
	
	@Test
	void badUpdateTest() {
		DigitalNumber[] digits = new DigitalNumber[3];
		for (int i = 0; i < 3; i++) {
			digits[i] = DigitalNumber.empty(100);
		}
		Pane parent = new Pane();
		parent.getChildren().addAll(digits);
		DigitalNumberService service = new DigitalNumberService(digits, 100, parent);
		
		assertThrows(IllegalArgumentException.class, () -> service.update(1100));
	}

}
