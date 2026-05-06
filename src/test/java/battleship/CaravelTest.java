package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Caravel class.
 * Author: IGE-111245 Martim Reis
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor: 5  (switch com 4 cases + default implícito)
 * - getSize: 1
 * - stillFloating: 2
 * - getPositions: 1
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - getTopMostPos: 2
 * - getBottomMostPos: 2
 * - getLeftMostPos: 2
 * - getRightMostPos: 2
 */
public class CaravelTest {

	private Caravel caravel;

	@BeforeEach
	void setUp() {
		caravel = new Caravel(Compass.NORTH, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		caravel = null;
	}

	// ----------------------------------------------------------------
	// constructor (CC = 5 → 5 tests: NORTH, SOUTH, EAST, WEST, null)
	// NORTH e SOUTH geram posições verticais (mesmo comportamento)
	// EAST e WEST geram posições horizontais (mesmo comportamento)
	// ----------------------------------------------------------------

	@Test
	void constructor1() {
		// path: bearing == NORTH → posições verticais
		assertAll(
				() -> assertNotNull(caravel, "Error: Caravel with NORTH should not be null."),
				() -> assertEquals("Caravela", caravel.getCategory(), "Error: Category should be 'Caravela'."),
				() -> assertEquals(Compass.NORTH, caravel.getBearing(), "Error: Bearing should be NORTH."),
				() -> assertEquals(2, caravel.getSize(), "Error: Size should be 2."),
				() -> assertEquals(new Position(5, 5), caravel.getPositions().get(0), "Error: First position (NORTH) should be (5,5)."),
				() -> assertEquals(new Position(6, 5), caravel.getPositions().get(1), "Error: Second position (NORTH) should be (6,5).")
		);
	}

	@Test
	void constructor2() {
		// path: bearing == SOUTH → posições verticais (igual a NORTH no código)
		Caravel cS = new Caravel(Compass.SOUTH, new Position(5, 5));
		assertAll(
				() -> assertNotNull(cS, "Error: Caravel with SOUTH should not be null."),
				() -> assertEquals(Compass.SOUTH, cS.getBearing(), "Error: Bearing should be SOUTH."),
				() -> assertEquals(new Position(5, 5), cS.getPositions().get(0), "Error: First position (SOUTH) should be (5,5)."),
				() -> assertEquals(new Position(6, 5), cS.getPositions().get(1), "Error: Second position (SOUTH) should be (6,5).")
		);
	}

	@Test
	void constructor3() {
		// path: bearing == EAST → posições horizontais
		Caravel cE = new Caravel(Compass.EAST, new Position(5, 5));
		assertAll(
				() -> assertNotNull(cE, "Error: Caravel with EAST should not be null."),
				() -> assertEquals(Compass.EAST, cE.getBearing(), "Error: Bearing should be EAST."),
				() -> assertEquals(new Position(5, 5), cE.getPositions().get(0), "Error: First position (EAST) should be (5,5)."),
				() -> assertEquals(new Position(5, 6), cE.getPositions().get(1), "Error: Second position (EAST) should be (5,6).")
		);
	}

	@Test
	void constructor4() {
		// path: bearing == WEST → posições horizontais (igual a EAST no código)
		Caravel cW = new Caravel(Compass.WEST, new Position(5, 5));
		assertAll(
				() -> assertNotNull(cW, "Error: Caravel with WEST should not be null."),
				() -> assertEquals(Compass.WEST, cW.getBearing(), "Error: Bearing should be WEST."),
				() -> assertEquals(new Position(5, 5), cW.getPositions().get(0), "Error: First position (WEST) should be (5,5)."),
				() -> assertEquals(new Position(5, 6), cW.getPositions().get(1), "Error: Second position (WEST) should be (5,6).")
		);
	}

	@Test
	void constructor5() {
		// path: bearing == null → NullPointerException from Ship constructor
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> new Caravel(null, new Position(0, 0)),
				"Error: Null bearing should throw NullPointerException.");
		assertEquals("Ship's bearing must not be null", ex.getMessage(),
				"Error: Exception message should be 'Ship's bearing must not be null'.");
	}

