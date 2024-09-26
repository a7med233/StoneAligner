package swapstones.model;

/**
 * Represents the position of a stone on the board.
 *
 * @param col the columns indexes in the board
 * @param stone the stones indexes in the board
 */
public record Position(int col, Stone stone) {

    /**
     * Constructs a {@code Position} object with the specified column and stone.
     *
     * @param col   the column index of the position
     * @param stone the stone at the position
     */
    public Position {
    }


    /**
     * Getter method returns the column index of the position.
     *
     * @return the column index
     */
    @Override
    public int col() {
        return col;
    }

    /**
     * Getter method Returns the stone at the position.
     *
     * @return the stone at the position
     */
    @Override
    public Stone stone() {
        return stone;
    }

    /**
     * to String method returns a string representation of the position.
     * The string representation consists of the column index and the stone,
     * enclosed in braces.
     *
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return "Position{"
                + "col="
                + col
                + ", stone="
                + stone
                + '}';
    }
}
