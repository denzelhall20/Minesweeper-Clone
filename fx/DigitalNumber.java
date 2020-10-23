package fx;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

/**
 * A class representation of a digital representation of the numbers 0 through 9 and the minus sign.
 * @author denzelhall
 *
 */
public class DigitalNumber extends Pane{
	private Path[] bars;
	private double barLength;
	private double barWidth;
	private int value;
	private static final double dimOpacity = 0.4;
	
	/**
	 * Creates a DigitalNumber object based on a given size, value, and double array containing the opacities of 
	 * each bar of the DigitalNumber object. 
	 * 
	 * The correspondence between the bars of the DigialNumber and the double array is shown below:
	 * 		   0
	 * 		_______
	 *     |	   |
	 * 6   |	   | 1
	 *     |   2   |
	 *      _______
	 *     |	   |
	 * 5   |	   | 3
	 *     |   4   |
	 *      _______
	 * 
	 * @param opacities a double array of opacities for the corresponding bar of the DigitalNumber object.
	 * @param width	the width of the DigitalNumber.
	 * @param value	the value associated with the DigtalNumber.
	 * @throws IllegalArguementException if opacities is not of length 7.
	 */
	public DigitalNumber(double[] opacities, double width, int value) throws IllegalArgumentException{
		super();
		
		if (opacities.length != 7) {
			throw new IllegalArgumentException("Excpected an array of atleast 7 doubles. Received an array of " +
											   opacities.length + " doubles.");
		}
		
		this.bars = new Path[7];
		barLength = width * 0.75;
		barWidth = width * 0.2;
		this.value = value;
		this.bars[0] = getHorizontalBar(barWidth * 0.625, barWidth * 0.5);
		this.bars[0].setStroke(Color.RED);
		this.bars[0].setFill(Color.RED);
		
		this.bars[1] = getVerticalBar(barWidth * 4.5, barWidth * 0.6);
		this.bars[1].setStroke(Color.RED);
		this.bars[1].setFill(Color.RED);
		
		this.bars[2] = getHorizontalBar(barWidth * 0.625, barWidth * 4.45);
		this.bars[2].setStroke(Color.RED);
		this.bars[2].setFill(Color.RED);
		
		this.bars[3] = getVerticalBar(barWidth * 4.5, barLength + (barWidth * 0.8));
		this.bars[3].setStroke(Color.RED);
		this.bars[3].setFill(Color.RED);
		
		this.bars[4] = getHorizontalBar(barWidth * 0.625, barWidth * 8.4);
		this.bars[4].setStroke(Color.RED);
		this.bars[4].setFill(Color.RED);
		
		this.bars[5] = getVerticalBar(barWidth * 0.5, barLength + (barWidth * 0.8));
		this.bars[5].setStroke(Color.RED);
		this.bars[5].setFill(Color.RED);
		
		this.bars[6] = getVerticalBar(barWidth * 0.5, barWidth * 0.6);
		this.bars[6].setStroke(Color.RED);
		this.bars[6].setFill(Color.RED);
		
		for (int i = 0; i < 7; i++) {
			this.bars[i].setOpacity(opacities[i]);
		}
		
		getChildren().addAll(this.bars);
		
		//Prevent resizing
		setMinSize(width, barWidth * 8.4 + barWidth * 0.8625);
		setPrefSize(width, barWidth * 8.4 + barWidth * 0.8625);
		setMaxSize(width, barWidth * 8.4 + barWidth * 0.8625);
	}
	
	private Path getHorizontalBar(double startX, double startY) {
		return new Path(new PathElement[] {
				new MoveTo(startX, startY),
				new LineTo(startX + (barLength * 0.1375), startY - (barWidth * 0.5)),
				new LineTo(startX + barLength * 0.8625, startY - barWidth * 0.5),
				new LineTo(startX + barLength, startY),
				new LineTo(startX + barLength * 0.8625, startY + barWidth * 0.5),
				new LineTo(startX + barLength * 0.1375, startY + barWidth * 0.5),
				new LineTo(startX, startY)
		});
	}
	
