package jchessfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class StatusBar extends HBox {
	
	// Private fields
	private Label timerBlack;
	private Label timerWhite;
	
	public StatusBar() {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(8));
		setPrefWidth(Integer.MAX_VALUE);
		
		timerBlack = new Label("15:00");
		timerWhite = new Label("15:00");

	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);
	    
		getChildren().addAll(timerBlack, spacer, timerWhite);
	}
}
