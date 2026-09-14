package com.voiceassistant.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeCommandTest {

    private final TimeCommand command = new TimeCommand();

    @Test
    void matchesWhenSentenceContainsTime() {
        assertTrue(command.matches("what is the time"));
    }

    @Test
    void doesNotMatchUnrelatedSentence() {
        assertFalse(command.matches("open calculator"));
    }

    @Test
    void executeReturnsASpokenTimeSentence() {
        String result = command.execute("what is the time");
        // we cannot check an exact clock value since the test can run at any second,
        // so we just check the response has the shape we expect
        assertTrue(result.startsWith("The current time is"));
    }
}
