package jchessfx;

public class GameLogic {

	// Public constants
	public static final int STATE_PLAYING   = 0;
	public static final int STATE_CHECK     = 1;
	public static final int STATE_CHECKMATE = 2;
	public static final int STATE_STALEMATE = 3;
	
	// Private constants
	private static final int TIMER_DURATION = 15 * 60; // 15 minutes
	
	// Private fields
	private Piece[][] board;
	private Piece     whiteKing;
	private Piece     blackKing;
	private int       gameState;
	private int       currentPlayer;
	private int       winner;
	private int       remainingSeconds[];
	private Piece     lastMovedPiece;
	
	// Public constructor
	
	public GameLogic(Piece[][] board) {
		this.board = board;
		remainingSeconds = new int[3];
	}
	
	// Public getters/setters

	public int getGameState() {
		return gameState;
	}
	
	public boolean isGameRunning() {
		return (gameState == STATE_PLAYING || gameState == STATE_CHECK);
	}
	
	public Piece getPiece(int x, int y) {
		return board[y][x];
	}
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getWinner() {
		return winner;
	}
	
	public int getRemainingSeconds(int player) {
		return remainingSeconds[player];
	}
	
	public void setLastMovedPiece(Piece piece) {
		lastMovedPiece = piece;
	}
	
	// Public methods
	
	public void reset() {
		whiteKing      = board[7][4];
		blackKing      = board[0][4];
		gameState      = STATE_PLAYING;
		currentPlayer  = Piece.WHITE;
		winner         = Piece.EMPTY;
		lastMovedPiece = null;

		remainingSeconds[Piece.WHITE] = TIMER_DURATION;
		remainingSeconds[Piece.BLACK] = TIMER_DURATION;
	}
	
	public void tickTimer() {
		remainingSeconds[currentPlayer] -= 1;
	}
	
	public void setPiecePosition(Piece piece, int x, int y) {
		if (piece != null) {
			board[piece.getY()][piece.getX()] = null;
			piece.setPosition(x, y);
		}
		board[y][x] = piece;
	}
	
