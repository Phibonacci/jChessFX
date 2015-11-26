package jchessfx;

public class PieceKnight extends Piece {

	public PieceKnight(int team, int x, int y) {
		super(team, x, y, "Knight");
	}

	@Override
	public boolean canMoveTo(int x, int y) {
		return (getX() != x && getY() != y &&
		        Math.abs(getX() - x) + Math.abs(getY() - y) == 3);
	}
	
	@Override
	public boolean hasLineOfSight() {
		return false;
	}
}
