package com.voiceassistant.command;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("time");
    }

    @Override
    public String execute(String spokenText) {
        // grab the system clock and format it like "3:45 PM" instead of raw 24hr format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String now = LocalTime.now().format(formatter);
        return "The current time is " + now;
    }
}
