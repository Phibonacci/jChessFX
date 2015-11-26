package jchessfx;

public class PieceKing extends Piece {

	public PieceKing(int team, int x, int y) {
		super(team, x, y, "King");
	}

	@Override
	public boolean canMoveTo(int x, int y) {
		return (Math.abs(getX() - x) <= 1 && Math.abs(getY() - y) <= 1);
	}
	
	@Override
	public boolean hasLineOfSight() {
		return false;
	}
}
