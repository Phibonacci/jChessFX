package jchessfx;

import javafx.scene.layout.Pane;

public class ChessBoard extends Pane {
	public static final int BOARD_WIDTH  = 8;
	public static final int BOARD_HEIGHT = 8;
	
	private Piece[][]  board;
	private Square[][] squares;

	public ChessBoard() {
		board   = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
		squares = new Square[BOARD_WIDTH][BOARD_HEIGHT];
	}
	
	//resize method
	
	//reset game method
	
	//select piece method
	
	//move piece method
	
	//private fields
}
