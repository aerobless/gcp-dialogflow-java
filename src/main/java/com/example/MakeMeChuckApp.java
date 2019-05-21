package com.example;

import com.google.actions.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MakeMeChuckApp extends DialogflowApp {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MakeMeChuckApp.class);
    private static final String GIVEN_NAME = "given-name";

    @ForIntent("Welcome")
    public ActionResponse welcome(ActionRequest request) {
        // Build the "welcome" response, which contains the "askExample" response as a substitution
        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String welcome = responses.getString("welcome");
        String askExample = responses.getString("askExample");

        return getResponseBuilder(request)
                .add(MessageFormat.format(welcome, askExample))
                .build();
    }

    @ForIntent("ProvideName")
    public ActionResponse provideName(ActionRequest request) throws IOException {
        String givenName = (String) request.getParameter(GIVEN_NAME);

        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String greetName = responses.getString("greetName");

        return getResponseBuilder(request)
                .add(MessageFormat.format(greetName, givenName))
                .build();
    }

    @ForIntent("TellRandomJoke")
    public ActionResponse tellRandomJoke(ActionRequest request) throws IOException {
        ActionContext context = request.getContext(request.getSessionId() + "/contexts/providename-followup");

        String givenName = "Chuck Fallback"; //TODO: write proper fallback
        if (context != null && context.getParameters() != null) {
            givenName = (String) context.getParameters().get(GIVEN_NAME);
        }

        if (givenName == null) {
            givenName = "Chuck Fallback"; //TODO: write proper fallback
        }

        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJoke(givenName);
        LOGGER.info("JOKELOG" + joke);

        return getResponseBuilder(request)
                .add("Here is your joke: " + joke)
                .build();
    }

    @ForIntent("Fallback")
    public ActionResponse fallback(ActionRequest request) {
        // Build the "didNotUnderstand" response, which contains the "askExample"
        // response as a substitution
        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String didNotUnderstand = responses.getString("didNotUnderstand");
        String askExample = responses.getString("askExample");

        return getResponseBuilder(request)
                .add(MessageFormat.format(didNotUnderstand, askExample))
                .build();
    }

}