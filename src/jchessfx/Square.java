package jchessfx;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * Just a white or black square. Used to fill the game board background.
 * You will not find any color here, everything is done using CSS.
 */
public class Square extends Group {
	private Rectangle shape;
	private int       team;
	
	public Square(int team) {
		this.shape = new Rectangle();
		this.team  = team;

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
		String style = "game-square";
		if (selectable) {
			style += "-selectable";
		}
		style += (team == Piece.WHITE ? "-white" : "-black");
		shape.getStyleClass().clear();
		shape.getStyleClass().add(style);
	}
}
