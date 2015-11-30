package jchessfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GameMenu extends VBox {
	private Button resumeButton;
	private Button restartButton;
	private Button exitButton;
	
	public GameMenu() {
		setMaxSize(320, 128);
		
		getStyleClass().add("game-menu");
		
		resumeButton  = new Button("Resume");
		restartButton = new Button("Restart");
		exitButton    = new Button("Exit");
		
		resumeButton .setPrefWidth(Integer.MAX_VALUE);
		restartButton.setPrefWidth(Integer.MAX_VALUE);
		exitButton   .setPrefWidth(Integer.MAX_VALUE);

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
