package battleship;

import java.util.List;

/**
 * The type Carrack represents a ship with a size of 3 units.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Carrack extends Ship {

	/**
	 * Instantiates a new Carrack.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Carrack(Compass bearing, IPosition pos) {
		super("Nau", bearing, pos, 3);

		switch (bearing) {
			case SOUTH:
                fillVertical(pos);
                break;
			case NORTH:
                fillVertical(pos);
                break;
			case EAST:
                fillHorizontal(pos);
                break;
			case WEST:
                fillHorizontal(pos);
                break;
		}
	}

    private void fillHorizontal(IPosition pos) {
        for (int c = 0; c < this.getSize(); c++)
            getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
    }

    private void fillVertical(IPosition pos) {
        for (int r = 0; r < this.getSize(); r++)
            getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
    }
}