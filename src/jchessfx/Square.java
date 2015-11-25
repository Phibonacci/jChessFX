package jchessfx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Group {
	private Rectangle shape;
	
	public Square(int team) {
		// make a new Ellipse and Translate, add the Translate to the Ellipse, add the Ellipse to the Group 
		shape = new Rectangle();
		getChildren().addAll(shape);
		if (team == Piece.WHITE) {
			shape.setStroke(Color.WHITE);
			shape.setFill(Color.WHITE);
		} else if (team == Piece.BLACK) {
			shape.setStroke(Color.DARKGRAY);
			shape.setFill(Color.DARKGRAY);
		}
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		shape.setWidth(width);
		shape.setHeight(height);
	}
}
