package jchessfx;

public class PiecePawn extends Piece {

	public PiecePawn(int team, int x, int y) {
		super(team, x, y, "Pawn");
	}

	@Override
	public boolean canMoveTo(int x, int y) {
		// The the white pawns must go up and black pawns down.
		if (getTeam() == Piece.WHITE && getY() < y
		 || getTeam() == Piece.BLACK && getY() > y) {
			return false;
		}
		// The pawn can move from 2 squares at once during his first move.
		int maxDistance = 1;
		if ((getTeam() == Piece.WHITE && getY() == 6)
		 || (getTeam() == Piece.BLACK && getY() == 1)) {
			maxDistance = 2;
		}
		return (getX() == x && Math.abs(getY() - y) <= maxDistance);
	}

	@Override
	public boolean canCaptureTo(int x, int y) {
		// The the white pawns must go up and black pawns down.
		if (getTeam() == Piece.WHITE && getY() < y
		 || getTeam() == Piece.BLACK && getY() > y) {
			return false;
		}
		return (Math.abs(getX() - x) == 1 && Math.abs(getY() - y) == 1);
	}
}
