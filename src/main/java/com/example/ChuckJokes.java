package com.example;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChuckJokes {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChuckJokes.class);
    public static final String CHUCK_NORRIS = "Chuck Norris";
    public static final String CHUCK = "Chuck";

    public String getJoke(String name) {

        if (name == null) {
            name = CHUCK;
            LOGGER.warn("Name was not provided.. ");
        }

        try {

            URL obj = new URL("https://api.chucknorris.io/jokes/random");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            LOGGER.info("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject myResponse = new JSONObject(response.toString());
            String joke = myResponse.getString("value");

            LOGGER.info(("RESPONSE " + response.toString()));

            String updatedJoke = joke.replace(CHUCK_NORRIS, name).replace(CHUCK, name);

            LOGGER.info(("UPDATED JOKE " + updatedJoke));
            return updatedJoke;
        } catch (Exception e) {
            LOGGER.error("Error " + e.getMessage(), e);
            return "Error " + e.getMessage();
        }
    }

}
