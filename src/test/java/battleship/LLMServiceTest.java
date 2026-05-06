package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Test class for the LLMService class.
 * Author: 99371
 * Date: 2026-05-06
 * Cyclomatic Complexity:
 * - constructor: 1
 * - generateRaw: 2
 * - generateJSON: 1
 * - cleanJSON: 3
 * - removeMarkdownFences: 2
 * - RequestBody.constructor: 1
 */
public class LLMServiceTest {

    private LLMService llmService;

    @BeforeEach
    void setUp() {
        llmService = new LLMService();
    }

    @AfterEach
    void tearDown() {
        llmService = null;
    }

    // ----------------------------------------------------------------
    // constructor (CC = 1)
    // ----------------------------------------------------------------

    @Test
    void constructor() {
        assertNotNull(llmService, "Error: LLMService instance should not be null.");
    }

    // ----------------------------------------------------------------
    // generateRaw (CC = 2)
    // path 1: normal execution trying to connect (throws Exception if server is offline)
    // path 2: execution with null prompt triggering early exception in mapper
    // Note: Due to hardcoded HttpURLConnection, we test the exception handling paths.
    // ----------------------------------------------------------------

    @Test
    void generateRaw1() {
        assertThrows(Exception.class,
                () -> llmService.generateRaw("Hello, World!"),
                "Error: Expected an Exception to be thrown (e.g., ConnectException) when Ollama server is unreachable."
        );
    }

    @Test
    void generateRaw2() {
        assertThrows(Exception.class,
                () -> llmService.generateRaw(null),
                "Error: Expected an Exception to be thrown when prompt is null."
        );
    }

    // ----------------------------------------------------------------
    // generateJSON (CC = 1)
    // path 1: Attempts to generate JSON and fails due to network/parsing
    // ----------------------------------------------------------------

    @Test
    void generateJSON() {
        assertThrows(Exception.class,
                () -> llmService.generateJSON("Give me JSON", String.class),
                "Error: Expected an Exception to be thrown during network call or JSON mapping."
        );
    }

    // ----------------------------------------------------------------
    // cleanJSON (CC = 3)
    // compound condition: if (start != -1 && end != -1)
    // path 1: start != -1 (true) && end != -1 (true) -> executes substring
    // path 2: start != -1 (true) && end != -1 (false) -> skips substring
    // path 3: start != -1 (false) -> short circuits &&, skips substring
    // ----------------------------------------------------------------

    @Test
    void cleanJSON1() throws Exception {
        Method cleanJSON = LLMService.class.getDeclaredMethod("cleanJSON", String.class);
        cleanJSON.setAccessible(true);

        String input = "random text [{\"key\":\"value\"}] more text";
        String actual = (String) cleanJSON.invoke(llmService, input);

        assertEquals("[{\"key\":\"value\"}]", actual, "Error: expected clean JSON bracket string but got different result.");
    }

    @Test
    void cleanJSON2() throws Exception {
        Method cleanJSON = LLMService.class.getDeclaredMethod("cleanJSON", String.class);
        cleanJSON.setAccessible(true);

        String input = "missing end bracket [";
        String actual = (String) cleanJSON.invoke(llmService, input);

        assertEquals(input, actual, "Error: expected string to remain unchanged when end bracket is missing.");
    }

    @Test
    void cleanJSON3() throws Exception {
        Method cleanJSON = LLMService.class.getDeclaredMethod("cleanJSON", String.class);
        cleanJSON.setAccessible(true);

        String input = "missing start bracket ]";
        String actual = (String) cleanJSON.invoke(llmService, input);

        assertEquals(input, actual, "Error: expected string to remain unchanged when start bracket is missing.");
    }

    // ----------------------------------------------------------------
    // removeMarkdownFences (CC = 2)
    // path 1: raw.startsWith("```") is true -> cleans markdown
    // path 2: raw.startsWith("```") is false -> returns unchanged
    // ----------------------------------------------------------------

    @Test
    void removeMarkdownFences1() throws Exception {
        Method removeMarkdownFences = LLMService.class.getDeclaredMethod("removeMarkdownFences", String.class);
        removeMarkdownFences.setAccessible(true);

        String input = "```json\n[1, 2, 3]\n```";
        String actual = (String) removeMarkdownFences.invoke(null, input); // static method, target is null

        assertEquals("[1, 2, 3]", actual, "Error: expected markdown fences to be removed.");
    }

    @Test
    void removeMarkdownFences2() throws Exception {
        Method removeMarkdownFences = LLMService.class.getDeclaredMethod("removeMarkdownFences", String.class);
        removeMarkdownFences.setAccessible(true);

        String input = "[1, 2, 3]";
        String actual = (String) removeMarkdownFences.invoke(null, input);

        assertEquals(input, actual, "Error: expected string without markdown fences to remain unchanged.");
    }

    // ----------------------------------------------------------------
    // RequestBody constructor (CC = 1)
    // Testing inner private class via reflection
    // ----------------------------------------------------------------

    @Test
    void requestBodyConstructor() throws Exception {
        Class<?> requestBodyClass = Class.forName("battleship.LLMService$RequestBody");
        Constructor<?> constructor = requestBodyClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        Object requestBody = constructor.newInstance("test prompt");

        Field promptField = requestBodyClass.getDeclaredField("prompt");
        promptField.setAccessible(true);
        String actualPrompt = (String) promptField.get(requestBody);

        assertAll(
                () -> assertNotNull(requestBody, "Error: RequestBody instance should not be null."),
                () -> assertEquals("test prompt", actualPrompt, "Error: expected prompt to match the initialized value.")
        );
    }
}
