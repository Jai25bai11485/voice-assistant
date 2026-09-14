package com.voiceassistant.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    // a tiny fake Command just for testing, so this test doesn't depend on
    // any real command's keyword logic
    private static class FakeCommand implements Command {
        private final String keyword;
        private final String response;

        FakeCommand(String keyword, String response) {
            this.keyword = keyword;
            this.response = response;
        }

        @Override
        public boolean matches(String spokenText) {
            return spokenText.contains(keyword);
        }

        @Override
        public String execute(String spokenText) {
            return response;
        }
    }

    @Test
    void dispatchesToTheFirstMatchingCommand() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new FakeCommand("hello", "Hi there"));
        registry.register(new FakeCommand("bye", "Goodbye"));

        assertEquals("Hi there", registry.handle("hello there"));
        assertEquals("Goodbye", registry.handle("bye now"));
    }

    @Test
    void returnsFallbackMessageWhenNothingMatches() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new FakeCommand("hello", "Hi there"));

        String result = registry.handle("do something random");
        assertEquals("Sorry, I did not understand that command", result);
    }

    @Test
    void firstRegisteredMatchWinsOverLaterOnes() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new FakeCommand("open", "First handler"));
        registry.register(new FakeCommand("open", "Second handler"));

        assertEquals("First handler", registry.handle("open calculator"));
    }
}
