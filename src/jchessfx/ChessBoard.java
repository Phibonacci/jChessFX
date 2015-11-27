package jchessfx;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class ChessBoard extends Pane {
	public static final int BOARD_WIDTH  = 8;
	public static final int BOARD_HEIGHT = 8;
	
	private double cellWidth;
	private double cellHeight;
	private Piece[][]  board;
	private Square[][] squares;
	private int currentPlayer;
	private Piece selected;
	
	private Piece whiteKing;
	private Piece blackKing;
	
	private static final int STATE_PLAYING   = 0;
	private static final int STATE_CHECK     = 1;
	private static final int STATE_CHECKMATE = 2;
	private static final int STATE_STALEMATE = 3;
	
	private int   winner;
	private int   gameState;

	public ChessBoard() {
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
		winner = Piece.EMPTY;
		selected = null;
		gameState = STATE_PLAYING;
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
				board[i][3] = new PieceQueen (team, 3, i);
				getChildren().add(board[i][3]);
				board[i][4] = new PieceKing  (team, 4, i);
				getChildren().add(board[i][4]);
				if (team == Piece.WHITE) {
					whiteKing = board[i][4];
				} else {
					blackKing = board[i][4];
				}
			} else {
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
		if (gameState != STATE_PLAYING && gameState != STATE_CHECK) {
			return;
		}
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
			if (isSelectedPieceAllowedToMoveTo(indexx, indexy)) {
				// Store the old position.
				final int oldPositionX = selected.getX();
				final int oldPositionY = selected.getY();

				// Move the piece to the new position.
				setPiecePosition(selected, indexx, indexy);

				// Add the animation.
				addTransitionAnimation(selected, target, oldPositionX, oldPositionY);

				// Clear the selected piece.
				selected.unSelect();
				selected = null;

				updateGameState();
			} else {
				// Clear the selected piece if we click somewhere we cannot move.
				selected.unSelect();
				selected = null;
			}
		}
		updateSelectableSquares();
	}

	/* 
	 * Return the possible winner
	 *  Piece.EMPTY
	 *  Piece.BLACK
	 *  Piece.WHITE
	 * Piece.EMPTY is set for both a running game and a stalemate
	 * Use getGameState to check the state of the game
	 */
	public int getWinner()
	{
		return winner;
	}
	
	/*
	 * Return the state of the game
	 *  Board.STATE_PLAYING
	 *  Board.STATE_CHECKMATE
	 *  Board.STATE_STALEMATE
	 */
	public int getGameSate()
	{
		return gameState;
	}
	
	private void updateGameState()
	{
		int nextPlayer  = (currentPlayer == Piece.WHITE ? Piece.BLACK : Piece.WHITE);
		boolean check   = isCheck(nextPlayer);
		boolean canMove = isTeamAllowedToMove(nextPlayer);
		
		if (check && !canMove) {
			gameState = STATE_CHECKMATE;
			winner = currentPlayer;
		} else if (!canMove) {
			gameState = STATE_STALEMATE;
		} else if (check) {
			gameState = STATE_CHECK;
			currentPlayer = nextPlayer;
		} else {
			gameState = STATE_PLAYING;
			// Swap the current player.
			currentPlayer = nextPlayer;
		}
	}
	
	private boolean isTeamAllowedToMove(int team) {
		for (int j = 0; j < BOARD_HEIGHT; j++) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				Piece target = board[j][i];
				if (target != null && target.getTeam() == team && isPieceAllowedToMove(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isPieceAllowedToMove(Piece piece) {
		for (int j = 0; j < BOARD_HEIGHT; j++) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				if (isPieceAllowedToMoveTo(piece, i, j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isCheck(int team) {
		Piece king = (team == Piece.WHITE ? whiteKing : blackKing);
		for (int j = 0; j < BOARD_HEIGHT; j++) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				Piece target = board[j][i];
				if (target != null) {
					if (canPieceMoveTo(target, king.getX(), king.getY())) {
						return true;
					}
				}
				
			}
		}
		return false;
	}
	
	private Piece getPiece(int x, int y) {
		return board[y][x];
	}
	
	private void setPiecePosition(Piece piece, int x, int y) {
		if (piece != null) {
			board[piece.getY()][piece.getX()] = null;
			piece.setPosition(x, y);
		}
		board[y][x] = piece;
	}
	
	private boolean canPieceMoveTo(Piece piece, int x, int y) {
		// A non-existing piece cannot move. This is not negotiable.
		if (piece == null) {
			return false;
		}
		
		// If we have a target, check if we can capture.
		// If we don't, check if we can move.
		Piece target = getPiece(x, y);
		if (target != null) {
			// We cannot capture a piece from our own team.
			if (piece.getTeam() == target.getTeam()) {
				return false;
			}
			if (!piece.canCaptureTo(x, y)) {
				return false;
			}
		} else {
			if (!piece.canMoveTo(x, y)) {
				return false;
			}
		}
		
		// Check the line of sight if the piece requires one.
		if (piece.hasLineOfSight() && !checkLineOfSight(piece.getX(), piece.getY(), x, y)) {
			return false;
		}
		
		return true;
	}

	private boolean isPieceAllowedToMoveTo(Piece piece, int x, int y) {
		if (canPieceMoveTo(piece, x, y)) {
			int oldX = piece.getX();
			int oldY = piece.getY();
			Piece target = getPiece(x, y);
			
			setPiecePosition(piece, x, y);
			boolean check = isCheck(piece.getTeam());
			setPiecePosition(piece, oldX, oldY);
			setPiecePosition(target, x, y);
			return !check;
		}
		return false;
	}
	
	private boolean isSelectedPieceAllowedToMoveTo(int x, int y) {
		return isPieceAllowedToMoveTo(selected, x, y);
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
				squares[i][j].setSelectable(isSelectedPieceAllowedToMoveTo(j, i));
			}
		}
	}
	
	private void addTransitionAnimation(Piece piece, Piece target, int fromX, int fromY) {
		// Animations do not use the same origin as the relocate method, so we have to calculate it.
		double originX = cellWidth * 0.5;
		double originY = cellHeight * 0.5;
		
		// Calculate the distance out piece have to run through.
		double deltaCellX = (fromX - piece.getX());
		double deltaCellY = (fromY - piece.getY());
		double deltaX = deltaCellX * cellWidth;
		double deltaY = deltaCellY * cellHeight;
		
		// Calculate the duration of the animation, based on the distance.
		double ms = Math.sqrt(deltaCellX * deltaCellX + deltaCellY * deltaCellY) * 200.0;
		Duration duration = Duration.millis(ms);
		
		// Create the path the animation will follow.
		Path path = new Path();
		path.getElements().add(new MoveTo(originX + deltaX, originY + deltaY));
		path.getElements().add(new LineTo(originX, originY));
		
		// Create and play the transition animation!
		PathTransition pathTransition = new PathTransition(duration, path, piece);
		pathTransition.play();
		
		// Run the fade-out animation as soon as we hit the target piece.
		if (target != null) {
	        runLater(ms - 150, new Runnable() {
	        	@Override
				public void run() {
					ChessBoard.this.addFadeoutAnimation(target);
				}
			});		
		}
	}
	
	private void addFadeoutAnimation(Piece piece) {
		// Create and play the fade-out animation to slowly make the piece invisible.
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), piece);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		
		// Rotating a piece before destroying it looks cool.
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), piece);
	    rotateTransition.setByAngle(360f);
	    
        // Combine both animations into one and execute it!
		ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(fadeTransition, rotateTransition );
        parallelTransition.play();
        
        // Delete the piece from the board as soon as the opacity is zero.
        runLater(500.0, new Runnable() {
        	@Override
			public void run() {
				ChessBoard.this.getChildren().remove(piece);
			}
		});
	}
	
	private void runLater(double ms, Runnable runnable) {
		Timeline delayTimeline = new Timeline(new KeyFrame(Duration.millis(ms), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				Platform.runLater(runnable);
		    }
		}));
		delayTimeline.play();
	}
}
