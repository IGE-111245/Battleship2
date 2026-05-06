package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the Position class.
 * Author: IGE-111245 (Martim Saldanha) / IGE-111519 (Vicente Viela)
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor(int, int): 1
 * - constructor(char, int): 1
 * - randomPosition: 1
 * - getRow: 1
 * - getColumn: 1
 * - getClassicRow: 1
 * - getClassicColumn: 1
 * - isInside: 4  (4 condições independentes: row<0, col<0, row>=SIZE, col>=SIZE)
 * - isAdjacentTo: 2  (Math.abs row <=1 && Math.abs col <=1)
 * - adjacentPositions: 2  (loop + isInside check)
 * - isOccupied: 1
 * - isHit: 1
 * - occupy: 1
 * - shoot: 1
 * - equals: 3  (this==other, instanceof IPosition, fallthrough)
 * - hashCode: 1
 * - toString: 1
 */
public class PositionTest {

    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(2, 3);
    }

    @AfterEach
    void tearDown() {
        position = null;
    }

    // ----------------------------------------------------------------
    // constructor(int, int) (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void constructor() {
        Position pos = new Position(3, 4);
        assertAll(
                () -> assertNotNull(pos, "Error: Position instance should not be null."),
                () -> assertEquals(3, pos.getRow(), "Error: Row should be 3."),
                () -> assertEquals(4, pos.getColumn(), "Error: Column should be 4."),
                () -> assertFalse(pos.isOccupied(), "Error: New position should not be occupied."),
                () -> assertFalse(pos.isHit(), "Error: New position should not be hit.")
        );
    }

    // ----------------------------------------------------------------
    // constructor(char, int) (CC = 1)
    // 'A' + 0 = row 0, column 1-1 = 0
    // 'C' + 0 = row 2, column 4-1 = 3
    // ----------------------------------------------------------------

    @Test
    void constructorClassic() {
        Position pos = new Position('C', 4);
        assertAll(
                () -> assertNotNull(pos, "Error: Position (char,int) should not be null."),
                () -> assertEquals(2, pos.getRow(), "Error: 'C' should map to row 2."),
                () -> assertEquals(3, pos.getColumn(), "Error: Column 4 should map to index 3."),
                () -> assertFalse(pos.isOccupied(), "Error: New position should not be occupied."),
                () -> assertFalse(pos.isHit(), "Error: New position should not be hit.")
        );
    }

    // ----------------------------------------------------------------
    // randomPosition (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void randomPosition() {
        Position pos = Position.randomPosition();
        assertAll(
                () -> assertNotNull(pos, "Error: Random position should not be null."),
                () -> assertTrue(pos.getRow() >= 0 && pos.getRow() < Game.BOARD_SIZE,
                        "Error: Random row should be within [0, BOARD_SIZE[."),
                () -> assertTrue(pos.getColumn() >= 0 && pos.getColumn() < Game.BOARD_SIZE,
                        "Error: Random column should be within [0, BOARD_SIZE[.")
        );
    }

    // ----------------------------------------------------------------
    // getRow (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getRow() {
        assertEquals(2, position.getRow(), "Error: Row should be 2.");
    }

    // ----------------------------------------------------------------
    // getColumn (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getColumn() {
        assertEquals(3, position.getColumn(), "Error: Column should be 3.");
    }

    // ----------------------------------------------------------------
    // getClassicRow (CC = 1)
    // row=2 → 'A'+2 = 'C'
    // ----------------------------------------------------------------

    @Test
    void getClassicRow() {
        assertEquals('C', position.getClassicRow(), "Error: Classic row for row=2 should be 'C'.");
    }

    // ----------------------------------------------------------------
    // getClassicColumn (CC = 1)
    // column=3 → 3+1 = 4
    // ----------------------------------------------------------------

    @Test
    void getClassicColumn() {
        assertEquals(4, position.getClassicColumn(), "Error: Classic column for column=3 should be 4.");
    }

    // ----------------------------------------------------------------
    // isInside (CC = 4)
    // path 1: row < 0 → false
    // path 2: col < 0 → false
    // path 3: row >= BOARD_SIZE → false
    // path 4: col >= BOARD_SIZE → false
    // (implicit path 5: all conditions met → true, tested via constructor)
    // ----------------------------------------------------------------

    @Test
    void isInside1() {
        // path: valid position → true
        Position valid = new Position(0, 0);
        assertTrue(valid.isInside(), "Error: Position (0,0) should be inside the board.");
    }

    @Test
    void isInside2() {
        // path: row < 0 → false
        Position neg = new Position(-1, 5);
        assertFalse(neg.isInside(), "Error: Position with negative row should not be inside the board.");
    }

    @Test
    void isInside3() {
        // path: column < 0 → false
        Position neg = new Position(5, -1);
        assertFalse(neg.isInside(), "Error: Position with negative column should not be inside the board.");
    }

    @Test
    void isInside4() {
        // path: row >= BOARD_SIZE → false
        Position over = new Position(Game.BOARD_SIZE, 5);
        assertFalse(over.isInside(), "Error: Position with row >= BOARD_SIZE should not be inside the board.");
    }

    // ----------------------------------------------------------------
    // isAdjacentTo (CC = 2)
    // path 1: |row diff| <= 1 && |col diff| <= 1 → true
    // path 2: at least one diff > 1 → false
    // ----------------------------------------------------------------

    @Test
    void isAdjacentTo1() {
        // horizontal neighbour → true
        assertTrue(position.isAdjacentTo(new Position(2, 4)),
                "Error: Position (2,4) should be adjacent to (2,3).");
    }

    @Test
    void isAdjacentTo2() {
        // vertical neighbour → true
        assertTrue(position.isAdjacentTo(new Position(3, 3)),
                "Error: Position (3,3) should be adjacent to (2,3).");
    }

    @Test
    void isAdjacentTo3() {
        // diagonal neighbour → true
        assertTrue(position.isAdjacentTo(new Position(3, 4)),
                "Error: Position (3,4) should be diagonally adjacent to (2,3).");
    }

    @Test
    void isAdjacentTo4() {
        // too far → false
        assertFalse(position.isAdjacentTo(new Position(0, 0)),
                "Error: Position (0,0) should not be adjacent to (2,3).");
    }

    // ----------------------------------------------------------------
    // adjacentPositions (CC = 2)
    // path 1: generated position is inside board → added to list
    // path 2: generated position is outside board → not added
    // ----------------------------------------------------------------

    @Test
    void adjacentPositions1() {
        // corner position (0,0) has only 3 valid neighbours
        Position corner = new Position(0, 0);
        List<IPosition> adj = corner.adjacentPositions();
        assertFalse(adj.isEmpty(), "Error: Corner position (0,0) should have adjacent positions.");
        // (0,0) corner: valid are (1,0),(0,1),(1,1) — 3 positions
        assertEquals(3, adj.size(), "Error: Corner (0,0) should have exactly 3 adjacent positions.");
    }

    @Test
    void adjacentPositions2() {
        // centre position has 8 valid neighbours
        Position centre = new Position(5, 5);
        List<IPosition> adj = centre.adjacentPositions();
        assertEquals(8, adj.size(), "Error: Centre position (5,5) should have exactly 8 adjacent positions.");
    }

    // ----------------------------------------------------------------
    // isOccupied (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void isOccupied() {
        assertFalse(position.isOccupied(), "Error: New position should not be occupied.");
        position.occupy();
        assertTrue(position.isOccupied(), "Error: Position should be occupied after occupy().");
    }

    // ----------------------------------------------------------------
    // isHit (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void isHit() {
        assertFalse(position.isHit(), "Error: New position should not be hit.");
        position.shoot();
        assertTrue(position.isHit(), "Error: Position should be hit after shoot().");
    }

    // ----------------------------------------------------------------
    // occupy (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void occupy() {
        position.occupy();
        assertTrue(position.isOccupied(), "Error: Position should be occupied after calling occupy().");
    }

    // ----------------------------------------------------------------
    // shoot (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void shoot() {
        position.shoot();
        assertTrue(position.isHit(), "Error: Position should be hit after calling shoot().");
    }

    // ----------------------------------------------------------------
    // equals (CC = 3)
    // path 1: same object reference → true
    // path 2: other is IPosition with same row and column → true
    // path 3: other is IPosition with different coordinates → false
    // path 4: (bonus) other is not IPosition → false
    // path 5: (bonus) other is null → false
    // ----------------------------------------------------------------

    @Test
    void equals1() {
        // path: same reference → true
        assertTrue(position.equals(position), "Error: A position should be equal to itself.");
    }

    @Test
    void equals2() {
        // path: different object, same coordinates → true
        Position same = new Position(2, 3);
        assertTrue(position.equals(same), "Error: Positions with same row and column should be equal.");
    }

    @Test
    void equals3() {
        // path: different coordinates → false
        Position different = new Position(2, 4);
        assertFalse(position.equals(different), "Error: Positions with different coordinates should not be equal.");
    }

    @Test
    void equals4() {
        // path: not an IPosition → false
        assertFalse(position.equals(new Object()), "Error: Position should not equal a non-IPosition object.");
    }

    @Test
    void equals5() {
        // path: null → false
        assertFalse(position.equals(null), "Error: Position should not equal null.");
    }

    // ----------------------------------------------------------------
    // hashCode (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void hashCode_consistent() {
        Position same = new Position(2, 3);
        assertEquals(position.hashCode(), same.hashCode(),
                "Error: Equal positions should have the same hashCode.");
    }

    // ----------------------------------------------------------------
    // toString (CC = 1)
    // row=2 → 'A'+2='C', column=3 → 3+1=4 → "C4"
    // ----------------------------------------------------------------

    @Test
    void toStringTest() {
        assertEquals("C4", position.toString(),
                "Error: toString() for position (2,3) should return 'C4'.");
    }
}