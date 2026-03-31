package src.balde.bah.tp6;

public class Clue {
	private String clue;
	private int row;
	private int column;
	private boolean horizontal;
	public Clue(String clue, int row, int column, boolean horizontal) {
		this.clue = clue;
        this.row = row;
        this.column = column;
        this.horizontal = horizontal;
	}

	public String getClue() {
		return clue;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	public boolean isHorizontal() {
        return this.horizontal;
    }
	
	@Override public String toString() {
		return "(" + (row + 1) + "," + (column + 1) + ") " + clue;
	}
}
