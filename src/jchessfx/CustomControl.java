package jchessfx;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Custom control representing the Chess board.
 * Contains a default skin and a board.
 * The control will detect mouse and keyboard interactions and overlay the board.
 * 
 * @see CustomControlSkin
 * @see ChessBoard
 */
public class CustomControl extends Control {

	// Private constants
	private static final int MARGIN = 16;
	
	// Private fields
	private ChessBoard board;
	
	/**
	 * Create a new custom control.
	 */
	public CustomControl(StatusBar statusBar) {
		setSkin(new CustomControlSkin(this));
		
		board = new ChessBoard(statusBar);
		getChildren().add(board);
		
		board.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				board.click(event.getX(), event.getY());
			}
		});
		
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.SPACE) {
					// TODO reset game
				}
			}
		});
	}

	/**
	 * Overridden {@link Control#resize} method.
	 * Resizes the control and the internal board.
	 */
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);

		// Make sure the board stays a square by computing the smallest size.
		double size = Math.min(width, height);
		
		// Leave enough space available for the margin;
		size -= MARGIN;
		
		// Make sure the board size is a multiple of 8 to prevent blurry lines.
		size -= size % 8;
		
		// Just in case...
		if (size < 8) {
			size = 8;
		}
		
		board.setMaxSize(size, size);
	}
}
