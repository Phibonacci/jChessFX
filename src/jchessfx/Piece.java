package jchessfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
			getChildren().add(imageView);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			rectangle = new Rectangle();
			rectangle.setFill(Color.PINK);
			getChildren().add(rectangle);
		}

	}

	public boolean hasMoved() {
		if (moveCount == 0) {
			return false;
		}
		return true;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void addMoveCount() {
		moveCount++;
	}

	public void removeMoveCount() {
		moveCount--;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int value) {
		x = value;
	}
	
	public void setY(int value) {
		y = value;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void select() {
		if (imageView != null) {
			getStyleClass().add("game-piece-selected");
		}
	}
	
	public void unSelect() {
		if (imageView != null) {
			getStyleClass().remove("game-piece-selected");
		}
	}
	
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		if (image == null) {
			rectangle.setWidth(width);
			rectangle.setHeight(height);
		} else {
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);
		}
	}

	public abstract boolean canMoveTo(int x, int y);
	
	public boolean canCaptureTo(int x, int y) {
		return canMoveTo(x, y);
	}
	
	public boolean hasLineOfSight() {
		return true;
	}
}