	private Path getVerticalBar(double startX, double startY) {
		return new Path(new PathElement[] {
				new MoveTo(startX, startY),
				new LineTo(startX - (barWidth * 0.5), startY + (barLength * 0.1375)),
				new LineTo(startX - barWidth * 0.5, startY + barLength * 0.8625),
				new LineTo(startX, startY + barLength),
				new LineTo(startX + barWidth * 0.5, startY + barLength * 0.8625),
				new LineTo(startX + barWidth * 0.5, startY + barLength * 0.1375),
				new LineTo(startX, startY)
		});
	}
	
	public int getValue() {
		return value;
	}
	
	public static DigitalNumber minusSign(double width) {
		return new DigitalNumber(new double[] {dimOpacity, dimOpacity, 1, dimOpacity, dimOpacity, dimOpacity, dimOpacity}, width, -1);
	}
	
	public static DigitalNumber empty(double width) {
		return new DigitalNumber(new double[] {dimOpacity, dimOpacity, dimOpacity, dimOpacity, dimOpacity, dimOpacity, dimOpacity}, width, -1);
	}
	
	public static DigitalNumber zero(double width) {
		return new DigitalNumber(new double[] {1, 1, dimOpacity, 1, 1, 1, 1}, width, 0);
	}
	
	public static DigitalNumber one(double width) {
		return new DigitalNumber(new double[] {dimOpacity, 1, dimOpacity, 1, dimOpacity, dimOpacity, dimOpacity}, width, 1);
	}
	
	public static DigitalNumber two(double width) {
		return new DigitalNumber(new double[] {1, 1, 1, dimOpacity, 1, 1, dimOpacity}, width, 2);
	}
	
	public static DigitalNumber three(double width) {
		return new DigitalNumber(new double[] {1, 1, 1, 1, 1, dimOpacity, dimOpacity}, width, 3);
	}
	
	public static DigitalNumber four(double width) {
		return new DigitalNumber(new double[] {dimOpacity, 1, 1, 1, dimOpacity, dimOpacity, 1}, width, 4);
	}
	
	public static DigitalNumber five(double width) {
		return new DigitalNumber(new double[] {1, dimOpacity, 1, 1, 1, dimOpacity, 1}, width, 5);
	}
	
	public static DigitalNumber six(double width) {
		return new DigitalNumber(new double[] {1, dimOpacity, 1, 1, 1, 1, 1}, width, 6);
	}
	
	public static DigitalNumber seven(double width) {
		return new DigitalNumber(new double[] {1, 1, dimOpacity, 1, dimOpacity, dimOpacity, dimOpacity}, width, 7);
	}
	
	public static DigitalNumber eight(double width) {
		return new DigitalNumber(new double[] {1, 1, 1, 1, 1, 1, 1}, width, 8);
	}
	
	public static DigitalNumber nine(double width) {
		return new DigitalNumber(new double[] {1, 1, 1, 1, 1, dimOpacity, 1}, width, 9);
	}
	
	/**
	 * Returns a DigitalNumber corresponding the the given digit that is between 0 and 9 inclusive. 
	 * @param digit the digit that the DigitalNumber corresponds with.
	 * @param width the width of the DigitalNumber.
	 * @return a DigitalNumber corresponding the the given digit that is between 0 and 9 inclusive. 
	 * @throws IllegalArgumentException if digit is not between 0 and 9 inclusive.
	 */
	public static DigitalNumber getDigitalNumber(int digit, double width) throws IllegalArgumentException{
		if (digit < 0 || digit >= 10) {
			throw new IllegalArgumentException("The digit must be a value from 0 to 9. Recieved: " + digit + " instead");
		}
		
		switch(digit) {
			case 0:	return zero(width);
			case 1: return one(width);
			case 2: return two(width);
			case 3: return three(width);
			case 4: return four(width);
			case 5: return five(width);
			case 6: return six(width);
			case 7: return seven(width);
			case 8: return eight(width);
			case 9: return nine(width);
		}
		
		return null;
	}
}
