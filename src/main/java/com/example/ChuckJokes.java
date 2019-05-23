package com.example;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ChuckJokes {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChuckJokes.class);
    private static final String CHUCK_NORRIS = "Chuck Norris";
    private static final String CHUCK = "Chuck";
    private static final String RANDOM_JOKE_ENDPOINT = "https://api.chucknorris.io/jokes/random";
    private static final String QUERY_JOKE_ENDPOINT = "https://api.chucknorris.io/jokes/search?query=";

    public Optional<String> getJoke(String name, String searchQuery) {

        if (name == null) {
            name = CHUCK;
            LOGGER.warn("Name was not provided.. ");
        }

        if (searchQuery == null) {
            searchQuery = "swiss";
            LOGGER.warn("No search query was not provided.. ");
        }

        try {
            URL obj = new URL(QUERY_JOKE_ENDPOINT + searchQuery);
            JSONObject myResponse = getJsonResponseFromRequest(obj);
            int totalNumberOfJokes = myResponse.getInt("total");

            if (totalNumberOfJokes == 0) {
                LOGGER.warn("No jokes found for query " + searchQuery);
                return Optional.empty();
            }

            int randomJoke = ThreadLocalRandom.current().nextInt(0, totalNumberOfJokes);

            JSONObject singleJoke = (JSONObject) myResponse.getJSONArray("result").get(randomJoke);
            String joke = singleJoke.getString("value");

            return Optional.of(joke.replace(CHUCK_NORRIS, name).replace(CHUCK, name));
        } catch (Exception e) {
            LOGGER.error("Error " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    public String getJoke(String name) {

        if (name == null) {
            name = CHUCK;
            LOGGER.warn("Name was not provided.. ");
        }

        try {
            URL obj = new URL(RANDOM_JOKE_ENDPOINT);
            JSONObject myResponse = getJsonResponseFromRequest(obj);
            String joke = myResponse.getString("value");
            return joke.replace(CHUCK_NORRIS, name).replace(CHUCK, name);
        } catch (Exception e) {
            LOGGER.error("Error " + e.getMessage(), e);
            return "Error " + e.getMessage();
        }
    }

    @NotNull
    private JSONObject getJsonResponseFromRequest(URL obj) throws IOException {
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
        return new JSONObject(response.toString());
    }

}
