package com.voiceassistant.command;

// every action (open app, tell time, search google...) implements this
// this is what makes it easy to add a new command later without touching old ones
public interface Command {

    // returns true if this command should handle the given sentence
    boolean matches(String spokenText);

    // actually does the work, and returns what the assistant should say back
    String execute(String spokenText);
}
