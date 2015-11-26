package jchessfx;

public class PieceRook extends Piece {

	public PieceRook(int team, int x, int y) {
		super(team, x, y, "Rook");
	}
	
	@Override
	public boolean canMoveTo(int x, int y) {
		return canMoveInLineFromTo(getX(), getY(), x, y);
	}

	public static boolean canMoveInLineFromTo(int fromX, int fromY, int toX, int toY) {
		return (fromX == toX || fromY == toY);
	}
}
