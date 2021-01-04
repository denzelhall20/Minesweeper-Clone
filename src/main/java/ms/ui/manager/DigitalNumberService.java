package ms.ui.manager;

import javafx.scene.layout.Pane;


public class DigitalNumberService {
	
	private DigitalNumber[] digits;
	private double size;
	private Pane parent;
	
	public DigitalNumberService(DigitalNumber[] digits, double size, Pane parent) throws IllegalArgumentException {
		for (DigitalNumber digit: digits) {
			if (!parent.getChildren().contains(digit)) {
				throw new IllegalArgumentException("The given parent must have the all of the digits as children.");
			}
		}
		this.digits = digits;
		this.parent = parent;
		this.size = size;
	}
	
	public void update(int newVal) throws IllegalArgumentException {
		if (numDigits(newVal) > digits.length) {
			throw new IllegalArgumentException("Retrieved a value with " + numDigits(newVal) + 
												"digits. But expected one with " + digits.length + "digits or less.");
		}
		int endPos = 0;
		
		if (newVal < 0) {
			endPos = 1;
			newVal = Math.abs(newVal);
			//This stops the updating of the first DigitalNumber if the newBombCount is still negative
			if (digits[0].getValue() > -1) {
				parent.getChildren().remove(digits[0]);
				digits[0] = DigitalNumber.minusSign(size);
				parent.getChildren().add(0, digits[0]);
			}
		}
		
		for (int i = digits.length - 1; i >= endPos; newVal /= 10, i--) {
			if (digits[i].getValue() != newVal % 10) {
				parent.getChildren().remove(digits[i]);
				digits[i] = DigitalNumber.getDigitalNumber(newVal % 10, size);
				parent.getChildren().add(i, digits[i]);
			}
		}
	}
	
	private int numDigits(int val) {
		int count = 0;
		if (val < 0) {
			count++;
			val = Math.abs(val);
		}
		for (; val > 0; count++, val /= 10);
		return count;
	}
}
