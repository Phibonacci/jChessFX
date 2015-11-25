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

	// Private fields
	private ChessBoard board;
	
	/**
	 * Create a new custom control.
	 */
	public CustomControl() {
		setSkin(new CustomControlSkin(this));
		
		board = new ChessBoard();
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
	 * Resize the control and the internal board.
	 */
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);

		double size = Math.min(width, height);
		board.setMaxSize(size, size);
	}
}
