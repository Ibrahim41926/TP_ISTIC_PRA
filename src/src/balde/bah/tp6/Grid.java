package src.balde.bah.tp6;

public class Grid <T> {
    private final int height;
    private final int width;
    private final T[][] array;

    /**
     * @pre height >= 0 et width >= 0
     */
    public Grid(int height, int width) {
        if (height < 0 || width < 0) {
            throw new IllegalArgumentException("Dimensions négatives: height=" + height + ", width=" + width);
        }
        this.height = height;
        this.width = width;
        this.array = (T[][]) new Object[height][width];

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * @return true ssi (row, column) désignent une cellule existante
     */
    public boolean correctCoords(int row, int column) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    private void checkCoords(int row, int column) {
        if (!correctCoords(row, column)) {
            throw new IllegalArgumentException("Coordonnées invalides: (" + row + "," + column + ")");
        }
    }

    /**
     * @pre correctCoords(row, column)
     */
    public T getCell(int row, int column) {
        checkCoords(row, column);
        return array[row][column];
    }

    /**
     * @pre correctCoords(row, column)
     */
    public void setCell(int row, int column, T value) {
        checkCoords(row, column);
        array[row][column] = value;
    }
}

