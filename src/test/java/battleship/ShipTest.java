package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the Ship abstract class.
 * Author: IGE-111245 (Martim Saldanha) / IGE-111519 (Vicente Viela)
 * Date: 2025/2026
 *
 * Ship is abstract — instantiated via Barge (size 1) and Caravel (size 2).
 *
 * Cyclomatic Complexity:
 * - buildShip: 6  (switch: barca, caravela, nau, fragata, galeao, default)
 * - constructor: 1
 * - getCategory: 1
 * - getPositions: 1
 * - getAdjacentPositions: 3  (outer loop + inner loop + duplicate check)
 * - getPosition: 1
 * - getBearing: 1
 * - getSize: 1
 * - stillFloating: 2
 * - getTopMostPos: 2
 * - getBottomMostPos: 2
 * - getLeftMostPos: 2
 * - getRightMostPos: 2
 * - occupies: 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - shoot: 2
 * - sink: 1
 * - toString: 1
 */
public class ShipTest {

    private Ship ship;

    @BeforeEach
    void setUp() {
        // Ship is abstract — use Barge as concrete subclass (size 1)
        ship = new Barge(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        ship = null;
    }

    // ----------------------------------------------------------------
    // buildShip (CC = 6)
    // path 1: "barca"    → Barge
    // path 2: "caravela" → Caravel
    // path 3: "nau"      → Carrack
    // path 4: "fragata"  → Frigate
    // path 5: "galeao"   → Galleon
    // path 6: default    → null
    // ----------------------------------------------------------------

    @Test
    void buildShip1() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
        assertAll(
                () -> assertNotNull(s, "Error: buildShip('barca') should not return null."),
                () -> assertInstanceOf(Barge.class, s, "Error: buildShip('barca') should return a Barge."),
                () -> assertEquals("Barca", s.getCategory(), "Error: Category should be 'Barca'.")
        );
    }

    @Test
    void buildShip2() {
        Ship s = Ship.buildShip("caravela", Compass.NORTH, new Position(1, 1));
        assertAll(
                () -> assertNotNull(s, "Error: buildShip('caravela') should not return null."),
                () -> assertInstanceOf(Caravel.class, s, "Error: buildShip('caravela') should return a Caravel."),
                () -> assertEquals("Caravela", s.getCategory(), "Error: Category should be 'Caravela'.")
        );
    }

    @Test
    void buildShip3() {
        Ship s = Ship.buildShip("nau", Compass.NORTH, new Position(1, 1));
        assertAll(
                () -> assertNotNull(s, "Error: buildShip('nau') should not return null."),
                () -> assertInstanceOf(Carrack.class, s, "Error: buildShip('nau') should return a Carrack."),
                () -> assertEquals("Nau", s.getCategory(), "Error: Category should be 'Nau'.")
        );
    }

    @Test
    void buildShip4() {
        Ship s = Ship.buildShip("fragata", Compass.NORTH, new Position(1, 1));
        assertAll(
                () -> assertNotNull(s, "Error: buildShip('fragata') should not return null."),
                () -> assertInstanceOf(Frigate.class, s, "Error: buildShip('fragata') should return a Frigate."),
                () -> assertEquals("Fragata", s.getCategory(), "Error: Category should be 'Fragata'.")
        );
    }

    @Test
    void buildShip5() {
        Ship s = Ship.buildShip("galeao", Compass.NORTH, new Position(1, 1));
        assertAll(
                () -> assertNotNull(s, "Error: buildShip('galeao') should not return null."),
                () -> assertInstanceOf(Galleon.class, s, "Error: buildShip('galeao') should return a Galleon."),
                () -> assertEquals("Galeao", s.getCategory(), "Error: Category should be 'Galeao'.")
        );
    }

    @Test
    void buildShip6() {
        // path: default → null
        Ship s = Ship.buildShip("unknown", Compass.NORTH, new Position(1, 1));
        assertNull(s, "Error: buildShip with unknown type should return null.");
    }

    // ----------------------------------------------------------------
    // constructor (CC = 1)
    // Null checks via Objects.requireNonNull
    // ----------------------------------------------------------------

    @Test
    void constructor() {
        assertAll(
                () -> assertNotNull(ship, "Error: Ship instance should not be null."),
                () -> assertEquals("Barca", ship.getCategory(), "Error: Category should be 'Barca'."),
                () -> assertEquals(Compass.NORTH, ship.getBearing(), "Error: Bearing should be NORTH."),
                () -> assertEquals(1, ship.getSize(), "Error: Size should be 1.")
        );
    }

    @Test
    void constructorNullCategory() {
        // Ship constructor calls Objects.requireNonNull on bearing
        assertThrows(NullPointerException.class,
                () -> new Barge(null, new Position(0, 0)),
                "Error: Null bearing should throw NullPointerException.");
    }

    @Test
    void constructorNullPosition() {
        assertThrows(NullPointerException.class,
                () -> new Barge(Compass.NORTH, null),
                "Error: Null position should throw NullPointerException.");
    }

