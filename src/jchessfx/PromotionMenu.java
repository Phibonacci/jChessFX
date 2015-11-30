package jchessfx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class PromotionMenu extends StackPane {
	
	private Button queenButton;
	private Button knightButton;
	private Button bishopButton;
	private Button rookButton;

	ImageView queenImageView;
	ImageView knightImageView;
	ImageView bishopImageView;
	ImageView rookImageView;
	
	private PromotionHBox hbox;

	private class ImageButton extends Button {
		ImageView imageView;
		
		public ImageButton(ImageView imageView) {
			super("", imageView);
			this.imageView = imageView;
			getStyleClass().add("game-promotion-button");
		}
		
		@Override
		public void resize(double width, double height) {
			super.resize(width, height);
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);
		}
	}
	
	private class PromotionHBox extends HBox {
		
		public PromotionHBox(int space) {
			super(space);
		}
				
		@Override
		public void resize(double width, double height) {
			super.resize(width, height);
			
			double buttonWidth  = width / (3.25);
			double buttonHeight = height;
			
			queenButton. setPrefSize(buttonWidth, buttonHeight);
			knightButton.setPrefSize(buttonWidth * 0.75, buttonHeight * 0.75);
			bishopButton.setPrefSize(buttonWidth * 0.75, buttonHeight * 0.75);
			rookButton.  setPrefSize(buttonWidth * 0.75, buttonHeight * 0.75);
			
			queenButton. setMaxSize(buttonWidth, buttonHeight);
			knightButton.setMaxSize(buttonWidth * 0.75, buttonHeight * 0.75);
			bishopButton.setMaxSize(buttonWidth * 0.75, buttonHeight * 0.75);
			rookButton.  setMaxSize(buttonWidth * 0.75, buttonHeight * 0.75);

			queenButton. setMinSize(buttonWidth, buttonHeight);
			knightButton.setMinSize(buttonWidth * 0.75, buttonHeight * 0.75);
			bishopButton.setMinSize(buttonWidth * 0.75, buttonHeight * 0.75);
			rookButton.  setMinSize(buttonWidth * 0.75, buttonHeight * 0.75);
		}
	}
	
	public PromotionMenu(int team) {
		hbox = new PromotionHBox(0);
		hbox.getStyleClass().add("game-promotion");
		
		Image queenImage;
		Image knightImage;
		Image bishopImage;
		Image rookImage;
		try {
			String teamColor = (team == Piece.WHITE ? "white" : "black");
			queenImage  = AssetsManager.INSTANCE.getImage("/assets/images/pieces/" + teamColor + "Queen.png");
			knightImage = AssetsManager.INSTANCE.getImage("/assets/images/pieces/" + teamColor + "Knight.png");
			bishopImage = AssetsManager.INSTANCE.getImage("/assets/images/pieces/" + teamColor + "Bishop.png");
			rookImage   = AssetsManager.INSTANCE.getImage("/assets/images/pieces/" + teamColor + "Rook.png");
			queenImageView  = new ImageView(queenImage);
			knightImageView = new ImageView(knightImage);
			bishopImageView = new ImageView(bishopImage);
			rookImageView   = new ImageView(rookImage);
			queenButton   = new ImageButton(queenImageView);
			knightButton  = new ImageButton(knightImageView);
			bishopButton  = new ImageButton(bishopImageView);
			rookButton    = new ImageButton(rookImageView);
			hbox.getChildren().addAll(queenButton, knightButton, bishopButton, rookButton);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getChildren().add(hbox);
	
	}
	
	public void setSpawnQueenAction(EventHandler<ActionEvent> action) {
		queenButton.setOnAction(action);
	}
	
	public void setSpawnKnightAction(EventHandler<ActionEvent> action) {
		knightButton.setOnAction(action);
	}

	public void setSpawnBishopAction(EventHandler<ActionEvent> action) {
		bishopButton.setOnAction(action);
	}

	public void setSpawnRookAction(EventHandler<ActionEvent> action) {
		rookButton.setOnAction(action);
	}
	
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);

		double squareWidth  = width  / ChessBoard.BOARD_WIDTH;
		double squareHeight = height / ChessBoard.BOARD_HEIGHT;
	
		double boxWidth = squareWidth * 0.75 * 3 + squareWidth;
		double boxHeight = squareHeight;
	
		hbox.setMinWidth (boxWidth);
		hbox.setMaxWidth (boxWidth);
		hbox.setPrefWidth(boxWidth);
	
		hbox.setMinHeight (boxHeight);
		hbox.setMaxHeight (boxHeight);
		hbox.setPrefHeight(boxHeight);
	}
	
}
