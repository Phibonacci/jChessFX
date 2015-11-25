package jchessfx;

import java.io.FileInputStream;
import java.nio.file.Paths;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Piece extends Group {

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	public static final String IMAGES_PATH = Paths.get("./assets/images/pieces/").toAbsolutePath().normalize().toString();
	
	private int   team;
	private Image image;
	
	private Canvas          canvas;
	private GraphicsContext graphicsContext;
	
	public Piece(int team, String imageName) {
		this.team = team;
		String imagePath = IMAGES_PATH + "/" + (team == WHITE ? "white" : "black") + imageName + ".png";
		canvas = new Canvas();
		graphicsContext = canvas.getGraphicsContext2D();
		try {
			this.image = AssetsManager.INSTANCE.getImage(imagePath);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			graphicsContext.setFill(Color.PINK);
		}
		getChildren().add(canvas);

	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		canvas.setWidth(width);
		canvas.setHeight(height);
		if (image == null) {
			graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		} else {
			graphicsContext.drawImage(image, 0, 0, width, height);
		}
	}
	//move method
	
	//capture method
	
}
