package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LLMService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "llama3:8b-instruct-q4_0";
    public static final double TEMPERATURE = 0.2;

    private final ObjectMapper mapper = new ObjectMapper();

    public String generateRaw(String prompt) throws Exception {
        URL url = new URL(OLLAMA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = mapper.writeValueAsString(new RequestBody(prompt));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        JsonNode root = mapper.readTree(response.toString());

        return root.get("response").asText(); //
    }

    public <T> T generateJSON(String prompt, Class<T> clazz) throws Exception {
        String raw = generateRaw(prompt);

        System.out.println("Resposta Raw: " + raw); // debug

        return mapper.readValue(cleanJSON(raw), clazz);
    }

    private String cleanJSON(String raw) {
        raw = raw.trim();

        raw = removeMarkdownFences(raw);

        int start = raw.indexOf("[");
        int end = raw.lastIndexOf("]");

        if (start != -1 && end != -1) {
            raw = raw.substring(start, end + 1);
        }

        return raw;
    }

    private static @NotNull String removeMarkdownFences(String raw) {
        if (raw.startsWith("```")) {
            raw = raw.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();
        }
        return raw;
    }

    //
    private static class RequestBody {
        public String model = MODEL;
        public String prompt;
        public boolean stream = false;
        public double temperature = TEMPERATURE;

        public RequestBody(String prompt) {
            this.prompt = prompt;
        }
    }
}