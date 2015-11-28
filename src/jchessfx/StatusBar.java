package jchessfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class StatusBar extends HBox {
	
	// Private fields
	private Label statusLabel;
	
	public StatusBar() {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(8));
		setPrefWidth(Integer.MAX_VALUE);
		
		statusLabel = new Label();

		try {
			Font font = AssetsManager.INSTANCE.getFont("assets/fonts/monofonto.ttf", 18);
			statusLabel.setFont(font);
		} catch (Exception e) {
		}
	    
		getChildren().addAll(statusLabel);
	}
	
	public void updateStatus(ChessBoard board) {
		String remainingWhite = board.getRemainingPiecesCount(Piece.WHITE) + "/16";
		String remainingBlack = board.getRemainingPiecesCount(Piece.BLACK) + "/16";
		String whiteTime = secondsToTime(board.getRemainingSeconds(Piece.WHITE));
		String blackTime = secondsToTime(board.getRemainingSeconds(Piece.BLACK));
		String separator = " | ";
		switch (board.getGameSate()) {
		case ChessBoard.STATE_CHECK:
			separator = " | Check | ";
			break;
		case ChessBoard.STATE_CHECKMATE:
			separator = " | Checkmate | ";
			break;
		case ChessBoard.STATE_STALEMATE:
			separator = " | Stalemate | ";
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
