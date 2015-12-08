package jchessfx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar extends HBox {
	
	// Private fields
	private Label statusLabel;
	
	public StatusBar() {
		getStyleClass().add("game-statusbar");
		setAlignment(Pos.CENTER);
		setPrefWidth(Integer.MAX_VALUE);
		
		statusLabel = new Label();
		
		getChildren().addAll(statusLabel);
	}
	
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
	
	private String secondsToTime(int seconds) {
		int minutes = seconds / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}
