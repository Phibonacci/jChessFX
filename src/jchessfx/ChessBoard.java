package jchessfx;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ChessBoard extends Pane {
	public static final int BOARD_WIDTH  = 8;
	public static final int BOARD_HEIGHT = 8;
	
	private double cellWidth;
	private double cellHeight;
	private Piece[][]  board;
	private Square[][] squares;
	private int currentPlayer;
	private Piece selected;

	public ChessBoard() {
		selected = null;
		currentPlayer = Piece.WHITE;
		board   = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
		squares = new Square[BOARD_WIDTH][BOARD_HEIGHT];
		for(int i = 0; i < BOARD_WIDTH; i++) {
			for(int j = 0; j < BOARD_HEIGHT; j++) {
				/*
				 * A white square always have 2 odd or 2 even numbers as
				 * coordinates while a black square always have 1 odd and 1
				 * even number.
				 * Then x + y will always be even for a white square and always
				 * odd for a black square.
				 */
				squares[i][j] = new Square(((i + j) % 2 == 0 ? Piece.WHITE : Piece.BLACK));
				getChildren().add(squares[i][j]);
			}
		}
		resetGame();
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		cellWidth  = width  / BOARD_WIDTH;
		cellHeight = height / BOARD_HEIGHT;

		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				squares[i][j].resize(cellWidth, cellHeight);
				squares[i][j].relocate(i * cellWidth, j * cellHeight);
			}
		}
	}
	
	public void resetGame() {
		currentPlayer = Piece.WHITE;
		for(int i = 0; i < board.length; i++) {
			if (i == 0 || i == 7) {
				int team = (i == 0 ? Piece.WHITE  : Piece.BLACK);
				board[i][0] = new PieceKnight(team);
				board[i][7] = new PieceKnight(team);
				board[i][1] = new PieceRook  (team);
				board[i][6] = new PieceRook  (team);
				board[i][2] = new PieceBishop(team);
				board[i][5] = new PieceBishop(team);
				board[i][3] = new PieceQueen (team);
				board[i][4] = new PieceKing  (team);
			}
			for(int j = 0; j < board[i].length; j++) {
				getChildren().remove(board[i][j]);
				if (i == 1) {
					board[i][j] = new PiecePawn(Piece.WHITE);
				} else if (i == 6) {
					board[i][j] = new PiecePawn(Piece.BLACK);
				} else {
					board[i][j] = null;
				}
			}
		}
	}

	public void click(final double x, final double y) {
		if (selected == null) {
			selectPiece(x, y);
		} else {
			placePiece(x, y);
		}
	}
	
	public void selectPiece(final double x, final double y) {
		
	}
	
	public void placePiece(final double x, final double y) {

	}
	
	//resize method
	
	//reset game method
	
	//select piece method
	
	//move piece method
	
	//private fields
}
