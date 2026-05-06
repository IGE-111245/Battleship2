package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Carrack class.
 * Author: IGE-111245 Martim Reis
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
 * - getTopMostPos: 3  (loop com comparação: init + 2 iterações possíveis)
 * - getBottomMostPos: 3
 * - getLeftMostPos: 3
 * - getRightMostPos: 3
 */
public class CarrackTest {

    private Carrack carrack;

    @BeforeEach
    void setUp() {
        carrack = new Carrack(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        carrack = null;
    }

    // ----------------------------------------------------------------
    // constructor (CC = 5)
    // ----------------------------------------------------------------

    @Test
    void constructor1() {
        // path: NORTH → posições verticais
        assertAll(
                () -> assertNotNull(carrack, "Error: Carrack NORTH should not be null."),
                () -> assertEquals("Nau", carrack.getCategory(), "Error: Category should be 'Nau'."),
                () -> assertEquals(3, carrack.getSize(), "Error: Size should be 3."),
                () -> assertEquals(new Position(5, 5), carrack.getPositions().get(0), "Error: First pos NORTH should be (5,5)."),
                () -> assertEquals(new Position(6, 5), carrack.getPositions().get(1), "Error: Second pos NORTH should be (6,5)."),
                () -> assertEquals(new Position(7, 5), carrack.getPositions().get(2), "Error: Third pos NORTH should be (7,5).")
        );
    }

    @Test
    void constructor2() {
        // path: SOUTH → posições verticais (igual a NORTH no código)
        Carrack cS = new Carrack(Compass.SOUTH, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.SOUTH, cS.getBearing(), "Error: Bearing should be SOUTH."),
                () -> assertEquals(new Position(5, 5), cS.getPositions().get(0), "Error: First pos SOUTH should be (5,5)."),
                () -> assertEquals(new Position(6, 5), cS.getPositions().get(1), "Error: Second pos SOUTH should be (6,5)."),
                () -> assertEquals(new Position(7, 5), cS.getPositions().get(2), "Error: Third pos SOUTH should be (7,5).")
        );
    }

