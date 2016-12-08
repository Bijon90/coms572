package coms572;

public class Move {
	private int c0, r0, c1, r1;
	private boolean _capture = false;
	int moveScore;
	
	public int getMoveScore() {
		return moveScore;
	}

	public void setMoveScore(int moveScore) {
		this.moveScore = moveScore;
	}

	public Move(){
		moveScore = 0;
	}
	
    public Move(int col0, int row0, int col1, int row1) {
        this.c0 = col0;
        this.r0 = row0;
        this.c1 = col1;
        this.r1 = row1;
        this.moveScore = 0;
    }

	public int getC0() {
		return c0;
	}

	public void setC0(int c0) {
		this.c0 = c0;
	}

	public int getR0() {
		return r0;
	}

	public void setR0(int r0) {
		this.r0 = r0;
	}

	public int getC1() {
		return c1;
	}

	public void setC1(int c1) {
		this.c1 = c1;
	}

	public int getR1() {
		return r1;
	}

	public void setR1(int r1) {
		this.r1 = r1;
	}

	public boolean is_capture() {
		return _capture;
	}

	public void set_capture(boolean _capture) {
		this._capture = _capture;
	}
	
	public boolean isEqual(Move move2){
		return this.hashCode() == move2.hashCode();
	}
	
    public int hashCode() {
        int cap = 0;
        if (_capture) cap = 1;
        return Integer.parseInt(String.valueOf(getC0()) + String.valueOf(getR0()) 
        + String.valueOf(getC1())+ String.valueOf(getR1()) + String.valueOf(cap));
    }
	
    public int getMoveLength() {
        return Math.max(Math.abs(r1 - r0), Math.abs(c1 - c0));
    }
    
	@Override
    public String toString() {
        return String.valueOf(getC0()) + String.valueOf(getR0()) + "-"
                + String.valueOf(getC1()) + String.valueOf(getR1());
    }
}
