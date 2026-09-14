package com.voiceassistant.command;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WebBrowserCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("search") || spokenText.contains("youtube") || spokenText.contains("wikipedia");
    }

    @Override
    public String execute(String spokenText) {
        try {
            if (spokenText.contains("youtube")) {
                String query = cleanQuery(spokenText, "youtube");
                open("https://www.youtube.com/results?search_query=" + encode(query));
                return "Opening YouTube" + (query.isEmpty() ? "" : " and searching for " + query);
            } else if (spokenText.contains("wikipedia")) {
                String query = cleanQuery(spokenText, "wikipedia");
                open("https://en.wikipedia.org/wiki/Special:Search?search=" + encode(query));
                return "Looking up " + query + " on Wikipedia";
            } else {
                String query = cleanQuery(spokenText, "search");
                open("https://www.google.com/search?q=" + encode(query));
                return "Searching Google for " + query;
            }
        } catch (IOException e) {
            return "I could not open the browser: " + e.getMessage();
        }
    }

    // strips filler words like "search for", "on youtube" so only the real topic is left
    private String cleanQuery(String text, String keyword) {
        String cleaned = text.replace(keyword, "")
                .replace("on", "")
                .replace("for", "")
                .replace("search", "")
                .trim();
        return cleaned;
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    private void open(String url) throws IOException {
        Desktop.getDesktop().browse(URI.create(url));
    }
}
