package com.voiceassistant.command;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {

    private final List<Command> commands = new ArrayList<>();

    public void register(Command command) {
        commands.add(command);
    }

    // loops through every registered command and asks "is this one yours?"
    // first match wins, so more specific commands should be registered before general ones
    public String handle(String spokenText) {
        String cleaned = spokenText.toLowerCase().trim();

        for (Command command : commands) {
            if (command.matches(cleaned)) {
                return command.execute(cleaned);
            }
        }
        return "Sorry, I did not understand that command";
    }
}
