package jchessfx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Status bar of the game, display information about the current board logic as text.
 */
public class StatusBar extends HBox {
	
	// Private fields
	private Label statusLabel;
	
	/**
	 * Initializes the status bar control and sub controls, especially the label.
	 */
	public StatusBar() {
		getStyleClass().add("game-statusbar");
		setAlignment(Pos.CENTER);
		setPrefWidth(Integer.MAX_VALUE);
		
		statusLabel = new Label();
		
		getChildren().addAll(statusLabel);
	}
	
	/**
	 * Updates the internal label with information about the game logic.
	 * @param board Logic of the current game board.
	 */
	public void updateStatus(GameLogic board) {
		String remainingWhite = board.getRemainingPiecesCount(Piece.WHITE) + "/16";
		String remainingBlack = board.getRemainingPiecesCount(Piece.BLACK) + "/16";
		String whiteTime = secondsToTime(board.getRemainingSeconds(Piece.WHITE));
		String blackTime = secondsToTime(board.getRemainingSeconds(Piece.BLACK));
		String separator = " | ";
		switch (board.getGameState()) {
		case GameLogic.STATE_CHECK:
			separator = " | Check | ";
			break;
		case GameLogic.STATE_CHECKMATE:
			separator = " | Checkmate | ";
			break;
		case GameLogic.STATE_STALEMATE:
			separator = " | Stalemate | ";
			break;
		case GameLogic.STATE_TIMESUP:
			separator = " | Time's up! | ";
			break;
		}
		String status = remainingWhite + " - " + whiteTime + separator + blackTime + " - " + remainingBlack;
		if (board.getCurrentPlayer() == Piece.WHITE) {
			status = "White    " + status + "         ";
		} else {
			status = "         " + status + "    Black";
		}
		statusLabel.setText(status);
	}
	
	/**
	 * Converts a number of seconds into a pretty time value, like "08:54".
	 * @param seconds Amount of seconds to translate.
	 * @return A new formatted string.
	 */
	private String secondsToTime(int seconds) {
		int minutes = seconds / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}
