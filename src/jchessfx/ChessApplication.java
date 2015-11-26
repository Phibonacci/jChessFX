package jchessfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A simple Chess application, made using JavaFX.
 * 
 * @version 0.1a
 * @author Jean Fauquenot, Paul-Maxime Le Duc
 */
public class ChessApplication extends Application {
	
	// Constants
	
	private static final int WINDOW_HEIGHT = 800;
	private static final int WINDOW_WIDTH  = 800;

	// Private fields
	
	private StackPane mainLayout;
	private CustomControl mainControl;

	/**
	 * Overridden {@link Application#init()} method.
	 * Initializes all the controls used by the application and the default layout.
	 * Attaches the controls events to the corresponding methods.
	 */
	@Override
	public void init() {
		mainControl = new CustomControl();
		mainControl.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

		mainLayout = new StackPane();
		mainLayout.getChildren().add(mainControl);
	}

	/**
	 * Overridden {@link Application#start(Stage)} method.
	 * Set a title on the window, set a scene, size then show the window.
	 * 
	 * @param primaryStage Top level stage corresponding to the primary window.
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("jChessFX");
		primaryStage.setScene(new Scene(mainLayout, WINDOW_HEIGHT, WINDOW_WIDTH));
		primaryStage.show();
	}

	/**
	 * Entry point to our program.
	 * Launches the JavaFX application using the specified command line arguments.
	 * 
	 * @param args Command line arguments passed to our program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}