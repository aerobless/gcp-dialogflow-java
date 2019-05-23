package com.example;

import com.google.actions.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class MakeMeChuckApp extends DialogflowApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MakeMeChuckApp.class);
    private static final String GIVEN_NAME = "given-name";
    public static final String JOKE_TOPIC = "joke-topic";
    public static final String JOKE_TYPE = "joke-type";
    public static final String JOKE_TYPE_TOPIC = "topic";

    @ForIntent("Welcome")
    public ActionResponse welcome(ActionRequest request) {
        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String welcome = responses.getString("welcome");

        return getResponseBuilder(request)
                .add(welcome) // Hello there, what's your name?
                .build();
    }

    @ForIntent("UserProvidesFirstnameAndSelectsJokeType")
    public ActionResponse userProvidesFirstnameAndSelectsJokeType(ActionRequest request) throws IOException {
        String givenName = (String) request.getParameter(GIVEN_NAME);

        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String greetName = responses.getString("greetName");
        String yes = responses.getString("yes");
        String no = responses.getString("no");
        String specificJoke = responses.getString("specificJoke");

        return getResponseBuilder(request)
                .add(MessageFormat.format(greetName, givenName))
                .addSuggestions(new String[]{yes, specificJoke, no})
                .build();
    }

    @ForIntent("UserWantsRandomJoke")
    public ActionResponse userWantsRandomJoke(ActionRequest request) throws IOException {
        Optional<String> optionalName = getFromContext(GIVEN_NAME, "userprovidesfirstname-followup", request);
        if (!optionalName.isPresent()) {
            fallback(request);
        }

        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJoke(optionalName.get());
        LOGGER.info("JOKELOG" + joke);

        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String anotherJoke = responses.getString("anotherJoke");
        String pleaseGoAway = responses.getString("pleaseGoAway");
        String specificJoke = responses.getString("specificJoke");

        return getResponseBuilder(request)
                .add(joke)
                .addSuggestions(new String[]{anotherJoke, specificJoke, pleaseGoAway})
                .build();
    }

    @ForIntent("UserWantsAnotherJoke")
    public ActionResponse userWantsAnotherJoke(ActionRequest request) throws IOException {
        Optional<String> optionalJokeType = getFromContext(JOKE_TYPE, "_actions_on_google", request);
        if (!optionalJokeType.isPresent()) {
            LOGGER.info("jokeType is not present, falling back to random joke");
            return userWantsRandomJoke(request);
        }

        String jokeType = optionalJokeType.get();
        if (jokeType.toLowerCase().equals(JOKE_TYPE_TOPIC)) {
            LOGGER.info("jokeType present: TOPIC");
            return userWantsSpecificJoke(request);
        } else {
            LOGGER.info("jokeType present: RANDOM");
            return userWantsRandomJoke(request);
        }

    }

    @ForIntent("UserProvidesJokeTopic")
    public ActionResponse userWantsSpecificJoke(ActionRequest request) throws IOException {
        Optional<String> optionalName = getFromContext(GIVEN_NAME, "userprovidesfirstname-followup", request);
        if (!optionalName.isPresent()) {
            fallback(request);
        }

        Optional<String> optionalJokeKeyword = getFromContext(JOKE_TOPIC, "await_jokekeyword", request);
        if (!optionalJokeKeyword.isPresent()) {
            fallback(request);
        }

        ChuckJokes chuckJokes = new ChuckJokes();
        Optional<String> optionalJoke = chuckJokes.getJoke(optionalName.get(), optionalJokeKeyword.get());

        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String noJokeFound = responses.getString("noJokeFound");
        String changeJokeTopic = responses.getString("changeJokeTopic");
        String pleaseGoAway = responses.getString("pleaseGoAway");
        String anotherJoke = responses.getString("anotherJoke");

        if (!optionalJoke.isPresent()) {
            return getResponseBuilder(request)
                    .add(noJokeFound)
                    .addSuggestions(new String[]{changeJokeTopic, pleaseGoAway})
                    .build();
        }

        LOGGER.info("JOKELOG " + optionalJoke.get());

        return getResponseBuilder(request)
                .add(optionalJoke.get())
                .addSuggestions(new String[]{anotherJoke, changeJokeTopic, pleaseGoAway})
                .build();
    }

    private Optional<String> getFromContext(String paramName, String contextName, ActionRequest request) {
        ActionContext context = request.getContext(request.getSessionId() + "/contexts/" + contextName);

        if (context == null || context.getParameters() == null || ((String) context.getParameters().get(paramName)).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of((String) context.getParameters().get(paramName));
    }

    @ForIntent("Fallback")
    public ActionResponse fallback(ActionRequest request) {
        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String didNotUnderstand = responses.getString("didNotUnderstand");

        return getResponseBuilder(request)
                .add(didNotUnderstand)
                .build();
    }

}