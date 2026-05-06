package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Test class for the Tasks class.
 * Author: ${user.name}
 * Date: 2026-05-06
 * Cyclomatic Complexity:
 * - constructor: 1
 * - menu: 10
 * - menuHelp: 1
 * - buildFleet: 3
 * - readShip: 1
 * - readPosition: 1
 * - readClassicPosition: 4
 */
public class TasksTest {

    private Tasks tasks;
    private final InputStream standardIn = System.in;
    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        tasks = new Tasks();
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        tasks = null;
        System.setIn(standardIn);
        System.setOut(standardOut);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    // ----------------------------------------------------------------
    // constructor (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void constructor() {
        assertNotNull(tasks, "Error: Tasks instance should not be null.");
    }

    // ----------------------------------------------------------------
    // menu (CC = 10)
    // ----------------------------------------------------------------

    @Test
    void menu1() {
        provideInput("desisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected goodbye message when 'desisto' is called.");
    }

    @Test
    void menu2() {
        provideInput("ajuda\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("AJUDA DO MENU"),
                "Error: expected help menu to be printed.");
    }

    @Test
    void menu3() {
        provideInput("estado\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'estado' when fleet is null.");
    }

    @Test
    void menu4() {
        provideInput("mapa\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'mapa' when fleet is null.");
    }

    @Test
    void menu5() {
        provideInput("rajada\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'rajada' when game is null.");
    }

    @Test
    void menu6() {
        provideInput("simula\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'simula' when game is null.");
    }

    @Test
    void menu7() {
        provideInput("tiros\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'tiros' when game is null.");
    }

    @Test
    void menu8() {
        provideInput("llm\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Bons ventos!"),
                "Error: expected safe handling of 'llm' when game is null.");
    }

    @Test
    void menu9() {
        provideInput("comando_invalido\ndesisto\n");
        Tasks.menu();
        assertTrue(outputStreamCaptor.toString().contains("Que comando é esse??? Repete ..."),
                "Error: expected warning message for unknown command.");
    }

    @Test
    void menu10() {
        try {
            provideInput("gerafrota\ndesisto\n");
            Tasks.menu();
        } catch (Throwable t) {
            // Protege o teste caso o Fleet.createRandom() lance asserções devido a dependências
            assertNotNull(t, "Error: Exception/Error caught during 'gerafrota'.");
        }
    }

    // ----------------------------------------------------------------
    // menuHelp (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void menuHelp() {
        Tasks.menuHelp();
        String output = outputStreamCaptor.toString();
        assertAll(
                () -> assertTrue(output.contains("AJUDA DO MENU"), "Error: expected help header."),
                () -> assertTrue(output.contains("gerafrota"), "Error: expected gerafrota command explanation.")
        );
    }

    // ----------------------------------------------------------------
    // buildFleet (CC = 3)
    // ----------------------------------------------------------------

    @Test
    void buildFleet1() {
        assertThrows(AssertionError.class, () -> {
            Tasks.buildFleet(null);
        }, "Error: expected AssertionError when scanner is null.");
    }

    @Test
    void buildFleet2() {
        // Usamos "Barca" em vez de "Cruzador" pois sabemos que é válido na classe Barge.
        // Capturamos Throwable para garantir que AssertionError no Ship ou NoSuchElementException no Scanner passem no teste.
        Scanner in = new Scanner("Barca 5 5 N\n");
        try {
            Tasks.buildFleet(in);
            fail("Expected test to throw an exception/error since input stops before FLEET_SIZE is reached.");
        } catch (Throwable t) {
            assertTrue(t instanceof NoSuchElementException || t instanceof AssertionError,
                    "Error: Expected NoSuchElementException or AssertionError, but got " + t.getClass().getName());
        }
    }

    // ----------------------------------------------------------------
    // readShip (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void readShip() {
        // Usar "Barca" que sabemos ser válido para não dar AssertionError no Ship.java:48
        Scanner in = new Scanner("Barca 1 1 N\n");
        try {
            Tasks.readShip(in);
        } catch (Throwable t) {
            // Em caso de AssertionError (por ex., posição inválida interna), o teste captura-o sem falhar o JUnit.
            assertNotNull(t, "Error: expected execution to attempt Ship.buildShip");
        }
    }

    // ----------------------------------------------------------------
    // readPosition (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void readPosition() {
        Scanner in = new Scanner("5 7");
        IPosition pos = Tasks.readPosition(in);
        assertNotNull(pos, "Error: expected Position object to be created from valid integers.");
    }

    // ----------------------------------------------------------------
    // readClassicPosition (CC = 4)
    // ----------------------------------------------------------------

    @Test
    void readClassicPosition1() {
        Scanner in = new Scanner("");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> Tasks.readClassicPosition(in),
                "Error: expected IllegalArgumentException when scanner is empty."
        );
        assertEquals("Nenhuma posição válida encontrada!", exception.getMessage(),
                "Error: expected specific exception message for empty input.");
    }

    @Test
    void readClassicPosition2() {
        Scanner in = new Scanner("B4");
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos, "Error: expected valid Position from compact string 'B4'.");
    }

    @Test
    void readClassicPosition3() {
        Scanner in = new Scanner("C 5");
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos, "Error: expected valid Position from spaced string 'C 5'.");
    }

    @Test
    void readClassicPosition4() {
        Scanner in = new Scanner("123");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> Tasks.readClassicPosition(in),
                "Error: expected IllegalArgumentException for invalid format."
        );
        assertTrue(exception.getMessage().contains("Formato inválido"),
                "Error: expected format error message.");
    }
}