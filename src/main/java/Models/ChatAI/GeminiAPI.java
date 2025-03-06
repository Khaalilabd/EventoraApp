package Models.ChatAI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GeminiAPI {
    private static final String API_KEY = "AIzaSyAmD5dvjLsq3xDhtH4KwzL8NxvArMWsVBo"; // ⚠️ Mets ici ta vraie clé API
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    public static String getResponseFromGemini(String userInput) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(API_URL);
            request.setHeader("Content-Type", "application/json");

            // Construction du JSON
            JsonObject jsonRequest = new JsonObject();
            JsonObject content = new JsonObject();
            JsonObject part = new JsonObject();
            part.addProperty("text", userInput);
            content.add("parts", part);
            jsonRequest.add("contents", content);

            request.setEntity(new StringEntity(new Gson().toJson(jsonRequest), ContentType.APPLICATION_JSON));

            // Exécution de la requête
            try (CloseableHttpResponse response = httpClient.execute(request);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            }
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}