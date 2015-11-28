package jchessfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GameMenu extends VBox {
	
	private static final String BUTTON_STYLE
		= "-fx-text-fill: white;"
		+ "-fx-background-color: #454545;"
		+ "-fx-font-size: 150%;";
	
	private Button resumeButton;
	private Button restartButton;
	private Button exitButton;
	
	public GameMenu() {
		setMaxSize(320, 128);
		setPadding(new Insets(16, 16, 16, 16));
		setSpacing(8);
		setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: rgba(32, 32, 32, 0.5)");
		
		resumeButton  = new Button("Resume");
		restartButton = new Button("Restart");
		exitButton    = new Button("Exit");
		
		resumeButton .setPrefWidth(Integer.MAX_VALUE);
		restartButton.setPrefWidth(Integer.MAX_VALUE);
		exitButton   .setPrefWidth(Integer.MAX_VALUE);

		resumeButton .setStyle(BUTTON_STYLE);
		restartButton.setStyle(BUTTON_STYLE);
		exitButton   .setStyle(BUTTON_STYLE);

		resumeButton .setCursor(Cursor.HAND);
		restartButton.setCursor(Cursor.HAND);
		exitButton   .setCursor(Cursor.HAND);

		getChildren().addAll(resumeButton, restartButton, exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
	}
	
	public void setResumeAction(EventHandler<ActionEvent> action) {
		resumeButton.setOnAction(action);
	}
	
	public void setRestartAction(EventHandler<ActionEvent> action) {
		restartButton.setOnAction(action);
	}
}
