package src.balde.bah.tp6;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CrosswordSquare<T> {
    private T solution;
    private StringProperty proposition = new SimpleStringProperty("");
    private T horizontalDefinition;
    private T verticalDefinition;
    private boolean black;

    public CrosswordSquare() {
        this.solution = null;
        this.horizontalDefinition = null;
        this.verticalDefinition = null;
        this.black = false;
    }
    public StringProperty propositionProperty() {
        return proposition;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
        if (black) {
            this.solution = null;
            this.proposition.set("");
        }
    }

    public T getSolution() { return solution; }
    public void setSolution(T solution) { this.solution = solution; }

    public String getProposition() { return proposition.get(); }
    public void setProposition(String prop) { this.proposition.set(prop); }
    
    public T getHorizontalDefinition() { return horizontalDefinition; }
    public void setHorizontalDefinition(T horizontalDefinition) { this.horizontalDefinition = horizontalDefinition; }

    public T getVerticalDefinition() { return verticalDefinition; }
    public void setVerticalDefinition(T verticalDefinition) { this.verticalDefinition = verticalDefinition; }
}

