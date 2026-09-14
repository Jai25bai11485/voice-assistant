package com.voiceassistant.command;

import com.voiceassistant.audio.SpeechSynthesizer;

import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimerCommand implements Command {

    // matches things like "set a timer for 5 minutes" or "timer for 30 seconds"
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)\\s*(second|minute)");

    private final SpeechSynthesizer synthesizer;

    public TimerCommand(SpeechSynthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("timer") || spokenText.contains("reminder");
    }

    @Override
    public String execute(String spokenText) {
        Matcher matcher = TIME_PATTERN.matcher(spokenText);
        if (!matcher.find()) {
            return "I did not catch how long you want the timer for";
        }

        int amount = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        long millis = unit.equals("minute") ? amount * 60_000L : amount * 1000L;

        // the countdown itself runs on a separate thread so it doesn't block new voice commands
        Thread timerThread = new Thread(() -> {
            try {
                Thread.sleep(millis);
                Toolkit.getDefaultToolkit().beep();
                synthesizer.speak("Time is up for your " + amount + " " + unit + " timer");
            } catch (InterruptedException e) {
                // timer was cancelled somehow, nothing to do here
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();

        return "Timer set for " + amount + " " + unit + (amount > 1 ? "s" : "");
    }
}
