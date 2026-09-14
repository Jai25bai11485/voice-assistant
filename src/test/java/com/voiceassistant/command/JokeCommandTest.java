package com.voiceassistant.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JokeCommandTest {

    private final JokeCommand command = new JokeCommand();

    @Test
    void matchesJokeKeyword() {
        assertTrue(command.matches("tell me a joke"));
    }

    @Test
    void doesNotMatchUnrelatedSentence() {
        assertFalse(command.matches("set a timer"));
    }

    @Test
    void executeNeverReturnsBlank() {
        // run it several times since the joke is picked randomly
        for (int i = 0; i < 20; i++) {
            String result = command.execute("tell me a joke");
            assertNotNull(result);
            assertFalse(result.isBlank());
        }
    }
}