	public void updateGameState() {
		int nextPlayer  = (currentPlayer == Piece.WHITE ? Piece.BLACK : Piece.WHITE);
		boolean check   = isCheck(nextPlayer);
		boolean canMove = isTeamAllowedToMove(nextPlayer);
		boolean canEnd  = canGameEnd();
		
		if (check && !canMove) {
			gameState = STATE_CHECKMATE;
			winner = currentPlayer;
		} else if (!canMove || !canEnd) {
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
	
	public int getRemainingPiecesCount(int player) {
		int count = 0;
		for (int x = 0; x < ChessBoard.BOARD_WIDTH; ++x) {
			for (int y = 0; y < ChessBoard.BOARD_HEIGHT; ++y) {
				Piece piece = getPiece(x, y);
				if (piece != null && piece.getTeam() == player) {
					++count;
				}
			}
		}
		return count;
	}

	
	public boolean isPieceAllowedToMoveTo(Piece piece, int x, int y) {
		if (piece instanceof PieceKing && !piece.hasMoved() && y == piece.getY() && (x == 2 || x == 6)) {
			if (isAllowedToDoCastling((PieceKing)piece, x, piece.getY())) {
				return true;
			}
		} else if (piece instanceof PiecePawn && lastMovedPiece instanceof PiecePawn && isAllowedToDoEnPassant((PiecePawn)piece, x, y)) {
			return true;
		}
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

	// Private methods

	private int getSquareColor(Piece piece) {
		if ((piece.getX() + piece.getY()) % 2 == 0) {
			return Piece.WHITE;
		}
		return Piece.BLACK;
	}
	
	public boolean canPawnGetPromoted(Piece piece) {
		if (piece instanceof PiecePawn) {
			if ((piece.getTeam() == Piece.WHITE && piece.getY() == 0)
					|| (piece.getTeam() == Piece.BLACK && piece.getY() == 7)) {
				return true;
			}
		}
		return false;
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
	
	private boolean canGameEnd() {
		int whiteTeam = 0;
		int blackTeam = 0;
		int whiteBishop = Piece.EMPTY;
		int blackBishop = Piece.EMPTY;
		for (int j = 0; j < ChessBoard.BOARD_HEIGHT; j++) {
			for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
				int bonus = 0;
				Piece target = board[j][i];
				if (target == null) {
					continue;
				}
				if (target instanceof PiecePawn) {
					bonus = 2;
				} else if (target instanceof PieceBishop) {
					int color = getSquareColor(target);
					if (target.getTeam() == Piece.WHITE && whiteBishop != color) {
						bonus = 1;
						whiteBishop = color;
					} else if (target.getTeam() == Piece.BLACK && blackBishop != color) {
						bonus = 1;
						blackBishop = color;
					}
				} else if (target instanceof PieceKnight) {
					bonus = 1;
				} else if (target instanceof PieceQueen) {
					bonus = 2;
				} else if (target instanceof PieceRook) {
					bonus = 2;
				}
				if (target.getTeam() == Piece.WHITE) {
					whiteTeam += bonus;
				} else {
					blackTeam += bonus;
				}
				if (whiteTeam >= 2 || blackTeam >= 2) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isCheck(int team) {
		Piece king = (team == Piece.WHITE ? whiteKing : blackKing);
		for (int j = 0; j < ChessBoard.BOARD_HEIGHT; j++) {
			for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
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
	
	private boolean isTeamAllowedToMove(int team) {
		for (int j = 0; j < ChessBoard.BOARD_HEIGHT; j++) {
			for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
				Piece target = board[j][i];
				if (target != null && target.getTeam() == team && isPieceAllowedToMove(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isPieceAllowedToMove(Piece piece) {
		for (int j = 0; j < ChessBoard.BOARD_HEIGHT; j++) {
			for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
				if (isPieceAllowedToMoveTo(piece, i, j)) {
					return true;
				}
			}
		}
		return false;
	} 
	
	private boolean isAllowedToDoCastling(PieceKing king, int x, int y) {
		if (king.hasMoved() || (x != 2 && x != 6)) {
			return false;
		}
		final int rookY = (currentPlayer == Piece.WHITE ? 7 : 0);
		final int rookX = (x == 2 ? 0 : 7);
		Piece rook = board[rookY][rookX];
		if (!(rook instanceof PieceRook) || rook.hasMoved()) {
			return false;
		}
		if (!checkLineOfSight(rookX, rookY, 4, y)) {
			return false;
		}

		int oldX = king.getX();
		int oldY = king.getY();
		
		setPiecePosition(king, x, y);
		setPiecePosition(rook, (x == 2 ? 3 : 5), y);
		boolean check = isCheck(king.getTeam());
		setPiecePosition(king, oldX, oldY);
		setPiecePosition(rook, rookX, rookY);
		return !check;
	}
	
	private boolean isAllowedToDoEnPassant(PiecePawn piece, int x, int y) {
		if (!(lastMovedPiece instanceof PiecePawn)) {
			return false;
		}
		PiecePawn lastPiece = (PiecePawn)lastMovedPiece;
		
		if (lastPiece.getMoveCount() == 1 && (lastPiece.getY() == 3 || lastPiece.getY() == 4)                    // LastPiece moved from 2 squares last turn?
				&& piece.getY() == lastPiece.getY() && Math.abs(piece.getX() - lastPiece.getX()) == 1            // piece juxtaposed on Y?
				&& y == lastPiece.getY() + (piece.getTeam() == Piece.WHITE ? -1 : 1) && x == lastPiece.getX()) { // diagonal move in the right direction?
			int oldX = piece.getX();
			int oldY = piece.getY();

			setPiecePosition(piece, x, y);
			board[lastPiece.getY()][lastPiece.getX()] = null;
			boolean check = isCheck(piece.getTeam());
			setPiecePosition(piece, oldX, oldY);
			board[lastPiece.getY()][lastPiece.getX()] = lastPiece;
			return !check;
		}
		return false;
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
}
