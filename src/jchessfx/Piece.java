package jchessfx;

import java.io.FileInputStream;
import java.nio.file.Paths;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Piece extends Group {

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	public static final String IMAGES_PATH = Paths.get("./assets/images/pieces/").toAbsolutePath().normalize().toString();
	
	private int   team;
	private Image image;
	
	private ImageView imageView;
	private Rectangle rectangle;
	
	private int x;
	private int y;
	
	public Piece(int team, int x, int y, String imageName) {
		this.team = team;
		this.x = x;
		this.y = y;
		String imagePath = IMAGES_PATH + "/" + (team == WHITE ? "white" : "black") + imageName + ".png";

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
	
	private void setBrightness(double brightness) {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(brightness);
		if (image == null) {
			rectangle.setEffect(effect);
		} else {
			imageView.setEffect(effect);
		}
	}
	
	public void select() {
		if (team == Piece.WHITE) {
			setBrightness(-0.5);
		} else {
			setBrightness(0.25);
		}
	}
	
	public void unSelect() {
		setBrightness(0);
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
	//move method
	
	//capture method
	
}
