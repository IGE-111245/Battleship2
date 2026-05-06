package battleship;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe FogGUI")
class FogGUITest {

    @Test
    @DisplayName("FogGUI pode ser instanciada")
    void shouldInstantiateFogGUI() {
        assertDoesNotThrow(FogGUI::new);
    }

    @Test
    @DisplayName("Array revelado é inicializado com 10x10")
    void shouldInitializeRevealedMatrix() throws Exception {
        FogGUI gui = new FogGUI();

        Field reveladoField = FogGUI.class.getDeclaredField("revelado");
        reveladoField.setAccessible(true);
        boolean[][] revelado = (boolean[][]) reveladoField.get(gui);

        assertNotNull(revelado);
        assertEquals(10, revelado.length);
        assertEquals(10, revelado[0].length);
        assertFalse(revelado[0][0]);
        assertFalse(revelado[9][9]);
    }

    @Test
    @DisplayName("revela marca posição válida como revelada")
    void shouldRevealValidPosition() throws Exception {
        FogGUI gui = new FogGUI();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Method revela = FogGUI.class.getDeclaredMethod("revela", int.class, int.class, GraphicsContext.class);
        revela.setAccessible(true);

        Field reveladoField = FogGUI.class.getDeclaredField("revelado");
        reveladoField.setAccessible(true);
        boolean[][] revelado = (boolean[][]) reveladoField.get(gui);

        assertFalse(revelado[2][1]);

        revela.invoke(gui, 1, 2, gc);

        assertTrue(revelado[2][1]);
    }

    @Test
    @DisplayName("revela ignora posição com x negativo")
    void shouldIgnoreNegativeX() throws Exception {
        FogGUI gui = new FogGUI();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Method revela = FogGUI.class.getDeclaredMethod("revela", int.class, int.class, GraphicsContext.class);
        revela.setAccessible(true);

        Field reveladoField = FogGUI.class.getDeclaredField("revelado");
        reveladoField.setAccessible(true);
        boolean[][] revelado = (boolean[][]) reveladoField.get(gui);

        revela.invoke(gui, -1, 2, gc);

        for (boolean[] linha : revelado) {
            for (boolean celula : linha) {
                assertFalse(celula);
            }
        }
    }

    @Test
    @DisplayName("revela ignora posição já revelada")
    void shouldIgnoreAlreadyRevealedPosition() throws Exception {
        FogGUI gui = new FogGUI();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Method revela = FogGUI.class.getDeclaredMethod("revela", int.class, int.class, GraphicsContext.class);
        revela.setAccessible(true);

        Field reveladoField = FogGUI.class.getDeclaredField("revelado");
        reveladoField.setAccessible(true);
        boolean[][] revelado = (boolean[][]) reveladoField.get(gui);

        revela.invoke(gui, 3, 4, gc);
        assertTrue(revelado[4][3]);

        revela.invoke(gui, 3, 4, gc);
        assertTrue(revelado[4][3]);
    }
}