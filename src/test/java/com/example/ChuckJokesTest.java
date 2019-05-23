package com.example;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class ChuckJokesTest {

    @Test
    public void randomJoke_getJoke_jokeFound() throws IOException {
        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJoke("Theo");

        assertThat(joke).isNotEmpty();
        assertThat(joke).contains("Theo");

    }

    @Test
    public void searchWithQuery_getJoke_jokeForQueryFound() throws IOException {
        ChuckJokes chuckJokes = new ChuckJokes();
        Optional<String> joke = chuckJokes.getJoke("Theo", "cheese");

        assertThat(joke).isNotEmpty();
        assertThat(joke.get()).contains("Theo");
        assertThat(joke.get().toLowerCase()).contains("cheese");
    }

    @Test
    public void searchWithQueryThatCantBeFound_getJoke_noJokeForQueryFound() throws IOException {
        ChuckJokes chuckJokes = new ChuckJokes();
        Optional<String> joke = chuckJokes.getJoke("Theo", "fasdfsdfsdf");

        assertThat(joke).isEmpty();
    }

    @Test
    public void searchWithQueryWithSingleResult_getJoke_jokeForQueryFound() throws IOException {
        ChuckJokes chuckJokes = new ChuckJokes();
        Optional<String> joke = chuckJokes.getJoke("Theo", "paint%20nature");

        assertThat(joke).isNotEmpty();
        assertThat(joke.get()).contains("Theo");
        assertThat(joke.get().toLowerCase()).contains("paint");
    }
}