	// ----------------------------------------------------------------
	// getSize (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getSize() {
		assertEquals(2, caravel.getSize(), "Error: Caravel size should be 2.");
	}

	// ----------------------------------------------------------------
	// stillFloating (CC = 2)
	// path 1: at least one position not hit → true
	// path 2: all positions hit → false
	// ----------------------------------------------------------------

	@Test
	void stillFloating1() {
		assertTrue(caravel.stillFloating(), "Error: Caravel should be floating when intact.");
	}

	@Test
	void stillFloating2() {
		caravel.getPositions().forEach(IPosition::shoot);
		assertFalse(caravel.stillFloating(), "Error: Caravel should not be floating when all positions are hit.");
	}

	// ----------------------------------------------------------------
	// getPositions (CC = 1)
	// ----------------------------------------------------------------

	@Test
	void getPositions() {
		assertAll(
				() -> assertEquals(2, caravel.getPositions().size(), "Error: Caravel should have exactly 2 positions."),
				() -> assertEquals(new Position(5, 5), caravel.getPositions().get(0), "Error: First position should be (5,5)."),
				() -> assertEquals(new Position(6, 5), caravel.getPositions().get(1), "Error: Second position should be (6,5).")
		);
	}

	// ----------------------------------------------------------------
	// shoot (CC = 2)
	// path 1: position found → marks as hit
	// path 2: position not found → no hit
	// ----------------------------------------------------------------

	@Test
	void shoot1() {
		caravel.shoot(new Position(5, 5));
		assertTrue(caravel.getPositions().get(0).isHit(), "Error: Position (5,5) should be marked as hit.");
	}

	@Test
	void shoot2() {
		caravel.shoot(new Position(0, 0));
		assertAll(
				() -> assertFalse(caravel.getPositions().get(0).isHit(), "Error: Position (5,5) should not be hit."),
				() -> assertFalse(caravel.getPositions().get(1).isHit(), "Error: Position (6,5) should not be hit.")
		);
	}

	// ----------------------------------------------------------------
	// occupies (CC = 2)
	// path 1: position found → true
	// path 2: position not found → false
	// ----------------------------------------------------------------

	@Test
	void occupies1() {
		assertTrue(caravel.occupies(new Position(6, 5)), "Error: Caravel should occupy position (6,5).");
	}

	@Test
	void occupies2() {
		assertFalse(caravel.occupies(new Position(0, 0)), "Error: Caravel should not occupy position (0,0).");
	}

	// ----------------------------------------------------------------
	// tooCloseTo(IShip) (CC = 2)
	// path 1: other ship has adjacent position → true
	// path 2: no adjacent position → false
	// ----------------------------------------------------------------

	@Test
	void tooCloseToShip1() {
		Caravel nearby = new Caravel(Compass.EAST, new Position(5, 7));
		assertTrue(caravel.tooCloseTo(nearby), "Error: Caravel at (5,7) EAST should be too close to caravel at (5,5) NORTH.");
	}

	@Test
	void tooCloseToShip2() {
		Caravel distant = new Caravel(Compass.NORTH, new Position(0, 0));
		assertFalse(caravel.tooCloseTo(distant), "Error: Caravel at (0,0) should not be too close to caravel at (5,5).");
	}

	// ----------------------------------------------------------------
	// tooCloseTo(IPosition) (CC = 2)
	// path 1: position is adjacent → true
	// path 2: position is not adjacent → false
	// ----------------------------------------------------------------

	@Test
	void tooCloseToPosition1() {
		assertTrue(caravel.tooCloseTo(new Position(4, 5)), "Error: Position (4,5) is adjacent to caravel at (5,5), should be too close.");
	}

	@Test
	void tooCloseToPosition2() {
		assertFalse(caravel.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) is not adjacent to caravel at (5,5).");
	}

	// ----------------------------------------------------------------
	// getTopMostPos (CC = 2)
	// path 1: first position has smallest row → returns it
	// path 2: another position has smaller row → returns that
	// (NORTH: top=5; EAST: top=5 — both same row, but loop runs 2 iterations)
	// ----------------------------------------------------------------

	@Test
	void getTopMostPos1() {
		// NORTH: positions (5,5) and (6,5) → topmost row = 5
		assertEquals(5, caravel.getTopMostPos(), "Error: Topmost row for NORTH caravel at (5,5) should be 5.");
	}

	@Test
	void getTopMostPos2() {
		// EAST: positions (5,5) and (5,6) → topmost row = 5
		Caravel cE = new Caravel(Compass.EAST, new Position(3, 3));
		assertEquals(3, cE.getTopMostPos(), "Error: Topmost row for EAST caravel at (3,3) should be 3.");
	}

	// ----------------------------------------------------------------
	// getBottomMostPos (CC = 2)
	// ----------------------------------------------------------------

	@Test
	void getBottomMostPos1() {
		// NORTH: positions (5,5) and (6,5) → bottommost row = 6
		assertEquals(6, caravel.getBottomMostPos(), "Error: Bottommost row for NORTH caravel at (5,5) should be 6.");
	}

	@Test
	void getBottomMostPos2() {
		// EAST: positions (5,5) and (5,6) → bottommost row = 5 (same row)
		Caravel cE = new Caravel(Compass.EAST, new Position(3, 3));
		assertEquals(3, cE.getBottomMostPos(), "Error: Bottommost row for EAST caravel at (3,3) should be 3.");
	}

	// ----------------------------------------------------------------
	// getLeftMostPos (CC = 2)
	// ----------------------------------------------------------------

	@Test
	void getLeftMostPos1() {
		// NORTH: positions (5,5) and (6,5) → leftmost col = 5
		assertEquals(5, caravel.getLeftMostPos(), "Error: Leftmost column for NORTH caravel at (5,5) should be 5.");
	}

	@Test
	void getLeftMostPos2() {
		// EAST: positions (5,5) and (5,6) → leftmost col = 5
		Caravel cE = new Caravel(Compass.EAST, new Position(3, 3));
		assertEquals(3, cE.getLeftMostPos(), "Error: Leftmost column for EAST caravel at (3,3) should be 3.");
	}

	// ----------------------------------------------------------------
	// getRightMostPos (CC = 2)
	// ----------------------------------------------------------------

	@Test
	void getRightMostPos1() {
		// NORTH: positions (5,5) and (6,5) → rightmost col = 5
		assertEquals(5, caravel.getRightMostPos(), "Error: Rightmost column for NORTH caravel at (5,5) should be 5.");
	}

	@Test
	void getRightMostPos2() {
		// EAST: positions (5,5) and (5,6) → rightmost col = 6
		Caravel cE = new Caravel(Compass.EAST, new Position(3, 3));
		assertEquals(5, cE.getRightMostPos(), "Error: Rightmost column for EAST caravel at (3,3) should be 5.");
	}
}