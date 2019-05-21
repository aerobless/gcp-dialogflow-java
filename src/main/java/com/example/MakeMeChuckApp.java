/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeMeChuckApp extends DialogflowApp {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MakeMeChuckApp.class);

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
        String givenName = (String) request.getParameter("given-name");

        ResourceBundle responses = ResourceBundle.getBundle("responses");
        String greetName = responses.getString("greetName");

        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJoke(givenName);
        LOGGER.info("JOKELOG" + joke);

        return getResponseBuilder(request)
                .add(MessageFormat.format(greetName, givenName))
                .add(" Here is your joke " + joke)
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