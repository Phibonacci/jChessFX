package jchessfx;

public class PieceKing extends Piece {

	private boolean check;
	
	public PieceKing(int team, int x, int y) {
		super(team, x, y, "King");
		check = false;
	}

	public boolean hasBeenChecked() {
		return check;
	}

	public void setCheck() {
		check = true;
	}
	
	@Override
	public boolean canMoveTo(int x, int y) {
		return (Math.abs(getX() - x) <= 1 && Math.abs(getY() - y) <= 1);
	}
}
