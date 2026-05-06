package battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for class Fleet.
 * Author: 111245 Martim Reis
 * Date: 2026-05-06 12:48
 * Cyclomatic Complexity:
 * - constructor: 1
 * - createRandom(): 3
 * - getShips(): 1
 * - addShip(IShip): 4
 * - getShipsLike(String): 2
 * - getFloatingShips(): 2
 * - getSunkShips(): 2
 * - shipAt(IPosition): 2
 * - printShips(List): 2
 * - printStatus(): 1
 * - printShipsByCategory(String): 1
 * - printFloatingShips(): 1
 * - printAllShips(): 1
 */
class FleetTest {

	private Fleet fleet;

	@BeforeEach
	void setUp() {
		fleet = new Fleet();
	}

	@AfterEach
	void tearDown() {
		fleet = null;
	}

	@Test
	void constructor() {
		assertAll("Fleet constructor path",
				() -> assertNotNull(fleet, "Error: expected Fleet instance to be created, but it was null."),
				() -> assertNotNull(fleet.getShips(), "Error: expected internal ships list to be initialized, but it was null."),
				() -> assertTrue(fleet.getShips().isEmpty(), "Error: expected a new fleet to start empty, but it was not.")
		);
	}

	@Test
	void createRandom1() {
		IFleet randomFleet = Fleet.createRandom();

		assertAll("createRandom returned fleet",
				() -> assertNotNull(randomFleet, "Error: expected createRandom() to return a fleet, but it returned null."),
				() -> assertEquals(11, randomFleet.getShips().size(), "Error: expected random fleet to contain 11 ships, but got a different size.")
		);
	}

	@Test
	void createRandom2() {
		IFleet randomFleet = Fleet.createRandom();

		assertAll("createRandom category distribution",
				() -> assertEquals(1, randomFleet.getShipsLike("Galeao").size(), "Error: expected 1 Galeao in random fleet, but got a different number."),
				() -> assertEquals(1, randomFleet.getShipsLike("Fragata").size(), "Error: expected 1 Fragata in random fleet, but got a different number."),
				() -> assertEquals(2, randomFleet.getShipsLike("Nau").size(), "Error: expected 2 Nau ships in random fleet, but got a different number."),
				() -> assertEquals(3, randomFleet.getShipsLike("Caravela").size(), "Error: expected 3 Caravela ships in random fleet, but got a different number."),
				() -> assertEquals(4, randomFleet.getShipsLike("Barca").size(), "Error: expected 4 Barca ships in random fleet, but got a different number.")
		);
	}

	@Test
	void createRandom3() {
		IFleet randomFleet = Fleet.createRandom();

		assertAll("createRandom ship count consistency",
				() -> assertEquals(11, randomFleet.getShips().size(), "Error: expected random fleet to contain 11 ships, but got a different size."),
				() -> assertEquals(11, randomFleet.getFloatingShips().size(), "Error: expected all newly created ships to be floating, but got a different count."),
				() -> assertEquals(0, randomFleet.getSunkShips().size(), "Error: expected no sunk ships in a new random fleet, but got a different count.")
		);
	}

	@Test
	void getShips() {
		List<IShip> ships = fleet.getShips();

		assertAll("getShips path",
				() -> assertNotNull(ships, "Error: expected getShips() to return a list, but it returned null."),
				() -> assertSame(ships, fleet.getShips(), "Error: expected getShips() to return the same internal list instance, but it did not."),
				() -> assertTrue(ships.isEmpty(), "Error: expected ships list to be empty in a new fleet, but it was not.")
		);
	}

	@Test
	void addShip1() {
		IShip ship = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);

