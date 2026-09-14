package com.voiceassistant.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateCommandTest {

    private final DateCommand command = new DateCommand();

    @Test
    void matchesDateKeyword() {
        assertTrue(command.matches("what is the date today"));
    }

    @Test
    void matchesTodayKeyword() {
        assertTrue(command.matches("what is today"));
    }

    @Test
    void doesNotMatchUnrelatedSentence() {
        assertFalse(command.matches("play music"));
    }

    @Test
    void executeReturnsASpokenDateSentence() {
        String result = command.execute("what is the date");
        assertTrue(result.startsWith("Today is"));
    }
}
