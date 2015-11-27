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
			Font font = AssetsManager.INSTANCE.getFont("assets/fonts/monofonto.ttf", 24);
			statusLabel.setFont(font);
		} catch (Exception e) {
		}
	    
		getChildren().addAll(statusLabel);
	}
	
	public void updateStatus(ChessBoard board) {
		statusLabel.setText(secondsToTime(board.getRemainingSeconds(Piece.WHITE)) + " | " + secondsToTime(board.getRemainingSeconds(Piece.BLACK)));
	}
	
	private String secondsToTime(int seconds) {
		int minutes = seconds / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}
