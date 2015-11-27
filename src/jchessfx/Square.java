package jchessfx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Group {
	private Rectangle shape;
	private int       team;
	
	public Square(int team) {
		this.shape = new Rectangle();
		this.team  = team;

		shape.setStroke(Color.rgb(32, 32, 32));
		shape.setStrokeWidth(1.0);
		
		getChildren().addAll(shape);
		setSelectable(false);
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		shape.setWidth(width);
		shape.setHeight(height);
	}
	
	public void setSelectable(boolean selectable) {
		Color color = null;
		if (team == Piece.WHITE) {
			color = selectable ? Color.LIGHTGREEN : Color.WHITE;
		} else if (team == Piece.BLACK) {
			color = selectable ? Color.FORESTGREEN : Color.DARKGRAY;
		}
		shape.setFill(color);
	}
}
