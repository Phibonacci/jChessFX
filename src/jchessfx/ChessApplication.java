package jchessfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A simple Chess application, made using JavaFX.
 * 
 * @version 0.6b
 * @author Jean Fauquenot, Paul-Maxime Le Duc
 */
public class ChessApplication extends Application {
	
	// Constants

	private static final int WINDOW_WIDTH  = 800;
	private static final int WINDOW_HEIGHT = 800;
	
	private static final int WINDOW_MIN_WIDTH  = 300;
	private static final int WINDOW_MIN_HEIGHT = 200;

	// Private fields
	
	private VBox mainLayout;
	private StatusBar statusBar;
	private CustomControl mainControl;

	/**
	 * Overridden {@link Application#init()} method.
	 * Initializes all the controls used by the application and the default layout.
	 * Attaches the controls events to the corresponding methods.
	 */
	@Override
	public void init() {
		statusBar = new StatusBar();
		
		mainControl = new CustomControl(statusBar);
		mainControl.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		mainLayout = new VBox();
		mainLayout.getChildren().addAll(mainControl, statusBar);
		
		mainLayout.getStyleClass().add("game-window");
	}

	/**
	 * Overridden {@link Application#start(Stage)} method.
	 * Set a title on the window, set a scene, size then show the window.
	 * 
	 * @param primaryStage Top level stage corresponding to the primary window.
	 */
	@Override
	public void start(Stage primaryStage) {
		// Create a new scene and add our default CSS stylesheet.
		Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("/assets/style/default.css").toString());
		
		// Set the stage information and display it.
		primaryStage.setTitle("jChessFX");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(WINDOW_MIN_WIDTH);
		primaryStage.setMinWidth(WINDOW_MIN_HEIGHT);
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