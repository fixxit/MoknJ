package nl.fixx.asset.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class CallRestAuthAPI {
    public static String doCall() {
	try {
	    URL url = new URL("http://localhost:8080/oauth/token?grant_type=password&username=fixxit&password=test");
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
