package battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for class Compass.
 * Author: Martim Reis 111245
 * Date: 2026-05-06 12:47
 * Cyclomatic Complexity:
 * - getDirection(): 1
 * - toString(): 1
 * - randomBearing(): 1
 * - charToCompass(char): 5
 */
class CompassTest {

	private Compass compass;

	@BeforeEach
	void setUp() {
		compass = Compass.NORTH;
	}

	@AfterEach
	void tearDown() {
		compass = null;
	}

	@Test
	void getDirection() {
		assertEquals('n', compass.getDirection(),
				"Error: expected NORTH direction character to be 'n', but got a different value.");
	}

	@Test
	void toStringTest() {
		assertEquals("n", compass.toString(),
				"Error: expected NORTH string representation to be 'n', but got a different value.");
	}

	@Test
	void randomBearing() {
		Compass result = Compass.randomBearing();

		assertAll("randomBearing result",
				() -> assertNotNull(result, "Error: expected randomBearing() to return a Compass value, but it returned null."),
				() -> assertTrue(
						result == Compass.NORTH || result == Compass.SOUTH || result == Compass.EAST || result == Compass.WEST,
						"Error: expected randomBearing() to return one of the defined Compass constants, but got an invalid value."
				)
		);
	}

	@Test
	void charToCompass1() {
		assertEquals(Compass.NORTH, Compass.charToCompass('n'),
				"Error: expected charToCompass('n') to return NORTH, but got a different value.");
	}

	@Test
	void charToCompass2() {
		assertEquals(Compass.SOUTH, Compass.charToCompass('s'),
				"Error: expected charToCompass('s') to return SOUTH, but got a different value.");
	}

	@Test
	void charToCompass3() {
		assertEquals(Compass.EAST, Compass.charToCompass('e'),
				"Error: expected charToCompass('e') to return EAST, but got a different value.");
	}

	@Test
	void charToCompass4() {
		assertEquals(Compass.WEST, Compass.charToCompass('o'),
				"Error: expected charToCompass('o') to return WEST, but got a different value.");
	}

	@Test
	void charToCompass5() {
		assertNull(Compass.charToCompass('x'),
				"Error: expected charToCompass('x') to return null for an invalid direction, but got a different value.");
	}
}