package com.example;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class ChuckJokesTest {

    @Test
    public void name() throws IOException {
        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJoke("Theo");

        assertThat(joke).isNotEmpty();
        assertThat(joke).contains("Theo");

    }
}