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
				if (board[i][j] != null) {
					board[i][j].resize(cellWidth, cellHeight);
					board[i][j].relocate(j * cellWidth, i * cellHeight);
				}
			}
		}
	}
	
	public void resetGame() {
		currentPlayer = Piece.WHITE;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					getChildren().remove(board[i][j]);
					board[i][j] = null;
				}
			}
		}
		for(int i = 0; i < board.length; i++) {
			if (i == 0 || i == 7) {
				int team = (i == 0 ? Piece.WHITE  : Piece.BLACK);
				board[i][0] = new PieceRook  (team);
				getChildren().add(board[i][0]);
				board[i][7] = new PieceRook  (team);
				getChildren().add(board[i][7]);
				board[i][1] = new PieceKnight(team);
				getChildren().add(board[i][1]);
				board[i][6] = new PieceKnight(team);
				getChildren().add(board[i][6]);
				board[i][2] = new PieceBishop(team);
				getChildren().add(board[i][2]);
				board[i][5] = new PieceBishop(team);
				getChildren().add(board[i][5]);
				board[i][3] = new PieceKing  (team);
				getChildren().add(board[i][3]);
				board[i][4] = new PieceQueen (team);
				getChildren().add(board[i][4]);
			}
			else {
				for(int j = 0; j < board[i].length; j++) {
					if (i == 1) {
						board[i][j] = new PiecePawn(Piece.WHITE);
						getChildren().add(board[i][j]);
					} else if (i == 6) {
						board[i][j] = new PiecePawn(Piece.BLACK);
						getChildren().add(board[i][j]);
					} else {
						board[i][j] = null;
					}
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
