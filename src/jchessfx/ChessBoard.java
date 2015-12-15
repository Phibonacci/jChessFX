package jchessfx;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.Animation;
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

/**
 * Represents the graphical view of the game board.
 * Executes the logic of the game and the various animations.
 */
public class ChessBoard extends Pane {
	
	// Public constants
	public static final int BOARD_WIDTH  = 8;
	public static final int BOARD_HEIGHT = 8;
	
	// Private members
	private double cellWidth;
	private double cellHeight;
	private Piece[][]  board;
	private Square[][] squares;
	private Piece selected;
	private boolean dragging;
	
	private Timeline timer;
	
	private List<Timeline> activeAnimations;
	private List<Piece>    orphanPieces;

	private GameLogic     logic;
	private StatusBar     statusBar;
	private PromotionMenu promotionMenu;

	public ChessBoard(StatusBar statusBar) {
		this.statusBar = statusBar;
		
		selected = null;
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
		timer = new Timeline(new KeyFrame(Duration.millis(1000), e -> updateTime()));
		timer.setCycleCount(Animation.INDEFINITE);
		activeAnimations = new LinkedList<Timeline>();
		orphanPieces = new LinkedList<Piece>();
		
		logic = new GameLogic(board);
		
		resetGame();
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		cellWidth  = width  / BOARD_WIDTH;
		cellHeight = height / BOARD_HEIGHT;
		
		if (promotionMenu != null) {
			promotionMenu.setMinWidth(width);
			promotionMenu.setMinHeight(width);
		}
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				squares[i][j].resize(cellWidth, cellHeight);
				squares[i][j].relocate(j * cellWidth, i * cellHeight);
				if (board[i][j] != null) {
					board[i][j].resize(cellWidth, cellHeight);
					if (!dragging || selected != board[i][j]) {
						board[i][j].relocate(j * cellWidth, i * cellHeight);
					}
				}
			}
		}
	}
	
	public void resetGame() {
		// Clear any active animation.
		for (Timeline animation : activeAnimations) {
			animation.stop();
		}
		activeAnimations.clear();
		
		// Delete all orphan pieces. They are no longer in the board
		// and only used in animations.
		for (Piece orphan : orphanPieces) {
			getChildren().remove(orphan);
		}
		activeAnimations.clear();
		
		// Close the promotion menu if existing.
		if (promotionMenu != null) {
			getChildren().remove(promotionMenu);
			promotionMenu = null;
		}
		
		dragging = false;
		selected = null;
		timer.stop();
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
		timer.playFromStart();
		logic.reset();
		updateStatus();
		updateSelectableSquares();
	}

	public void click(final double x, final double y) {
		if (!logic.isGameRunning() || promotionMenu != null) {
			return;
		}
		if (selected == null) {
			selectPiece(x, y);
		} else {
			placePiece(x, y);
			dragging = false;
		}
	}
	
	public void dragEnter(final double x, final double y) {
		if (!logic.isGameRunning() || promotionMenu != null) {
			return;
		}
		if (selected == null) {
			selectPiece(x, y);
		}
		dragging = true;
	}

	public void drag(double x, double y) {
		if (!logic.isGameRunning() || promotionMenu != null || selected == null) {
			dragging = false;
			return;
		}
		if (x > BOARD_WIDTH * cellWidth) {
			x = BOARD_WIDTH * cellWidth;
		} else if (x < 0) {
			x = 0;
		}
		if (y > BOARD_HEIGHT * cellHeight) {
			y = BOARD_HEIGHT * cellHeight;
		} else if (y < 0) {
			y = 0;
		}
		if (x >= 0 && x <= BOARD_WIDTH * cellWidth && y >= 0 && y <= BOARD_HEIGHT * cellHeight) {
			selected.relocate(x - cellWidth / 2, y - cellHeight / 2);
		}
	}
	
	public void selectPiece(final double x, final double y) {
		int indexx = (int)(x / cellWidth);
		int indexy = (int)(y / cellHeight);
		
		Piece target = board[indexy][indexx];
		if (target != null && logic.getCurrentPlayer() == target.getTeam()) {
			target.select();
			selected = target;
			selected.toFront();
		} 
		updateSelectableSquares();
	}
	
	public void placePiece(final double x, final double y) {
		int indexx = (int)(x / cellWidth);
		int indexy = (int)(y / cellHeight);
		
		Piece target = logic.getPiece(indexx, indexy);
		if (target != null && target == selected) {
			selected.unSelect();
			selected = null;
		} else {
			if (isSelectedPieceAllowedToMoveTo(indexx, indexy)) {
				// Store the old position.
				final int oldPositionX = selected.getX();
				final int oldPositionY = selected.getY();

				// Move the piece to the new position.
				logic.setPiecePosition(selected, indexx, indexy);
				selected.addMoveCount();

				// Move the matching rook in case of castling
				if (selected instanceof PieceKing && Math.abs(oldPositionX - indexx) == 2) {
					final int rookY = (logic.getCurrentPlayer() == Piece.WHITE ? 7 : 0);
					final int rookX = (indexx == 2 ? 0 : 7);

					Piece rook = board[rookY][rookX];
					final int oldRookPositionX = rook.getX();
					final int oldRookPositionY = rook.getY();
					logic.setPiecePosition(rook, (indexx == 2 ? 3 : 5), indexy);
					rook.addMoveCount();
					if (dragging && target != null) {
						addFadeoutAnimation(target);
					} else if (!dragging) {
						addTransitionAnimation(selected, target, oldPositionX, oldPositionY);
					}
					addTransitionAnimation(rook, null, oldRookPositionX, oldRookPositionY);
				} else if (selected instanceof PiecePawn && target == null && indexx != oldPositionX) { // En passant
					final int pawnY = oldPositionY;
					final int pawnX = selected.getX();

					Piece pawn = board[pawnY][pawnX];
					board[pawnY][pawnX] = null;
					if (dragging) {
						addFadeoutAnimation(pawn);
					} else if (!dragging) {
						addTransitionAnimation(selected, pawn, oldPositionX, oldPositionY);
					}
				} else {
					// Add the animation.
					if (dragging && target != null) {
						addFadeoutAnimation(target);
					} else if (!dragging) {
						addTransitionAnimation(selected, target, oldPositionX, oldPositionY);
					}
				}
				
				logic.setLastMovedPiece(selected);
				
				// Clear the selected piece.
				selected.unSelect();
				
				if (logic.canPawnGetPromoted(selected)) {
					promotePawn((PiecePawn)selected);
				} else {
					// in case of promote, the updateGameState is only applied
					// once a new piece is selected
					logic.updateGameState();
				}
				
				selected = null;
				
				// Reset the timer.
				timer.stop();
				timer.playFromStart();
				
				// Update the status bar.
				updateStatus();
			} else {
				// Clear the selected piece if we click somewhere we cannot move.
				selected.unSelect();
				selected = null;
			}
		}
		updateSelectableSquares();
	}

	private void replacePromotedPawn(PiecePawn pawn, Piece newPiece) {
		board[pawn.getY()][pawn.getX()] = newPiece;
		getChildren().remove(pawn);
		getChildren().add(newPiece);
		getChildren().remove(promotionMenu);
		promotionMenu = null;
		logic.updateGameState();
		updateStatus();
	}
	
	private void promotePawn(PiecePawn pawn) {
		promotionMenu = new PromotionMenu(pawn.getTeam());

		getChildren().add(promotionMenu);

		promotionMenu.setSpawnQueenAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Piece newPiece = new PieceQueen(pawn.getTeam(), pawn.getX(), pawn.getY());
				replacePromotedPawn(pawn, newPiece);
			}
		});
		promotionMenu.setSpawnKnightAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Piece newPiece = new PieceKnight(pawn.getTeam(), pawn.getX(), pawn.getY());
				replacePromotedPawn(pawn, newPiece);
			}
		});
		promotionMenu.setSpawnBishopAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Piece newPiece = new PieceBishop(pawn.getTeam(), pawn.getX(), pawn.getY());
				replacePromotedPawn(pawn, newPiece);
			}
		});
		promotionMenu.setSpawnRookAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Piece newPiece = new PieceRook(pawn.getTeam(), pawn.getX(), pawn.getY());
				replacePromotedPawn(pawn, newPiece);
			}
		});
	}
	
	private boolean isSelectedPieceAllowedToMoveTo(int x, int y) {
		return logic.isPieceAllowedToMoveTo(selected, x, y);
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
			orphanPieces.add(target);
	        runLater(ms - 150, new Runnable() {
	        	@Override
				public void run() {
					addFadeoutAnimation(target);
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
				getChildren().remove(piece);
				orphanPieces.remove(piece);
			}
		});
	}
	
	private void runLater(double ms, Runnable runnable) {
		Timeline delayTimeline = new Timeline();
		delayTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(ms), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	activeAnimations.remove(delayTimeline);
				Platform.runLater(runnable);
		    }
		}));
		delayTimeline.play();
		activeAnimations.add(delayTimeline);
	}
	
	private void updateTime() {
		if (logic.isGameRunning()) {
			logic.tickTimer();
			updateStatus();
			if (logic.getGameState() == GameLogic.STATE_TIMESUP) {
				gameOver();
			}
		}
	}
	
	private void gameOver() {
		if (selected != null) {
			selected.unSelect();
			selected = null;
		}
		if (promotionMenu != null) {
			getChildren().remove(promotionMenu);
			promotionMenu = null;
		}
		updateSelectableSquares();
	}
	
	private void updateStatus() {
		statusBar.updateStatus(logic);
	}

	public void pauseTimer() {
		timer.stop();
	}
	
	public void resumeTimer() {
		timer.play();
	}
}
