package jchessfx;

import javafx.scene.image.Image;

//class declaration - abstract because we will not want to create a Piece object but we would
//like to specify the private fields that all pieces should have in addition to their behaviours
public abstract class Piece {

	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private int team;
	private Image image;
	
	public Piece(int team) {
		this.team = team;
	}
	
	//move method
	
	//capture method
	
}