    // ----------------------------------------------------------------
    // getCategory (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getCategory() {
        assertEquals("Barca", ship.getCategory(), "Error: Category should be 'Barca'.");
    }

    // ----------------------------------------------------------------
    // getPositions (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getPositions() {
        assertAll(
                () -> assertNotNull(ship.getPositions(), "Error: Positions list should not be null."),
                () -> assertEquals(1, ship.getPositions().size(), "Error: Barge should have exactly 1 position."),
                () -> assertEquals(new Position(5, 5), ship.getPositions().get(0), "Error: Position should be (5,5).")
        );
    }

    // ----------------------------------------------------------------
    // getAdjacentPositions (CC = 3)
    // path 1: position has adjacents that are not ship positions → added
    // path 2: adjacent position is already in ship positions → skipped
    // path 3: adjacent position already in adjacentPositions list → skipped (no duplicate)
    // ----------------------------------------------------------------

    @Test
    void getAdjacentPositions1() {
        // Barge at (5,5): the 8 surrounding valid positions are all adjacent
        List<IPosition> adj = ship.getAdjacentPositions();
        assertFalse(adj.isEmpty(), "Error: Adjacent positions list should not be empty.");
        // (5,5) centre → all 8 neighbours are valid and not occupied by ship
        assertEquals(8, adj.size(), "Error: Barge at (5,5) should have 8 adjacent positions.");
    }

    @Test
    void getAdjacentPositions2() {
        // Caravel NORTH at (5,5): positions (5,5) and (6,5)
        // Adjacent positions should NOT include (5,5) or (6,5) themselves
        Ship caravel = new Caravel(Compass.NORTH, new Position(5, 5));
        List<IPosition> adj = caravel.getAdjacentPositions();
        assertFalse(adj.contains(new Position(5, 5)),
                "Error: Adjacent positions should not include ship's own position (5,5).");
        assertFalse(adj.contains(new Position(6, 5)),
                "Error: Adjacent positions should not include ship's own position (6,5).");
    }

    @Test
    void getAdjacentPositions3() {
        // No duplicate adjacent positions
        Ship caravel = new Caravel(Compass.NORTH, new Position(5, 5));
        List<IPosition> adj = caravel.getAdjacentPositions();
        long distinctCount = adj.stream().distinct().count();
        assertEquals(distinctCount, adj.size(),
                "Error: Adjacent positions list should not contain duplicates.");
    }

    // ----------------------------------------------------------------
    // getPosition (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getPosition() {
        assertAll(
                () -> assertNotNull(ship.getPosition(), "Error: Initial position should not be null."),
                () -> assertEquals(new Position(5, 5), ship.getPosition(), "Error: Position should be (5,5).")
        );
    }

    // ----------------------------------------------------------------
    // getBearing (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getBearing() {
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Bearing should be NORTH.");
    }

    // ----------------------------------------------------------------
    // getSize (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getSize() {
        assertEquals(1, ship.getSize(), "Error: Barge size should be 1.");
    }

    // ----------------------------------------------------------------
    // stillFloating (CC = 2)
    // path 1: at least one position not hit → true
    // path 2: all positions hit → false
    // ----------------------------------------------------------------

    @Test
    void stillFloating1() {
        assertTrue(ship.stillFloating(), "Error: Ship should be floating when intact.");
    }

    @Test
    void stillFloating2() {
        ship.getPositions().forEach(IPosition::shoot);
        assertFalse(ship.stillFloating(), "Error: Ship should not be floating when all positions are hit.");
    }

    // ----------------------------------------------------------------
    // getTopMostPos (CC = 2)
    // path 1: first element is smallest (no replacement in loop)
    // path 2: a later element is smaller (replacement occurs)
    // (Barge has 1 pos, so only path 1; Caravel NORTH has 2 rows: 5,6 → top=5)
    // ----------------------------------------------------------------

    @Test
    void getTopMostPos1() {
        // Barge at (5,5): single position, loop doesn't iterate → returns row 5
        assertEquals(5, ship.getTopMostPos(), "Error: Topmost row for Barge at (5,5) should be 5.");
    }

    @Test
    void getTopMostPos2() {
        // Caravel NORTH at (5,5): positions (5,5),(6,5) → loop checks i=1, no update → top=5
        Ship caravel = new Caravel(Compass.NORTH, new Position(5, 5));
        assertEquals(5, caravel.getTopMostPos(), "Error: Topmost row for Caravel NORTH at (5,5) should be 5.");
    }

    // ----------------------------------------------------------------
    // getBottomMostPos (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void getBottomMostPos1() {
        // Barge at (5,5): single position → bottom = 5
        assertEquals(5, ship.getBottomMostPos(), "Error: Bottommost row for Barge at (5,5) should be 5.");
    }

    @Test
    void getBottomMostPos2() {
        // Caravel NORTH at (5,5): positions (5,5),(6,5) → bottom = 6 (replacement occurs at i=1)
        Ship caravel = new Caravel(Compass.NORTH, new Position(5, 5));
        assertEquals(6, caravel.getBottomMostPos(), "Error: Bottommost row for Caravel NORTH at (5,5) should be 6.");
    }

    // ----------------------------------------------------------------
    // getLeftMostPos (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void getLeftMostPos1() {
        // Barge at (5,5) → left = 5
        assertEquals(5, ship.getLeftMostPos(), "Error: Leftmost col for Barge at (5,5) should be 5.");
    }

    @Test
    void getLeftMostPos2() {
        // Caravel EAST at (5,3): positions (5,3),(5,4) → left = 3
        Ship caravel = new Caravel(Compass.EAST, new Position(5, 3));
        assertEquals(3, caravel.getLeftMostPos(), "Error: Leftmost col for Caravel EAST at (5,3) should be 3.");
    }

    // ----------------------------------------------------------------
    // getRightMostPos (CC = 2)
    // ----------------------------------------------------------------

    @Test
    void getRightMostPos1() {
        // Barge at (5,5) → right = 5
        assertEquals(5, ship.getRightMostPos(), "Error: Rightmost col for Barge at (5,5) should be 5.");
    }

    @Test
    void getRightMostPos2() {
        // Caravel EAST at (5,3): positions (5,3),(5,4) → right = 4 (replacement at i=1)
        Ship caravel = new Caravel(Compass.EAST, new Position(5, 3));
        assertEquals(4, caravel.getRightMostPos(), "Error: Rightmost col for Caravel EAST at (5,3) should be 4.");
    }

    // ----------------------------------------------------------------
    // occupies (CC = 2)
    // path 1: position found in list → true
    // path 2: position not found → false
    // ----------------------------------------------------------------

    @Test
    void occupies1() {
        assertTrue(ship.occupies(new Position(5, 5)), "Error: Ship should occupy position (5,5).");
    }

    @Test
    void occupies2() {
        assertFalse(ship.occupies(new Position(0, 0)), "Error: Ship should not occupy position (0,0).");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IShip) (CC = 2)
    // path 1: other ship has adjacent position → true
    // path 2: other ship has no adjacent position → false
    // ----------------------------------------------------------------

    @Test
    void tooCloseToShip1() {
        Ship nearby = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(ship.tooCloseTo(nearby), "Error: Barge at (5,6) should be too close to ship at (5,5).");
    }

    @Test
    void tooCloseToShip2() {
        Ship distant = new Barge(Compass.NORTH, new Position(0, 0));
        assertFalse(ship.tooCloseTo(distant), "Error: Barge at (0,0) should not be too close to ship at (5,5).");
    }

    // ----------------------------------------------------------------
    // tooCloseTo(IPosition) (CC = 2)
    // path 1: position is adjacent → true
    // path 2: position is not adjacent → false
    // ----------------------------------------------------------------

    @Test
    void tooCloseToPosition1() {
        assertTrue(ship.tooCloseTo(new Position(5, 6)), "Error: Position (5,6) is adjacent to ship at (5,5).");
    }

    @Test
    void tooCloseToPosition2() {
        assertFalse(ship.tooCloseTo(new Position(0, 0)), "Error: Position (0,0) should not be adjacent to ship at (5,5).");
    }

    // ----------------------------------------------------------------
    // shoot (CC = 2)
    // path 1: position matches a ship position → marks as hit
    // path 2: position does not match → no change
    // ----------------------------------------------------------------

    @Test
    void shoot1() {
        ship.shoot(new Position(5, 5));
        assertTrue(ship.getPositions().get(0).isHit(), "Error: Position (5,5) should be marked as hit after shooting.");
    }

    @Test
    void shoot2() {
        ship.shoot(new Position(1, 1));
        assertFalse(ship.getPositions().get(0).isHit(), "Error: Position (5,5) should not be hit when shooting at (1,1).");
    }

    // ----------------------------------------------------------------
    // sink (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void sink() {
        ship.sink();
        assertAll(
                () -> assertFalse(ship.stillFloating(), "Error: Ship should not be floating after sink()."),
                () -> assertTrue(ship.getPositions().get(0).isHit(), "Error: All positions should be hit after sink().")
        );
    }

    // ----------------------------------------------------------------
    // toString (CC = 1)
    // format: "[category bearing position]"
    // ----------------------------------------------------------------

    @Test
    void toStringTest() {
        String result = ship.toString();
        assertAll(
                () -> assertNotNull(result, "Error: toString() should not return null."),
                () -> assertTrue(result.contains("Barca"), "Error: toString() should contain 'Barca'."),
                () -> assertTrue(result.contains("n"), "Error: toString() should contain bearing 'n' (NORTH)."),
                () -> assertTrue(result.startsWith("["), "Error: toString() should start with '['."),
                () -> assertTrue(result.endsWith("]"), "Error: toString() should end with ']'.")
        );
    }
}