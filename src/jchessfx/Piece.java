package jchessfx;

import javafx.scene.image.Image;

//class declaration - abstract because we will not want to create a Piece object but we would
//like to specify the private fields that all pieces should have in addition to their behaviours
public abstract class Piece {
	
	//piece can be either white (1) or black (2)
	private int type;
	//image used to display the piece
	private Image image;
	
	public Piece(int type) {
		this.type = type;
	}
	
	//move method
	
	//capture method
	
}