    @Test
    void constructor3() {
        // path: EAST → posições horizontais
        Carrack cE = new Carrack(Compass.EAST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.EAST, cE.getBearing(), "Error: Bearing should be EAST."),
                () -> assertEquals(new Position(5, 5), cE.getPositions().get(0), "Error: First pos EAST should be (5,5)."),
                () -> assertEquals(new Position(5, 6), cE.getPositions().get(1), "Error: Second pos EAST should be (5,6)."),
                () -> assertEquals(new Position(5, 7), cE.getPositions().get(2), "Error: Third pos EAST should be (5,7).")
        );
    }

    @Test
    void constructor4() {
        // path: WEST → posições horizontais (igual a EAST no código)
        Carrack cW = new Carrack(Compass.WEST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.WEST, cW.getBearing(), "Error: Bearing should be WEST."),
                () -> assertEquals(new Position(5, 5), cW.getPositions().get(0), "Error: First pos WEST should be (5,5)."),
                () -> assertEquals(new Position(5, 6), cW.getPositions().get(1), "Error: Second pos WEST should be (5,6)."),
                () -> assertEquals(new Position(5, 7), cW.getPositions().get(2), "Error: Third pos WEST should be (5,7).")
        );
    }

    @Test
    void constructor5() {
        // path: null bearing → NullPointerException
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new Carrack(null, new Position(5, 5)),
                "Error: Null bearing should throw NullPointerException.");
        assertEquals("Ship's bearing must not be null", ex.getMessage(),
                "Error: Exception message should be 'Ship's bearing must not be null'.");
    }

    // ----------------------------------------------------------------
    // getSize (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getSize() {
        assertEquals(3, carrack.getSize(), "Error: Carrack size should be 3.");
    }

    // ----------------------------------------------------------------
    // stillFloating (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void stillFloating1() {
        // path: at least one not hit → true
        assertTrue(carrack.stillFloating(), "Error: Carrack should be floating when intact.");
    }

    @Test
    void stillFloating2() {
        // path: all hit → false
        carrack.getPositions().forEach(IPosition::shoot);
        assertFalse(carrack.stillFloating(), "Error: Carrack should not be floating when all positions are hit.");
    }

    // ----------------------------------------------------------------
    // getPositions (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getPositions() {
        assertAll(
                () -> assertEquals(3, carrack.getPositions().size(), "Error: Carrack should have exactly 3 positions."),
                () -> assertEquals(new Position(5, 5), carrack.getPositions().get(0), "Error: First position should be (5,5)."),
                () -> assertEquals(new Position(6, 5), carrack.getPositions().get(1), "Error: Second position should be (6,5)."),
                () -> assertEquals(new Position(7, 5), carrack.getPositions().get(2), "Error: Third position should be (7,5).")
        );
    }

    // ----------------------------------------------------------------
    // shoot (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void shoot1() {
        // path: position matches → hit
        carrack.shoot(new Position(6, 5));
        assertTrue(carrack.getPositions().get(1).isHit(), "Error: Position (6,5) should be marked as hit.");
    }

    @Test
    void shoot2() {
        // path: position does not match → no hit
        carrack.shoot(new Position(0, 0));
        assertAll(
                () -> assertFalse(carrack.getPositions().get(0).isHit(), "Error: (5,5) should not be hit."),
                () -> assertFalse(carrack.getPositions().get(1).isHit(), "Error: (6,5) should not be hit."),
                () -> assertFalse(carrack.getPositions().get(2).isHit(), "Error: (7,5) should not be hit.")
        );
    }

    // ----------------------------------------------------------------
    // occupies (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void occupies1() {
        assertTrue(carrack.occupies(new Position(7, 5)), "Error: Carrack should occupy position (7,5).");
    }

    @Test
    void occupies2() {
        assertFalse(carrack.occupies(new Position(0, 0)), "Error: Carrack should not occupy position (0,0).");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IShip) (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void tooCloseToShip1() {
        Carrack nearby = new Carrack(Compass.EAST, new Position(5, 7));
        assertTrue(carrack.tooCloseTo(nearby), "Error: Carrack at (5,7) EAST should be too close.");
    }

    @Test
    void tooCloseToShip2() {
        Carrack distant = new Carrack(Compass.NORTH, new Position(0, 0));
        assertFalse(carrack.tooCloseTo(distant), "Error: Carrack at (0,0) should not be too close.");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IPosition) (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void tooCloseToPosition1() {
        assertTrue(carrack.tooCloseTo(new Position(4, 5)), "Error: Position (4,5) is adjacent to carrack, should be too close.");
    }

    @Test
    void tooCloseToPosition2() {
        assertFalse(carrack.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) should not be too close to carrack at (5,5).");
    }

    // ----------------------------------------------------------------
    // getTopMostPos (CC = 3)
    // path 1: first position has lowest row (no replacement in loop)
    // path 2: a later position has lower row (replacement occurs once)
    // path 3: loop ends without replacement after multiple iterations
    // ----------------------------------------------------------------

    @Test
    void getTopMostPos1() {
        // NORTH: (5,5),(6,5),(7,5) → top = 5 (first is already smallest)
        assertEquals(5, carrack.getTopMostPos(), "Error: Topmost row for NORTH carrack at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos2() {
        // EAST: (5,5),(5,6),(5,7) → top = 5
        Carrack cE = new Carrack(Compass.EAST, new Position(3, 3));
        assertEquals(3, cE.getTopMostPos(), "Error: Topmost row for EAST carrack at (3,3) should be 3.");
    }

    @Test
    void getTopMostPos3() {
        // Verify loop traverses all 3 positions
        Carrack cN = new Carrack(Compass.NORTH, new Position(2, 2));
        assertEquals(2, cN.getTopMostPos(), "Error: Topmost row for NORTH carrack at (2,2) should be 2.");
    }

    // ----------------------------------------------------------------
    // getBottomMostPos (CC = 3)
    // ----------------------------------------------------------------

    @Test
    void getBottomMostPos1() {
        // NORTH: (5,5),(6,5),(7,5) → bottom = 7
        assertEquals(7, carrack.getBottomMostPos(), "Error: Bottommost row for NORTH carrack at (5,5) should be 7.");
    }

    @Test
    void getBottomMostPos2() {
        // EAST: (5,5),(5,6),(5,7) → bottom = 5
        Carrack cE = new Carrack(Compass.EAST, new Position(3, 3));
        assertEquals(3, cE.getBottomMostPos(), "Error: Bottommost row for EAST carrack at (3,3) should be 3.");
    }

    @Test
    void getBottomMostPos3() {
        Carrack cN = new Carrack(Compass.NORTH, new Position(1, 1));
        assertEquals(3, cN.getBottomMostPos(), "Error: Bottommost row for NORTH carrack at (1,1) should be 3.");
    }

    // ----------------------------------------------------------------
    // getLeftMostPos (CC = 3)
    // ----------------------------------------------------------------

    @Test
    void getLeftMostPos1() {
        // NORTH: (5,5),(6,5),(7,5) → left = 5
        assertEquals(5, carrack.getLeftMostPos(), "Error: Leftmost col for NORTH carrack at (5,5) should be 5.");
    }

    @Test
    void getLeftMostPos2() {
        // EAST: (3,3),(3,4),(3,5) → left = 3
        Carrack cE = new Carrack(Compass.EAST, new Position(3, 3));
        assertEquals(3, cE.getLeftMostPos(), "Error: Leftmost col for EAST carrack at (3,3) should be 3.");
    }

    @Test
    void getLeftMostPos3() {
        Carrack cN = new Carrack(Compass.NORTH, new Position(1, 2));
        assertEquals(2, cN.getLeftMostPos(), "Error: Leftmost col for NORTH carrack at (1,2) should be 2.");
    }

    // ----------------------------------------------------------------
    // getRightMostPos (CC = 3)
    // ----------------------------------------------------------------

    @Test
    void getRightMostPos1() {
        // NORTH: (5,5),(6,5),(7,5) → right = 5
        assertEquals(5, carrack.getRightMostPos(), "Error: Rightmost col for NORTH carrack at (5,5) should be 5.");
    }

    @Test
    void getRightMostPos2() {
        // EAST: (3,3),(3,4),(3,5) → right = 5
        Carrack cE = new Carrack(Compass.EAST, new Position(3, 3));
        assertEquals(5, cE.getRightMostPos(), "Error: Rightmost col for EAST carrack at (3,3) should be 5.");
    }

    @Test
    void getRightMostPos3() {
        Carrack cN = new Carrack(Compass.NORTH, new Position(1, 2));
        assertEquals(2, cN.getRightMostPos(), "Error: Rightmost col for NORTH carrack at (1,2) should be 2.");
    }
}