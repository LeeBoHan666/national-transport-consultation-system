import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.regex.*;

public class LLM {


    private static final HttpClient webClient = HttpClient.newHttpClient();

    public static String searchWeb(String query) {
        try {
            String url = "https://html.duckduckgo.com/html/?q="
                + URLEncoder.encode(query, StandardCharsets.UTF_8);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .timeout(java.time.Duration.ofSeconds(8))
                .GET().build();
            HttpResponse<String> resp = webClient.send(req, HttpResponse.BodyHandlers.ofString());
            return extractSnippets(resp.body());
        } catch (Exception e) {
            return "";
        }
    }

    private static String extractSnippets(String html) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("class=\"result__snippet\"[^>]*>(.*?)</");
        Matcher m = p.matcher(html);
        int count = 0;
        while (m.find() && count < 5) {
            String text = m.group(1).replaceAll("<[^>]+>", "").trim();
            if (!text.isEmpty()) {
                sb.append("- ").append(text).append("\n");
                count++;
            }
        }
        return sb.toString();
    }


    private final HttpClient client;

    public LLM() {
        this.client = HttpClient.newHttpClient();
    }
    public void chatStream(String prompt, Consumer<String> onToken) throws Exception {
        String json = "{\"model\":\"" + Main.Model + "\", \"prompt\":\"" + escape(prompt) + "\", \"stream\":true}";

        var builder = HttpRequest.newBuilder()
                .uri(URI.create(Main.URL))
                .header("Content-Type", "application/json");

        // 如果配了 API Key，就加上鉴权头
        if (Main.API != null && !Main.API.isEmpty()) {
            builder.header("Authorization", "Bearer " + Main.API);
        }

        HttpRequest request = builder
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<java.io.InputStream> response = client.send(
                request, HttpResponse.BodyHandlers.ofInputStream());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String error = extractJsonValue(line, "error");
                if (error != null) throw new RuntimeException(error);
                String token = extractJsonValue(line, "response");
                boolean done = line.contains("\"done\":true");
                if (token != null && !token.isEmpty()) {
                    onToken.accept(token);
                }
                if (done) break;
            }
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return null;
        start += search.length();
        StringBuilder val = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\') {
                i++;
                if (i < json.length()) {
                    switch (json.charAt(i)) {
                        case 'n': val.append('\n'); break;
                        case 't': val.append('\t'); break;
                        case '"': val.append('"'); break;
                        case '\\': val.append('\\'); break;
                        default: val.append(json.charAt(i));
                    }
                }
            } else if (c == '"') {
                break;
            } else {
                val.append(c);
            }
        }
        return val.toString();
    }
}
