package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Frigate class.
 * Author: IGE-111245 (Martim Saldanha)
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor: 5  (switch com 4 cases)
 * - getSize: 1
 * - stillFloating: 2
 * - getPositions: 1
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - getTopMostPos: 4
 * - getBottomMostPos: 4
 * - getLeftMostPos: 4
 * - getRightMostPos: 4
 */
public class FrigateTest {

    private Frigate frigate;

    @BeforeEach
    void setUp() {
        frigate = new Frigate(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        frigate = null;
    }

    // ----------------------------------------------------------------
    // constructor (CC = 5)
    // ----------------------------------------------------------------

    @Test
    void constructor1() {
        // path: NORTH → 4 posições verticais
        assertAll(
                () -> assertNotNull(frigate, "Error: Frigate NORTH should not be null."),
                () -> assertEquals("Fragata", frigate.getCategory(), "Error: Category should be 'Fragata'."),
                () -> assertEquals(4, frigate.getSize(), "Error: Size should be 4."),
                () -> assertEquals(new Position(5, 5), frigate.getPositions().get(0), "Error: Pos 0 NORTH should be (5,5)."),
                () -> assertEquals(new Position(6, 5), frigate.getPositions().get(1), "Error: Pos 1 NORTH should be (6,5)."),
                () -> assertEquals(new Position(7, 5), frigate.getPositions().get(2), "Error: Pos 2 NORTH should be (7,5)."),
                () -> assertEquals(new Position(8, 5), frigate.getPositions().get(3), "Error: Pos 3 NORTH should be (8,5).")
        );
    }

    @Test
    void constructor2() {
        // path: SOUTH → 4 posições verticais (igual a NORTH no código)
        Frigate fS = new Frigate(Compass.SOUTH, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.SOUTH, fS.getBearing(), "Error: Bearing should be SOUTH."),
                () -> assertEquals(new Position(5, 5), fS.getPositions().get(0), "Error: Pos 0 SOUTH should be (5,5)."),
                () -> assertEquals(new Position(6, 5), fS.getPositions().get(1), "Error: Pos 1 SOUTH should be (6,5)."),
                () -> assertEquals(new Position(7, 5), fS.getPositions().get(2), "Error: Pos 2 SOUTH should be (7,5)."),
                () -> assertEquals(new Position(8, 5), fS.getPositions().get(3), "Error: Pos 3 SOUTH should be (8,5).")
        );
    }

