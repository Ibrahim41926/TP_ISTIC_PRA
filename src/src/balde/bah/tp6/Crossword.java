package src.balde.bah.tp6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;

public class Crossword extends Grid<CrosswordSquare> {
	private ObservableList<Clue> verticalClues;
	private ObservableList<Clue> horizontalClues;
	public Crossword(int height, int width)throws Exception {
        super(height, width);
        // Permet d'initialiser chaque case de la grille avec un objet CrosswordSquare
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                this.setCell(r, c, new CrosswordSquare());
            }
        }
        verticalClues = FXCollections.observableArrayList();
        horizontalClues = FXCollections.observableArrayList();
    }
	
	public StringProperty propositionProperty(int row, int column) throws Exception {
        return getCell(row, column).propositionProperty();
    }
	
	public boolean isBlackSquare(int row, int column) throws Exception {
        return getCell(row, column).isBlack();
    }

    public void setBlackSquare(int row, int column, boolean black) throws Exception {
        getCell(row, column).setBlack(black);
    }

    public char getSolution(int row, int column) throws Exception {
        Object sol = getCell(row, column).getSolution();
        return (sol == null) ? ' ' : (char) sol;
    }

    public void setSolution(int row, int column, char solution) throws Exception {
        getCell(row, column).setSolution(solution);
    }

    public char getProposition(int row, int column) throws Exception {
    	String prop = getCell(row, column).getProposition();
        // Si la case est vide ou null, on renvoie un espace, sinon le caractère
        return (prop == null || prop.isEmpty()) ? ' ' : prop.charAt(0);
    }

    public void setProposition(int row, int column, char proposition) throws Exception {
    	getCell(row, column).setProposition(String.valueOf(proposition));
    }


    public String getDefinition(int row, int column, boolean horizontal) throws Exception {
        if (horizontal) {
            return (String) getCell(row, column).getHorizontalDefinition();
        } else {
            return (String) getCell(row, column).getVerticalDefinition();
        }
    }

    public void setDefinition(int row, int column, boolean horizontal, String definition) throws Exception {
        if (horizontal) {
            getCell(row, column).setHorizontalDefinition(definition);
        } else {
            getCell(row, column).setVerticalDefinition(definition);
        }
    }


    public ObservableList<Clue> getVerticalClues() {
        return verticalClues;
    }

    public ObservableList<Clue> getHorizontalClues() {
        return horizontalClues;
    }

    /**
     * Vérifie si la grille est entièrement remplie et correcte
     */
    public boolean isGridComplete() throws Exception {
        for (int r = 0; r < getHeight(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (!isBlackSquare(r, c)) {
                    char prop = getProposition(r, c);
                    char sol = getSolution(r, c);
                    // Si la case n'est pas remplie ou incorrecte, la grille n'est pas complète
                    if (prop == ' ' || prop == '\0' || Character.toUpperCase(prop) != Character.toUpperCase(sol)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void printProposition() {
    	System.out.println("Grille des propositions :");
    	System.out.println(this.toString());
    }

    public void printSolution() {
    	System.out.println("Grille des solutions :");
    	for (int i = 0; i < getHeight(); i++) {
    		for (int j = 0; j < getWidth(); j++) {
    			try {
    				if (isBlackSquare(i, j)) System.out.print("■ ");
    				else System.out.print(getSolution(i, j) + " ");
    			} catch (Exception e) { e.printStackTrace(); }
    		}
    		System.out.println();
    	}
    }
}

