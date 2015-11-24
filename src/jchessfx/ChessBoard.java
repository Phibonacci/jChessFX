package jchessfx;

import javafx.scene.layout.Pane;

public class ChessBoard extends Pane {
	public static final int BOARD_WIDTH  = 8;
	public static final int BOARD_HEIGHT = 8;
	
	private double cellWidth;
	private double cellHeight;
	private Piece[][]  board;
	private Square[][] squares;

	public ChessBoard() {
		board   = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
		squares = new Square[BOARD_WIDTH][BOARD_HEIGHT];
		for(int i = 0; i < BOARD_WIDTH; i++) {
			for(int j = 0; j < BOARD_HEIGHT; j++) {
				board[i][j]  = null;
				squares[i][j] = new Square(((i + j) % 2 == 0 ? Piece.WHITE : Piece.BLACK));
				getChildren().add(squares[i][j]);
			}
		}
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		cellWidth  = width  / BOARD_WIDTH;
		cellHeight = height / BOARD_HEIGHT;

//		background.relocate(0, menu_height);
//		background.setWidth(width);
//		background.setHeight(height - menu_height * 2);

		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				squares[i][j].resize(cellWidth, cellHeight);
				squares[i][j].relocate(i * cellWidth, j * cellHeight);

			}
		}
	}
	
	//resize method
	
	//reset game method
	
	//select piece method
	
	//move piece method
	
	//private fields
}
