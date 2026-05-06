package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Galleon class.
 * Author: IGE-111245 (Martim Saldanha)
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor: 5  (switch com 4 cases)
 * - fillNorth: 1
 * - fillSouth: 1
 * - fillEast: 1
 * - fillWest: 1
 * - getSize: 1
 * - stillFloating: 2
 * - getPositions: 1
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - getTopMostPos: 5
 * - getBottomMostPos: 5
 * - getLeftMostPos: 5
 * - getRightMostPos: 5
 */
public class GalleonTest {

    private Galleon galleon;

    @BeforeEach
    void setUp() {
        galleon = new Galleon(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        galleon = null;
    }

    // ----------------------------------------------------------------
    // constructor (CC = 5)
    // Galleon forma T:
    //   NORTH pos=(r,c): (r,c),(r,c+1),(r,c+2),(r+1,c+1),(r+2,c+1)
    //   SOUTH pos=(r,c): (r,c),(r+1,c),(r+2,c-1),(r+2,c),(r+2,c+1)
    //   EAST  pos=(r,c): (r,c),(r+1,c-2),(r+1,c-1),(r+1,c),(r+2,c)
    //   WEST  pos=(r,c): (r,c),(r+1,c),(r+1,c+1),(r+1,c+2),(r+2,c)
    // ----------------------------------------------------------------

    @Test
    void constructor1() {
        // path: NORTH
        assertAll(
                () -> assertNotNull(galleon, "Error: Galleon NORTH should not be null."),
                () -> assertEquals("Galeao", galleon.getCategory(), "Error: Category should be 'Galeao'."),
                () -> assertEquals(5, galleon.getSize(), "Error: Size should be 5."),
                () -> assertEquals(new Position(5, 5), galleon.getPositions().get(0), "Error: NORTH pos 0 should be (5,5)."),
                () -> assertEquals(new Position(5, 6), galleon.getPositions().get(1), "Error: NORTH pos 1 should be (5,6)."),
                () -> assertEquals(new Position(5, 7), galleon.getPositions().get(2), "Error: NORTH pos 2 should be (5,7)."),
                () -> assertEquals(new Position(6, 6), galleon.getPositions().get(3), "Error: NORTH pos 3 should be (6,6)."),
                () -> assertEquals(new Position(7, 6), galleon.getPositions().get(4), "Error: NORTH pos 4 should be (7,6).")
        );
    }

    @Test
    void constructor2() {
        // path: SOUTH
        Galleon gS = new Galleon(Compass.SOUTH, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.SOUTH, gS.getBearing(), "Error: Bearing should be SOUTH."),
                () -> assertEquals(new Position(5, 5), gS.getPositions().get(0), "Error: SOUTH pos 0 should be (5,5)."),
                () -> assertEquals(new Position(6, 5), gS.getPositions().get(1), "Error: SOUTH pos 1 should be (6,5)."),
                () -> assertEquals(new Position(7, 4), gS.getPositions().get(2), "Error: SOUTH pos 2 should be (7,4)."),
                () -> assertEquals(new Position(7, 5), gS.getPositions().get(3), "Error: SOUTH pos 3 should be (7,5)."),
                () -> assertEquals(new Position(7, 6), gS.getPositions().get(4), "Error: SOUTH pos 4 should be (7,6).")
        );
    }

