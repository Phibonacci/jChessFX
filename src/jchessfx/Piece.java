package jchessfx;

import javafx.scene.Group;
import javafx.scene.image.Image;

public abstract class Piece extends Group {

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	public static final String IMAGES_PATH = "file://assets/images/pieces/";
	
	private int team;
	private Image image;
	
	public Piece(int team, String imageName) {
		this.team = team;
		
		String imagePath = IMAGES_PATH + (team == WHITE ? "white" : "black") + imageName + ".png";
		this.image = AssetsManager.INSTANCE.getImage(imagePath);
	}
	
	//move method
	
	//capture method
	
}
