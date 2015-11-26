package jchessfx;

public class PieceBishop extends Piece {

	public PieceBishop(int team, int x, int y) {
		super(team, x, y, "Bishop");
	}
	
	@Override
	public boolean canMoveTo(int x, int y) {
		return canMoveDiagonallyFromTo(getX(), getY(), x, y);
	}
	
	public static boolean canMoveDiagonallyFromTo(int fromX, int fromY, int toX, int toY) {
		return (Math.abs(fromX - toX) == Math.abs(fromY - toY));
	}
}
