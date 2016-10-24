package nl.fixx.asset.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class CallRestAuthAPI {
    public static String doCall(String username, String password) {
        try {
            StringBuilder sb = new StringBuilder("http://localhost:8080/oauth/token?grant_type=password&username=");
            sb.append(username);
            sb.append("&password=");
            sb.append(password);

            URL url = new URL(sb.toString());
            String userCredentials = "fixx-trusted-client:fixx_secret";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Authorization", basicAuth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String response = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                response += output;
            }

            conn.disconnect();

            return response;
        } catch (Exception ex) {
            return null;
        }
    }

}
