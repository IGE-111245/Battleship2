package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Move class.
 * Author: IGE-111245 (Martim Saldanha) / IGE-111519 (Vicente Viela)
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - constructor: 1
 * - toString: 1
 * - getNumber: 1
 * - getShots: 1
 * - getShotResults: 1
 * - processEnemyFire: 7
 *     (loop over results + valid/repeated + ship==null + sunk + verbose branches)
 */
public class MoveTest {

    private Move move;
    private List<IPosition> shots;
    private List<IGame.ShotResult> results;

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        shots.add(new Position(0, 0));
        shots.add(new Position(0, 1));
        shots.add(new Position(0, 2));

        results = new ArrayList<>();
        move = new Move(1, shots, results);
    }

    @AfterEach
    void tearDown() {
        move = null;
        shots = null;
        results = null;
    }

    // ----------------------------------------------------------------
    // constructor (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void constructor() {
        assertAll(
                () -> assertNotNull(move, "Error: Move instance should not be null."),
                () -> assertEquals(1, move.getNumber(), "Error: Move number should be 1."),
                () -> assertEquals(3, move.getShots().size(), "Error: Move should have 3 shots."),
                () -> assertTrue(move.getShotResults().isEmpty(), "Error: Move results should be empty initially.")
        );
    }

    // ----------------------------------------------------------------
    // toString (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void toStringTest() {
        String result = move.toString();
        assertAll(
                () -> assertNotNull(result, "Error: toString() should not return null."),
                () -> assertTrue(result.contains("1"), "Error: toString() should contain move number '1'."),
                () -> assertTrue(result.contains("3"), "Error: toString() should contain shots count '3'.")
        );
    }

    // ----------------------------------------------------------------
    // getNumber (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getNumber() {
        assertEquals(1, move.getNumber(), "Error: getNumber() should return 1.");
    }

    // ----------------------------------------------------------------
    // getShots (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getShots() {
        assertAll(
                () -> assertNotNull(move.getShots(), "Error: getShots() should not return null."),
                () -> assertEquals(3, move.getShots().size(), "Error: getShots() should return 3 shots."),
                () -> assertEquals(new Position(0, 0), move.getShots().get(0), "Error: First shot should be (0,0).")
        );
    }

    // ----------------------------------------------------------------
    // getShotResults (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void getShotResults() {
        assertAll(
                () -> assertNotNull(move.getShotResults(), "Error: getShotResults() should not return null."),
                () -> assertTrue(move.getShotResults().isEmpty(), "Error: getShotResults() should be empty.")
        );
    }

    // ----------------------------------------------------------------
    // processEnemyFire (CC = 7)
    //
    // path 1: empty results list → all zeros, valid JSON returned
    // path 2: invalid shot (valid=false) → ignored, outsideShots incremented
    // path 3: repeated shot (valid=true, repeated=true) → repeatedShots++
    // path 4: valid shot, ship=null → missedShots++
    // path 5: valid shot, ship≠null, sunk=false → hitsPerBoat updated
    // path 6: valid shot, ship≠null, sunk=true → sunkBoatsCount updated
    // path 7: verbose=true → prints output and returns JSON
    // ----------------------------------------------------------------

    @Test
    void processEnemyFire1() {
        // path: empty results → outsideShots = NUMBER_SHOTS, all others 0
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertNotNull(json, "Error: processEnemyFire should return non-null JSON."),
                () -> assertTrue(json.contains("\"validShots\""), "Error: JSON should contain 'validShots'."),
                () -> assertTrue(json.contains("\"missedShots\""), "Error: JSON should contain 'missedShots'."),
                () -> assertTrue(json.contains("\"repeatedShots\""), "Error: JSON should contain 'repeatedShots'."),
                () -> assertTrue(json.contains("\"outsideShots\""), "Error: JSON should contain 'outsideShots'."),
                () -> assertTrue(json.contains("\"sunkBoats\""), "Error: JSON should contain 'sunkBoats'."),
                () -> assertTrue(json.contains("\"hitsOnBoats\""), "Error: JSON should contain 'hitsOnBoats'.")
        );
    }

    @Test
    void processEnemyFire2() {
        // path: invalid shot (valid=false) → contributes to outsideShots
        results.add(new IGame.ShotResult(false, false, null, false));
        Move m = new Move(2, shots, results);
        String json = m.processEnemyFire(false);
        assertNotNull(json, "Error: JSON should not be null for a move with one invalid shot.");
        // validShots = 0, outsideShots = NUMBER_SHOTS - 0 - 0
        assertTrue(json.contains("\"validShots\" : 0"), "Error: validShots should be 0 for an invalid shot.");
    }

    @Test
    void processEnemyFire3() {
        // path: repeated shot (valid=true, repeated=true)
        results.add(new IGame.ShotResult(true, true, null, false));
        Move m = new Move(3, shots, results);
        String json = m.processEnemyFire(false);
        assertNotNull(json, "Error: JSON should not be null for a move with a repeated shot.");
        assertTrue(json.contains("\"repeatedShots\" : 1"), "Error: repeatedShots should be 1.");
    }

    @Test
    void processEnemyFire4() {
        // path: valid shot, ship=null → missedShots++
        results.add(new IGame.ShotResult(true, false, null, false));
        Move m = new Move(4, shots, results);
        String json = m.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"), "Error: validShots should be 1."),
                () -> assertTrue(json.contains("\"missedShots\" : 1"), "Error: missedShots should be 1.")
        );
    }

    @Test
    void processEnemyFire5() {
        // path: valid shot, ship≠null, sunk=false → hitsOnBoats updated
        Fleet fleet = new Fleet();
        Barge barge = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(barge);

        results.add(new IGame.ShotResult(true, false, barge, false));
        Move m = new Move(5, shots, results);
        String json = m.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"), "Error: validShots should be 1."),
                () -> assertTrue(json.contains("hitsOnBoats"), "Error: JSON should contain hitsOnBoats."),
                () -> assertTrue(json.contains("Barca"), "Error: JSON hitsOnBoats should contain 'Barca'.")
        );
    }

    @Test
    void processEnemyFire6() {
        // path: valid shot, ship≠null, sunk=true → sunkBoats updated
        Fleet fleet = new Fleet();
        Barge barge = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(barge);

        results.add(new IGame.ShotResult(true, false, barge, true));
        Move m = new Move(6, shots, results);
        String json = m.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"), "Error: validShots should be 1."),
                () -> assertTrue(json.contains("sunkBoats"), "Error: JSON should contain sunkBoats."),
                () -> assertTrue(json.contains("Barca"), "Error: JSON sunkBoats should contain 'Barca'.")
        );
    }

    @Test
    void processEnemyFire7() {
        // path: verbose=true → should print and return valid JSON without throwing
        results.add(new IGame.ShotResult(true, false, null, false));
        Move m = new Move(7, shots, results);
        assertDoesNotThrow(() -> m.processEnemyFire(true),
                "Error: processEnemyFire(true) should not throw any exception.");
        String json = m.processEnemyFire(true);
        assertNotNull(json, "Error: processEnemyFire(true) should return non-null JSON.");
    }

    // ----------------------------------------------------------------
    // Cenário extra: múltiplos resultados mistos
    // ----------------------------------------------------------------

    @Test
    void processEnemyFire_mixedResults() {
        Barge barge1 = new Barge(Compass.NORTH, new Position(1, 1));
        Barge barge2 = new Barge(Compass.NORTH, new Position(3, 3));

        results.add(new IGame.ShotResult(true, false, barge1, true));   // sunk
        results.add(new IGame.ShotResult(true, false, null, false));    // miss
        results.add(new IGame.ShotResult(true, true, null, false));     // repeated

        Move m = new Move(8, shots, results);
        String json = m.processEnemyFire(false);

        assertAll(
                () -> assertNotNull(json, "Error: JSON should not be null for mixed results."),
                () -> assertTrue(json.contains("\"validShots\" : 2"), "Error: validShots should be 2 (sunk + miss)."),
                () -> assertTrue(json.contains("\"repeatedShots\" : 1"), "Error: repeatedShots should be 1."),
                () -> assertTrue(json.contains("\"missedShots\" : 1"), "Error: missedShots should be 1.")
        );
    }
}