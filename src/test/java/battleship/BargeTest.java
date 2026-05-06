package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Barge class.
 * Author: IGE-111245 Martim Reis
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getSize: 1  (inherited from Ship)
 * - stillFloating: 2
 * - getPositions: 1
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - getTopMostPos: 1
 * - getBottomMostPos: 1
 * - getLeftMostPos: 1
 * - getRightMostPos: 1
 */
public class BargeTest {

	private Barge barge;

	@BeforeEach
	void setUp() {
		barge = new Barge(Compass.NORTH, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		barge = null;
	}

	// ----------------------------------------------------------------
	// constructor (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void constructor() {
		assertAll(
				() -> assertNotNull(barge, "Error: Barge instance should not be null."),
				() -> assertEquals("Barca", barge.getCategory(), "Error: Barge category should be 'Barca'."),
				() -> assertEquals(Compass.NORTH, barge.getBearing(), "Error: Barge bearing should be NORTH."),
				() -> assertEquals(1, barge.getSize(), "Error: Barge size should be 1."),
				() -> assertEquals(1, barge.getPositions().size(), "Error: Barge should have exactly 1 position."),
				() -> assertEquals(new Position(5, 5), barge.getPositions().get(0), "Error: Barge position should be (5,5).")
		);
	}

	// ----------------------------------------------------------------
	// getSize (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getSize() {
		assertEquals(1, barge.getSize(), "Error: Barge size should be 1.");
	}

	// ----------------------------------------------------------------
	// stillFloating (CC = 2)
	// path 1: at least one position not hit → returns true
	// path 2: all positions hit → returns false
	// ----------------------------------------------------------------

	@Test
	void stillFloating1() {
		assertTrue(barge.stillFloating(), "Error: Barge should be floating when not hit.");
	}

	@Test
	void stillFloating2() {
		barge.getPositions().get(0).shoot();
		assertFalse(barge.stillFloating(), "Error: Barge should not be floating after its only position is hit.");
	}

	// ----------------------------------------------------------------
	// getPositions (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getPositions() {
		assertAll(
				() -> assertEquals(1, barge.getPositions().size(), "Error: Barge should have exactly 1 position."),
				() -> assertEquals(new Position(5, 5), barge.getPositions().get(0), "Error: Barge position should be (5,5).")
		);
	}

	// ----------------------------------------------------------------
	// shoot (CC = 2)
	// path 1: position matches → marks as hit
	// path 2: position does not match → no hit
	// ----------------------------------------------------------------

	@Test
	void shoot1() {
		barge.shoot(new Position(5, 5));
		assertTrue(barge.getPositions().get(0).isHit(), "Error: Position (5,5) should be marked as hit after shooting it.");
	}

	@Test
	void shoot2() {
		barge.shoot(new Position(1, 1));
		assertFalse(barge.getPositions().get(0).isHit(), "Error: Position (5,5) should not be hit when shooting at (1,1).");
	}

	// ----------------------------------------------------------------
	// occupies (CC = 2)
	// path 1: position found in list → returns true
	// path 2: position not found → returns false
	// ----------------------------------------------------------------

	@Test
	void occupies1() {
		assertTrue(barge.occupies(new Position(5, 5)), "Error: Barge should occupy position (5,5).");
	}

	@Test
	void occupies2() {
		assertFalse(barge.occupies(new Position(1, 1)), "Error: Barge should not occupy position (1,1).");
	}

	// ----------------------------------------------------------------
	// tooCloseTo(IShip) (CC = 2)
	// path 1: other ship has an adjacent position → returns true
	// path 2: other ship has no adjacent position → returns false
	// ----------------------------------------------------------------

	@Test
	void tooCloseToShip1() {
		Barge nearby = new Barge(Compass.NORTH, new Position(5, 6));
		assertTrue(barge.tooCloseTo(nearby), "Error: Barge at (5,6) should be too close to barge at (5,5).");
	}

	@Test
	void tooCloseToShip2() {
		Barge distant = new Barge(Compass.NORTH, new Position(0, 0));
		assertFalse(barge.tooCloseTo(distant), "Error: Barge at (0,0) should not be too close to barge at (5,5).");
	}

	// ----------------------------------------------------------------
	// tooCloseTo(IPosition) (CC = 2)
	// path 1: given position is adjacent to a ship position → returns true
	// path 2: given position is not adjacent → returns false
	// ----------------------------------------------------------------

	@Test
	void tooCloseToPosition1() {
		assertTrue(barge.tooCloseTo(new Position(5, 6)), "Error: Position (5,6) is adjacent to barge at (5,5), should be too close.");
	}

	@Test
	void tooCloseToPosition2() {
		assertFalse(barge.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) is not adjacent to barge at (5,5), should not be too close.");
	}

	// ----------------------------------------------------------------
	// getTopMostPos (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getTopMostPos() {
		assertEquals(5, barge.getTopMostPos(), "Error: Topmost row of barge at (5,5) should be 5.");
	}

	// ----------------------------------------------------------------
	// getBottomMostPos (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getBottomMostPos() {
		assertEquals(5, barge.getBottomMostPos(), "Error: Bottommost row of barge at (5,5) should be 5.");
	}

	// ----------------------------------------------------------------
	// getLeftMostPos (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getLeftMostPos() {
		assertEquals(5, barge.getLeftMostPos(), "Error: Leftmost column of barge at (5,5) should be 5.");
	}

	// ----------------------------------------------------------------
	// getRightMostPos (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getRightMostPos() {
		assertEquals(5, barge.getRightMostPos(), "Error: Rightmost column of barge at (5,5) should be 5.");
	}
}