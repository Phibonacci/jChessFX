package jchessfx;

public class PieceQueen extends Piece {

	public PieceQueen(int team, int x, int y) {
		super(team, x, y, "Queen");
	}
	
	@Override
	public boolean canMoveTo(int x, int y) {
		return PieceRook.canMoveInLineFromTo(getX(), getY(), x, y)
		    || PieceBishop.canMoveDiagonallyFromTo(getX(), getY(), x, y);
	}
}