    @Test
    void constructor3() {
        // path: EAST
        Galleon gE = new Galleon(Compass.EAST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.EAST, gE.getBearing(), "Error: Bearing should be EAST."),
                () -> assertEquals(new Position(5, 5), gE.getPositions().get(0), "Error: EAST pos 0 should be (5,5)."),
                () -> assertEquals(new Position(6, 3), gE.getPositions().get(1), "Error: EAST pos 1 should be (6,3)."),
                () -> assertEquals(new Position(6, 4), gE.getPositions().get(2), "Error: EAST pos 2 should be (6,4)."),
                () -> assertEquals(new Position(6, 5), gE.getPositions().get(3), "Error: EAST pos 3 should be (6,5)."),
                () -> assertEquals(new Position(7, 5), gE.getPositions().get(4), "Error: EAST pos 4 should be (7,5).")
        );
    }

    @Test
    void constructor4() {
        // path: WEST
        Galleon gW = new Galleon(Compass.WEST, new Position(5, 5));
        assertAll(
                () -> assertEquals(Compass.WEST, gW.getBearing(), "Error: Bearing should be WEST."),
                () -> assertEquals(new Position(5, 5), gW.getPositions().get(0), "Error: WEST pos 0 should be (5,5)."),
                () -> assertEquals(new Position(6, 5), gW.getPositions().get(1), "Error: WEST pos 1 should be (6,5)."),
                () -> assertEquals(new Position(6, 6), gW.getPositions().get(2), "Error: WEST pos 2 should be (6,6)."),
                () -> assertEquals(new Position(6, 7), gW.getPositions().get(3), "Error: WEST pos 3 should be (6,7)."),
                () -> assertEquals(new Position(7, 5), gW.getPositions().get(4), "Error: WEST pos 4 should be (7,5).")
        );
    }

    @Test
    void constructor5() {
        // path: null bearing → NullPointerException
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new Galleon(null, new Position(5, 5)),
                "Error: Null bearing should throw NullPointerException.");
        assertEquals("Ship's bearing must not be null", ex.getMessage(),
                "Error: Exception message should be 'Ship's bearing must not be null'.");
    }

    // ----------------------------------------------------------------
    // getSize (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getSize() {
        assertEquals(5, galleon.getSize(), "Error: Galleon size should be 5.");
    }

    // ----------------------------------------------------------------
    // stillFloating (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void stillFloating1() {
        // path: at least one not hit → true
        assertTrue(galleon.stillFloating(), "Error: Galleon should be floating when intact.");
    }

    @Test
    void stillFloating2() {
        // path: all 5 hit → false
        galleon.getPositions().forEach(IPosition::shoot);
        assertFalse(galleon.stillFloating(), "Error: Galleon should not float when all 5 positions are hit.");
    }

    // ----------------------------------------------------------------
    // getPositions (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getPositions() {
        assertAll(
                () -> assertEquals(5, galleon.getPositions().size(), "Error: Galleon should have exactly 5 positions."),
                () -> assertEquals(new Position(5, 5), galleon.getPositions().get(0), "Error: Pos 0 should be (5,5)."),
                () -> assertEquals(new Position(5, 6), galleon.getPositions().get(1), "Error: Pos 1 should be (5,6)."),
                () -> assertEquals(new Position(5, 7), galleon.getPositions().get(2), "Error: Pos 2 should be (5,7)."),
                () -> assertEquals(new Position(6, 6), galleon.getPositions().get(3), "Error: Pos 3 should be (6,6)."),
                () -> assertEquals(new Position(7, 6), galleon.getPositions().get(4), "Error: Pos 4 should be (7,6).")
        );
    }

    // ----------------------------------------------------------------
    // shoot (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void shoot1() {
        // path: position found → hit
        galleon.shoot(new Position(7, 6));
        assertTrue(galleon.getPositions().get(4).isHit(), "Error: Position (7,6) should be marked as hit.");
    }

    @Test
    void shoot2() {
        // path: position not found → no hit
        galleon.shoot(new Position(0, 0));
        galleon.getPositions().forEach(p ->
                assertFalse(p.isHit(), "Error: No position should be hit when shooting at (0,0)."));
    }

    // ----------------------------------------------------------------
    // occupies (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void occupies1() {
        assertTrue(galleon.occupies(new Position(6, 6)), "Error: Galleon should occupy position (6,6).");
    }

    @Test
    void occupies2() {
        assertFalse(galleon.occupies(new Position(0, 0)), "Error: Galleon should not occupy position (0,0).");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IShip) (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void tooCloseToShip1() {
        Barge nearby = new Barge(Compass.NORTH, new Position(5, 8));
        assertTrue(galleon.tooCloseTo(nearby), "Error: Barge at (5,8) should be too close to galleon NORTH at (5,5).");
    }

    @Test
    void tooCloseToShip2() {
        Barge distant = new Barge(Compass.NORTH, new Position(0, 0));
        assertFalse(galleon.tooCloseTo(distant), "Error: Barge at (0,0) should not be too close to galleon.");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IPosition) (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void tooCloseToPosition1() {
        // pos (5,4) é adjacente a (5,5)
        assertTrue(galleon.tooCloseTo(new Position(5, 4)), "Error: Position (5,4) is adjacent to galleon, should be too close.");
    }

    @Test
    void tooCloseToPosition2() {
        assertFalse(galleon.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) should not be too close to galleon.");
    }

    // ----------------------------------------------------------------
    // getTopMostPos (CC = 5)
    // loop corre 4 vezes (size-1); cada iteração pode ou não actualizar top
    // ----------------------------------------------------------------

    @Test
    void getTopMostPos1() {
        // NORTH (5,5)→(7,6): row 5 é o menor → top = 5
        assertEquals(5, galleon.getTopMostPos(), "Error: Topmost row for NORTH galleon at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos2() {
        // SOUTH (5,5)→(7,6): row 5 é o menor → top = 5
        Galleon gS = new Galleon(Compass.SOUTH, new Position(5, 5));
        assertEquals(5, gS.getTopMostPos(), "Error: Topmost row for SOUTH galleon at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos3() {
        // EAST (5,5)→(7,5): row 5 é o menor → top = 5
        Galleon gE = new Galleon(Compass.EAST, new Position(5, 5));
        assertEquals(5, gE.getTopMostPos(), "Error: Topmost row for EAST galleon at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos4() {
        // WEST (5,5)→(7,5): row 5 é o menor → top = 5
        Galleon gW = new Galleon(Compass.WEST, new Position(5, 5));
        assertEquals(5, gW.getTopMostPos(), "Error: Topmost row for WEST galleon at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos5() {
        // Diferente posição de origem para verificar caminho diferente
        Galleon gN = new Galleon(Compass.NORTH, new Position(2, 2));
        assertEquals(2, gN.getTopMostPos(), "Error: Topmost row for NORTH galleon at (2,2) should be 2.");
    }

    // ----------------------------------------------------------------
    // getBottomMostPos (CC = 5)
    // ----------------------------------------------------------------

    @Test
    void getBottomMostPos1() {
        // NORTH at (5,5): rows 5,5,5,6,7 → bottom = 7
        assertEquals(7, galleon.getBottomMostPos(), "Error: Bottommost row for NORTH galleon at (5,5) should be 7.");
    }

    @Test
    void getBottomMostPos2() {
        // SOUTH at (5,5): rows 5,6,7,7,7 → bottom = 7
        Galleon gS = new Galleon(Compass.SOUTH, new Position(5, 5));
        assertEquals(7, gS.getBottomMostPos(), "Error: Bottommost row for SOUTH galleon at (5,5) should be 7.");
    }

    @Test
    void getBottomMostPos3() {
        // EAST at (5,5): rows 5,6,6,6,7 → bottom = 7
        Galleon gE = new Galleon(Compass.EAST, new Position(5, 5));
        assertEquals(7, gE.getBottomMostPos(), "Error: Bottommost row for EAST galleon at (5,5) should be 7.");
    }

    @Test
    void getBottomMostPos4() {
        // WEST at (5,5): rows 5,6,6,6,7 → bottom = 7
        Galleon gW = new Galleon(Compass.WEST, new Position(5, 5));
        assertEquals(7, gW.getBottomMostPos(), "Error: Bottommost row for WEST galleon at (5,5) should be 7.");
    }

    @Test
    void getBottomMostPos5() {
        Galleon gN = new Galleon(Compass.NORTH, new Position(1, 1));
        assertEquals(3, gN.getBottomMostPos(), "Error: Bottommost row for NORTH galleon at (1,1) should be 3.");
    }

    // ----------------------------------------------------------------
    // getLeftMostPos (CC = 5)
    // ----------------------------------------------------------------

    @Test
    void getLeftMostPos1() {
        // NORTH at (5,5): cols 5,6,7,6,6 → left = 5
        assertEquals(5, galleon.getLeftMostPos(), "Error: Leftmost col for NORTH galleon at (5,5) should be 5.");
    }

    @Test
    void getLeftMostPos2() {
        // SOUTH at (5,5): cols 5,5,4,5,6 → left = 4
        Galleon gS = new Galleon(Compass.SOUTH, new Position(5, 5));
        assertEquals(4, gS.getLeftMostPos(), "Error: Leftmost col for SOUTH galleon at (5,5) should be 4.");
    }

    @Test
    void getLeftMostPos3() {
        // EAST at (5,5): cols 5,3,4,5,5 → left = 3
        Galleon gE = new Galleon(Compass.EAST, new Position(5, 5));
        assertEquals(3, gE.getLeftMostPos(), "Error: Leftmost col for EAST galleon at (5,5) should be 3.");
    }

    @Test
    void getLeftMostPos4() {
        // WEST at (5,5): cols 5,5,6,7,5 → left = 5
        Galleon gW = new Galleon(Compass.WEST, new Position(5, 5));
        assertEquals(5, gW.getLeftMostPos(), "Error: Leftmost col for WEST galleon at (5,5) should be 5.");
    }

    @Test
    void getLeftMostPos5() {
        Galleon gN = new Galleon(Compass.NORTH, new Position(1, 1));
        assertEquals(1, gN.getLeftMostPos(), "Error: Leftmost col for NORTH galleon at (1,1) should be 1.");
    }

    // ----------------------------------------------------------------
    // getRightMostPos (CC = 5)
    // ----------------------------------------------------------------

    @Test
    void getRightMostPos1() {
        // NORTH at (5,5): cols 5,6,7,6,6 → right = 7
        assertEquals(7, galleon.getRightMostPos(), "Error: Rightmost col for NORTH galleon at (5,5) should be 7.");
    }

    @Test
    void getRightMostPos2() {
        // SOUTH at (5,5): cols 5,5,4,5,6 → right = 6
        Galleon gS = new Galleon(Compass.SOUTH, new Position(5, 5));
        assertEquals(6, gS.getRightMostPos(), "Error: Rightmost col for SOUTH galleon at (5,5) should be 6.");
    }

    @Test
    void getRightMostPos3() {
        // EAST at (5,5): cols 5,3,4,5,5 → right = 5
        Galleon gE = new Galleon(Compass.EAST, new Position(5, 5));
        assertEquals(5, gE.getRightMostPos(), "Error: Rightmost col for EAST galleon at (5,5) should be 5.");
    }

    @Test
    void getRightMostPos4() {
        // WEST at (5,5): cols 5,5,6,7,5 → right = 7
        Galleon gW = new Galleon(Compass.WEST, new Position(5, 5));
        assertEquals(7, gW.getRightMostPos(), "Error: Rightmost col for WEST galleon at (5,5) should be 7.");
    }

    @Test
    void getRightMostPos5() {
        Galleon gN = new Galleon(Compass.NORTH, new Position(1, 1));
        assertEquals(3, gN.getRightMostPos(), "Error: Rightmost col for NORTH galleon at (1,1) should be 3.");
    }
}