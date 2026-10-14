import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.*;
import java.util.function.*;
import java.util.regex.*;
import com.google.gson.*;

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
                try {
                    JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                    if (obj.has("error")) {
                        String err = obj.get("error").getAsString();
                        if (!err.isEmpty()) throw new RuntimeException(err);
                    }
                    if (obj.has("response")) {
                        String token = obj.get("response").getAsString();
                        if (!token.isEmpty()) onToken.accept(token);
                    }
                    if (obj.has("done") && obj.get("done").getAsBoolean()) break;
                } catch (JsonSyntaxException ignored) {
                    // 跳过解析失败的行（极少发生）
                }
            }
        }
    }


    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }


}
