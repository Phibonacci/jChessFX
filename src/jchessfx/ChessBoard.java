package jchessfx;

import javafx.scene.layout.Pane;

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
				squares[i][j].relocate(j * cellWidth, i * cellHeight);
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
				int team = (i == 0 ? Piece.BLACK  : Piece.WHITE);
				board[i][0] = new PieceRook  (team, 0, i);
				getChildren().add(board[i][0]);
				board[i][7] = new PieceRook  (team, 7, i);
				getChildren().add(board[i][7]);
				board[i][1] = new PieceKnight(team, 1, i);
				getChildren().add(board[i][1]);
				board[i][6] = new PieceKnight(team, 6, i);
				getChildren().add(board[i][6]);
				board[i][2] = new PieceBishop(team, 2, i);
				getChildren().add(board[i][2]);
				board[i][5] = new PieceBishop(team, 5, i);
				getChildren().add(board[i][5]);
				board[i][3] = new PieceKing  (team, 3, i);
				getChildren().add(board[i][3]);
				board[i][4] = new PieceQueen (team, 4, i);
				getChildren().add(board[i][4]);
			}
			else {
				for(int j = 0; j < board[i].length; j++) {
					if (i == 1) {
						board[i][j] = new PiecePawn(Piece.BLACK, j, i);
						getChildren().add(board[i][j]);
					} else if (i == 6) {
						board[i][j] = new PiecePawn(Piece.WHITE, j, i);
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
		int indexx = (int)(x / cellWidth);
		int indexy = (int)(y / cellHeight);
		
		Piece target = board[indexy][indexx];
		if(target != null && currentPlayer == target.getTeam()) {
			target.select();
			selected = target;
		} 
		updateSelectableSquares();
	}
	
	public void placePiece(final double x, final double y) {
		int indexx = (int)(x / cellWidth);
		int indexy = (int)(y / cellHeight);
		
		Piece target = getPiece(indexx, indexy);
		if (target != null && target == selected) {
			selected.unSelect();
			selected = null;
		} else {
			if (canSelectedPieceMoveTo(indexx, indexy)) {
				// Move the piece to the new position.
				board[selected.getY()][selected.getX()] = null;
				board[indexy][indexx] = selected;
				
				// Delete the target if it was a capture.
				if (target != null) {
					getChildren().remove(target);
				}
				
				// Clear the selected piece.
				selected.setPosition(indexx, indexy);
				selected.unSelect();
				selected = null;
				
				// Swap the current player.
				currentPlayer = (currentPlayer == Piece.WHITE ? Piece.BLACK : Piece.WHITE);
			}
		}
		updateSelectableSquares();
	}

	private Piece getPiece(int x, int y) {
		return board[y][x];
	}
	
	private boolean canSelectedPieceMoveTo(int x, int y) {
		// A non-existing piece cannot move. This is not negotiable.
		if (selected == null) {
			return false;
		}
		
		// If we have a target, check if we can capture.
		// If we don't, check if we can move.
		Piece target = getPiece(x, y);
		if (target != null) {
			// We cannot capture a piece from our own team.
			if (selected.getTeam() == target.getTeam()) {
				return false;
			}
			if (!selected.canCaptureTo(x, y)) {
				return false;
			}
		} else {
			if (!selected.canMoveTo(x, y)) {
				return false;
			}
		}
		
		// Check the line of sight if the piece requires one.
		if (selected.hasLineOfSight() && !checkLineOfSight(selected.getX(), selected.getY(), x, y)) {
			return false;
		}
		
		return true;
	}
	
	private boolean checkLineOfSight(int x, int y, int toX, int toY) {
		while (x != toX || y != toY) {
			if (x > toX) {
				x--;
			} else if (x < toX) {
				x++;
			}
			if (y > toY) {
				y--;
			} else if (y < toY) {
				y++;
			}
			if ((x != toX || y != toY) && getPiece(x, y) != null) {
				return false;
			}
		}
		return true;
	}
	
	private void updateSelectableSquares() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				squares[i][j].setSelectable(canSelectedPieceMoveTo(j, i));
			}
		}
	}
}
