package jchessfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Base class representing any piece of the board.
 * Contains both the graphical representation of the element and the base logic.
 * The logic should be implemented in sub classes.
 */
public abstract class Piece extends Pane {

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	public static final String IMAGES_PATH = "/assets/images/pieces/";
	
	private int   team;
	private Image image;
	
	private ImageView imageView;
	private Rectangle rectangle;
	
	private int x;
	private int y;

	private int moveCount;
	
	public Piece(int team, int x, int y, String imageName) {
		this.team = team;
		this.x = x;
		this.y = y;
		String imagePath = IMAGES_PATH + (team == WHITE ? "white" : "black") + imageName + ".png";

		try {
			image     = AssetsManager.INSTANCE.getImage(imagePath);
			imageView = new ImageView(image);
			imageView.fitWidthProperty().bind(widthProperty());
			imageView.fitHeightProperty().bind(heightProperty());
			getChildren().add(imageView);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			rectangle = new Rectangle();
			rectangle.setFill(Color.PINK);
			rectangle.widthProperty().bind(widthProperty());
			rectangle.heightProperty().bind(heightProperty());
			getChildren().add(rectangle);
		}

	}

	public boolean hasMoved() {
		return (moveCount != 0);
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void addMoveCount() {
		moveCount++;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void select() {
		getStyleClass().add("game-piece-selected");
	}
	
	public void unSelect() {
		getStyleClass().remove("game-piece-selected");
	}
	
	public abstract boolean canMoveTo(int x, int y);
	
	public boolean canCaptureTo(int x, int y) {
		return canMoveTo(x, y);
	}
	
	public boolean hasLineOfSight() {
		return true;
	}
}
