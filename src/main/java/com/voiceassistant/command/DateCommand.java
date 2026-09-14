package com.voiceassistant.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("date") || spokenText.contains("today");
    }

    @Override
    public String execute(String spokenText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return "Today is " + LocalDate.now().format(formatter);
    }
}
