package Services.Utilisateur.Crud;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BrevoEmailSender {

    private static final String API_KEY = System.getenv("SENDINBLUE_API_KEY");
    private static final String API_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String SENDER_EMAIL = "khalilabdelmoumen7@gmail.com"; // Vérifié dans Brevo

    public void sendEmail(String toEmail, String subject, String htmlContent) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("api-key", API_KEY);
            httpPost.setHeader("Content-Type", "application/json");

            // Construire le JSON avec org.json
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sender", new JSONObject().put("email", SENDER_EMAIL));
            jsonBody.put("to", new JSONArray().put(new JSONObject().put("email", toEmail)));
            jsonBody.put("subject", subject);
            jsonBody.put("htmlContent", htmlContent); // Pas besoin d'échapper manuellement

            StringEntity entity = new StringEntity(jsonBody.toString(), "UTF-8");
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);

                if (statusCode >= 200 && statusCode < 300) {
                    System.out.println("Email envoyé avec succès. Réponse : " + responseString);
                } else {
                    throw new Exception("Échec de l'envoi de l'email. Code : " + statusCode + ", Réponse : " + responseString);
                }
            }
        }
    }
}