		assertAll("addShip valid path",
				() -> assertTrue(fleet.addShip(ship), "Error: expected addShip() to accept a valid ship, but it returned false."),
				() -> assertEquals(1, fleet.getShips().size(), "Error: expected fleet size 1 after adding a valid ship, but got a different size."),
				() -> assertSame(ship, fleet.getShips().get(0), "Error: expected added ship to be stored in fleet, but it was not.")
		);
	}

	@Test
	void addShip2() {
		IShip ship = new TestShip("Fragata", true, false, true, false, -1, 3, 0, 0);

		assertAll("addShip outside board path",
				() -> assertFalse(fleet.addShip(ship), "Error: expected addShip() to reject an out-of-board ship, but it returned true."),
				() -> assertTrue(fleet.getShips().isEmpty(), "Error: expected fleet to remain empty after rejecting out-of-board ship, but it was not.")
		);
	}

	@Test
	void addShip3() {
		IShip firstShip = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);
		IShip collidingShip = new TestShip("Barca", true, true, true, true, 1, 1, 1, 1);

		assertTrue(fleet.addShip(firstShip), "Error: expected first valid ship to be added, but it was rejected.");

		assertAll("addShip collision path",
				() -> assertFalse(fleet.addShip(collidingShip), "Error: expected addShip() to reject a colliding ship, but it returned true."),
				() -> assertEquals(1, fleet.getShips().size(), "Error: expected fleet size to remain 1 after rejecting colliding ship, but got a different size.")
		);
	}

	@Test
	void addShip4() {
		for (int i = 0; i < IFleet.FLEET_SIZE + 1; i++) {
			assertTrue(fleet.addShip(new TestShip("Barca" + i, true, false, false, true, i, i, i, i)),
					"Error: expected ship " + i + " to be added before fleet limit rejection path, but it was rejected.");
		}

		assertFalse(
				fleet.addShip(new TestShip("Extra", true, false, false, true, 0, 0, 0, 0)),
				"Error: expected addShip() to reject a ship after the fleet size limit was exceeded, but it returned true."
		);
	}

	@Test
	void getShipsLike1() {
		IShip target = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);
		IShip other = new TestShip("Barca", true, false, false, true, 5, 5, 5, 5);

		fleet.addShip(target);
		fleet.addShip(other);

		List<IShip> result = fleet.getShipsLike("Fragata");

		assertAll("getShipsLike match path",
				() -> assertEquals(1, result.size(), "Error: expected exactly one matching ship, but got a different size."),
				() -> assertSame(target, result.get(0), "Error: expected matching ship to be returned, but got a different ship.")
		);
	}

	@Test
	void getShipsLike2() {
		fleet.addShip(new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0));

		List<IShip> result = fleet.getShipsLike("Barca");

		assertTrue(result.isEmpty(), "Error: expected no ships for category Barca, but result was not empty.");
	}

	@Test
	void getFloatingShips1() {
		IShip floatingShip = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);
		IShip sunkShip = new TestShip("Barca", false, false, false, true, 5, 5, 5, 5);

		fleet.addShip(floatingShip);
		fleet.addShip(sunkShip);

		List<IShip> result = fleet.getFloatingShips();

		assertAll("getFloatingShips match path",
				() -> assertEquals(1, result.size(), "Error: expected one floating ship, but got a different size."),
				() -> assertSame(floatingShip, result.get(0), "Error: expected floating ship to be returned, but got a different ship.")
		);
	}

	@Test
	void getFloatingShips2() {
		fleet.addShip(new TestShip("Barca", false, false, true, true, 0, 0, 0, 0));

		assertTrue(fleet.getFloatingShips().isEmpty(), "Error: expected no floating ships, but result was not empty.");
	}

	@Test
	void getSunkShips1() {
		IShip floatingShip = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);
		IShip sunkShip = new TestShip("Barca", false, false, false, true, 5, 5, 5, 5);

		fleet.addShip(floatingShip);
		fleet.addShip(sunkShip);

		List<IShip> result = fleet.getSunkShips();

		assertAll("getSunkShips match path",
				() -> assertEquals(1, result.size(), "Error: expected one sunk ship, but got a different size."),
				() -> assertSame(sunkShip, result.get(0), "Error: expected sunk ship to be returned, but got a different ship.")
		);
	}

	@Test
	void getSunkShips2() {
		fleet.addShip(new TestShip("Barca", true, false, true, true, 0, 0, 0, 0));

		assertTrue(fleet.getSunkShips().isEmpty(), "Error: expected no sunk ships, but result was not empty.");
	}

	@Test
	void shipAt1() {
		IShip ship = new TestShip("Fragata", true, false, true, true, 0, 3, 0, 0);
		fleet.addShip(ship);

		IShip result = fleet.shipAt(new Position(2, 2));

		assertSame(ship, result, "Error: expected shipAt() to return the ship occupying the position, but got a different result.");
	}

	@Test
	void shipAt2() {
		fleet.addShip(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertNull(fleet.shipAt(new Position(9, 9)),
				"Error: expected shipAt() to return null for an unoccupied position, but it returned a ship.");
	}

	@Test
	void printShips1() {
		List<IShip> ships = new ArrayList<>();
		ships.add(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertDoesNotThrow(() -> fleet.printShips(ships),
				"Error: expected printShips() with a non-null list not to throw an exception, but it did.");
	}

	@Test
	void printShips2() {
		assertThrows(AssertionError.class, () -> fleet.printShips(null),
				"Error: expected printShips(null) to throw AssertionError, but it did not.");
	}

	@Test
	void printStatus() {
		fleet.addShip(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertDoesNotThrow(() -> fleet.printStatus(),
				"Error: expected printStatus() not to throw an exception, but it did.");
	}

	@Test
	void printShipsByCategory() {
		fleet.addShip(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertDoesNotThrow(() -> fleet.printShipsByCategory("Fragata"),
				"Error: expected printShipsByCategory() with a valid category not to throw an exception, but it did.");
	}

	@Test
	void printFloatingShips() {
		fleet.addShip(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertDoesNotThrow(() -> fleet.printFloatingShips(),
				"Error: expected printFloatingShips() not to throw an exception, but it did.");
	}

	@Test
	void printAllShips() {
		fleet.addShip(new TestShip("Fragata", true, false, false, true, 0, 3, 0, 0));

		assertDoesNotThrow(() -> fleet.printAllShips(),
				"Error: expected printAllShips() not to throw an exception, but it did.");
	}

	private static final class TestShip implements IShip {
		private final String category;
		private final boolean floating;
		private final boolean tooCloseToShip;
		private final boolean occupiesPosition;
		private final boolean insideBoard;
		private final int leftMost;
		private final int rightMost;
		private final int topMost;
		private final int bottomMost;

		TestShip(String category, boolean floating, boolean tooCloseToShip, boolean occupiesPosition,
				 boolean insideBoard, int leftMost, int rightMost, int topMost, int bottomMost) {
			this.category = category;
			this.floating = floating;
			this.tooCloseToShip = tooCloseToShip;
			this.occupiesPosition = occupiesPosition;
			this.insideBoard = insideBoard;
			this.leftMost = leftMost;
			this.rightMost = rightMost;
			this.topMost = topMost;
			this.bottomMost = bottomMost;
		}

		@Override
		public String getCategory() {
			return category;
		}

		@Override
		public Integer getSize() {
			return 1;
		}

		@Override
		public List<IPosition> getPositions() {
			List<IPosition> positions = new ArrayList<>();
			positions.add(new Position(2, 2));
			return positions;
		}

		@Override
		public List<IPosition> getAdjacentPositions() {
			return new ArrayList<>();
		}

		@Override
		public IPosition getPosition() {
			return new Position(2, 2);
		}

		@Override
		public Compass getBearing() {
			return Compass.NORTH;
		}

		@Override
		public boolean stillFloating() {
			return floating;
		}

		@Override
		public int getTopMostPos() {
			return insideBoard ? topMost : -1;
		}

		@Override
		public int getBottomMostPos() {
			return insideBoard ? bottomMost : Game.BOARD_SIZE;
		}

		@Override
		public int getLeftMostPos() {
			return insideBoard ? leftMost : -1;
		}

		@Override
		public int getRightMostPos() {
			return insideBoard ? rightMost : Game.BOARD_SIZE;
		}

		@Override
		public boolean occupies(IPosition pos) {
			return occupiesPosition && pos.equals(new Position(2, 2));
		}

		@Override
		public boolean tooCloseTo(IShip other) {
			return tooCloseToShip;
		}

		@Override
		public boolean tooCloseTo(IPosition pos) {
			return false;
		}

		@Override
		public void shoot(IPosition pos) {
		}

		@Override
		public void sink() {
		}

		@Override
		public String toString() {
			return category;
		}
	}
}