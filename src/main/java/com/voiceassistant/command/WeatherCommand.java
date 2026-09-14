package com.voiceassistant.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class WeatherCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("weather");
    }

    @Override
    public String execute(String spokenText) {
        try {
            // wttr.in gives back a plain text weather summary, no API key needed
            // format=3 means "just give me the short one line version"
            URI uri = URI.create("https://wttr.in/?format=3");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line = reader.readLine();
            reader.close();

            return "Here is the weather: " + line;
        } catch (Exception e) {
            return "I could not fetch the weather right now, check your internet connection";
        }
    }
}