    @Test
    void constructor3() {
        // path: EAST → 4 posições horizontais
        Frigate fE = new Frigate(Compass.EAST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.EAST, fE.getBearing(), "Error: Bearing should be EAST."),
                () -> assertEquals(new Position(5, 5), fE.getPositions().get(0), "Error: Pos 0 EAST should be (5,5)."),
                () -> assertEquals(new Position(5, 6), fE.getPositions().get(1), "Error: Pos 1 EAST should be (5,6)."),
                () -> assertEquals(new Position(5, 7), fE.getPositions().get(2), "Error: Pos 2 EAST should be (5,7)."),
                () -> assertEquals(new Position(5, 8), fE.getPositions().get(3), "Error: Pos 3 EAST should be (5,8).")
        );
    }

    @Test
    void constructor4() {
        // path: WEST → 4 posições horizontais (igual a EAST no código)
        Frigate fW = new Frigate(Compass.WEST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.WEST, fW.getBearing(), "Error: Bearing should be WEST."),
                () -> assertEquals(new Position(5, 5), fW.getPositions().get(0), "Error: Pos 0 WEST should be (5,5)."),
                () -> assertEquals(new Position(5, 6), fW.getPositions().get(1), "Error: Pos 1 WEST should be (5,6)."),
                () -> assertEquals(new Position(5, 7), fW.getPositions().get(2), "Error: Pos 2 WEST should be (5,7)."),
                () -> assertEquals(new Position(5, 8), fW.getPositions().get(3), "Error: Pos 3 WEST should be (5,8).")
        );
    }

    @Test
    void constructor5() {
        // path: null bearing → NullPointerException
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new Frigate(null, new Position(5, 5)),
                "Error: Null bearing should throw NullPointerException.");
        assertEquals("Ship's bearing must not be null", ex.getMessage(),
                "Error: Exception message should be 'Ship's bearing must not be null'.");
    }

    // ----------------------------------------------------------------
    // getSize (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getSize() {
        assertEquals(4, frigate.getSize(), "Error: Frigate size should be 4.");
    }

    // ----------------------------------------------------------------
    // stillFloating (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void stillFloating1() {
        // path: at least one not hit → true
        assertTrue(frigate.stillFloating(), "Error: Frigate should be floating when intact.");
    }

    @Test
    void stillFloating2() {
        // path: all hit → false
        frigate.getPositions().forEach(IPosition::shoot);
        assertFalse(frigate.stillFloating(), "Error: Frigate should not float when all 4 positions are hit.");
    }

    // ----------------------------------------------------------------
    // getPositions (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getPositions() {
        assertAll(
                () -> assertEquals(4, frigate.getPositions().size(), "Error: Frigate should have exactly 4 positions."),
                () -> assertEquals(new Position(5, 5), frigate.getPositions().get(0), "Error: Pos 0 should be (5,5)."),
                () -> assertEquals(new Position(6, 5), frigate.getPositions().get(1), "Error: Pos 1 should be (6,5)."),
                () -> assertEquals(new Position(7, 5), frigate.getPositions().get(2), "Error: Pos 2 should be (7,5)."),
                () -> assertEquals(new Position(8, 5), frigate.getPositions().get(3), "Error: Pos 3 should be (8,5).")
        );
    }

    // ----------------------------------------------------------------
    // shoot (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void shoot1() {
        // path: position found → hit
        frigate.shoot(new Position(8, 5));
        assertTrue(frigate.getPositions().get(3).isHit(), "Error: Last position (8,5) should be marked as hit.");
    }

    @Test
    void shoot2() {
        // path: position not found → no hit
        frigate.shoot(new Position(0, 0));
        frigate.getPositions().forEach(p ->
                assertFalse(p.isHit(), "Error: No position should be hit when shooting at (0,0)."));
    }

    // ----------------------------------------------------------------
    // occupies (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void occupies1() {
        assertTrue(frigate.occupies(new Position(7, 5)), "Error: Frigate should occupy position (7,5).");
    }

    @Test
    void occupies2() {
        assertFalse(frigate.occupies(new Position(0, 0)), "Error: Frigate should not occupy position (0,0).");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IShip) (CC = 2)
    // ----------------------------------------------------------------
    @Test
    void tooCloseToShip1() {
        // Frigate NORTH at (5,5) ocupa (5,5),(6,5),(7,5),(8,5)
        // Barge at (5,6) é adjacente a (5,5) → demasiado perto
        Barge nearby = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(frigate.tooCloseTo(nearby),
                "Error: Barge at (5,6) should be too close to Frigate NORTH at (5,5).");
    }
    @Test
    void tooCloseToShip2() {
        Frigate distant = new Frigate(Compass.EAST, new Position(0, 0));
        assertFalse(frigate.tooCloseTo(distant), "Error: Frigate at (0,0) should not be too close.");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IPosition) (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void tooCloseToPosition1() {
        assertTrue(frigate.tooCloseTo(new Position(4, 5)), "Error: Position (4,5) is adjacent, should be too close.");
    }

    @Test
    void tooCloseToPosition2() {
        assertFalse(frigate.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) should not be too close.");
    }

    // ----------------------------------------------------------------
    // getTopMostPos (CC = 4)
    // loop runs size-1=3 times; replacement can happen at iteration 1, 2, or 3
    // ----------------------------------------------------------------

    @Test
    void getTopMostPos1() {
        // NORTH (5,5)→(8,5): first element is already topmost
        assertEquals(5, frigate.getTopMostPos(), "Error: Topmost row for NORTH frigate at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos2() {
        // EAST (3,3)→(3,6): all same row, loop finds no smaller
        Frigate fE = new Frigate(Compass.EAST, new Position(3, 3));
        assertEquals(3, fE.getTopMostPos(), "Error: Topmost row for EAST frigate at (3,3) should be 3.");
    }

    @Test
    void getTopMostPos3() {
        Frigate fN = new Frigate(Compass.NORTH, new Position(1, 1));
        assertEquals(1, fN.getTopMostPos(), "Error: Topmost row for NORTH frigate at (1,1) should be 1.");
    }

    @Test
    void getTopMostPos4() {
        Frigate fE = new Frigate(Compass.EAST, new Position(0, 0));
        assertEquals(0, fE.getTopMostPos(), "Error: Topmost row for EAST frigate at (0,0) should be 0.");
    }

    // ----------------------------------------------------------------
    // getBottomMostPos (CC = 4)
    // ----------------------------------------------------------------

    @Test
    void getBottomMostPos1() {
        // NORTH: (5,5)→(8,5) → bottom = 8
        assertEquals(8, frigate.getBottomMostPos(), "Error: Bottommost row for NORTH frigate at (5,5) should be 8.");
    }

    @Test
    void getBottomMostPos2() {
        // EAST: (3,3)→(3,6) → bottom = 3
        Frigate fE = new Frigate(Compass.EAST, new Position(3, 3));
        assertEquals(3, fE.getBottomMostPos(), "Error: Bottommost row for EAST frigate at (3,3) should be 3.");
    }

    @Test
    void getBottomMostPos3() {
        Frigate fN = new Frigate(Compass.NORTH, new Position(1, 1));
        assertEquals(4, fN.getBottomMostPos(), "Error: Bottommost row for NORTH frigate at (1,1) should be 4.");
    }

    @Test
    void getBottomMostPos4() {
        Frigate fE = new Frigate(Compass.EAST, new Position(0, 0));
        assertEquals(0, fE.getBottomMostPos(), "Error: Bottommost row for EAST frigate at (0,0) should be 0.");
    }

    // ----------------------------------------------------------------
    // getLeftMostPos (CC = 4)
    // ----------------------------------------------------------------

    @Test
    void getLeftMostPos1() {
        // NORTH: all col=5 → left = 5
        assertEquals(5, frigate.getLeftMostPos(), "Error: Leftmost col for NORTH frigate at (5,5) should be 5.");
    }

    @Test
    void getLeftMostPos2() {
        // EAST: (3,3)→(3,6) → left = 3
        Frigate fE = new Frigate(Compass.EAST, new Position(3, 3));
        assertEquals(3, fE.getLeftMostPos(), "Error: Leftmost col for EAST frigate at (3,3) should be 3.");
    }

    @Test
    void getLeftMostPos3() {
        Frigate fN = new Frigate(Compass.NORTH, new Position(1, 2));
        assertEquals(2, fN.getLeftMostPos(), "Error: Leftmost col for NORTH frigate at (1,2) should be 2.");
    }

    @Test
    void getLeftMostPos4() {
        Frigate fE = new Frigate(Compass.EAST, new Position(0, 0));
        assertEquals(0, fE.getLeftMostPos(), "Error: Leftmost col for EAST frigate at (0,0) should be 0.");
    }

    // ----------------------------------------------------------------
    // getRightMostPos (CC = 4)
    // ----------------------------------------------------------------

    @Test
    void getRightMostPos1() {
        // NORTH: all col=5 → right = 5
        assertEquals(5, frigate.getRightMostPos(), "Error: Rightmost col for NORTH frigate at (5,5) should be 5.");
    }

    @Test
    void getRightMostPos2() {
        // EAST: (3,3)→(3,6) → right = 6
        Frigate fE = new Frigate(Compass.EAST, new Position(3, 3));
        assertEquals(6, fE.getRightMostPos(), "Error: Rightmost col for EAST frigate at (3,3) should be 6.");
    }

    @Test
    void getRightMostPos3() {
        Frigate fN = new Frigate(Compass.NORTH, new Position(1, 2));
        assertEquals(2, fN.getRightMostPos(), "Error: Rightmost col for NORTH frigate at (1,2) should be 2.");
    }

    @Test
    void getRightMostPos4() {
        Frigate fE = new Frigate(Compass.EAST, new Position(0, 0));
        assertEquals(3, fE.getRightMostPos(), "Error: Rightmost col for EAST frigate at (0,0) should be 3.");
    }
}