package com.voiceassistant.audio;

import java.io.IOException;

// speaks text out loud using whatever speech engine the operating system already
// ships with - no external library needed, so Maven has nothing extra to download
public class SpeechSynthesizer {

    private final String os = System.getProperty("os.name").toLowerCase();

    public void speak(String text) {
        System.out.println("Assistant: " + text);
        try {
            if (os.contains("win")) {
                speakOnWindows(text);
            } else if (os.contains("mac")) {
                speakOnMac(text);
            } else {
                speakOnLinux(text);
            }
        } catch (Exception e) {
            // if the OS voice engine is missing, don't crash, just keep going text-only
            System.out.println("(could not speak out loud: " + e.getMessage() + ")");
        }
    }

    // Windows ships System.Speech in every install, reachable through PowerShell
    private void speakOnWindows(String text) throws IOException, InterruptedException {
        // '' is how a single quote is escaped inside a PowerShell single-quoted string
        String safeText = text.replace("'", "''");
        String psCommand = "Add-Type -AssemblyName System.Speech; " +
                "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" + safeText + "');";
        new ProcessBuilder("powershell", "-Command", psCommand).start().waitFor();
    }

    // macOS ships the "say" command with every install
    private void speakOnMac(String text) throws IOException, InterruptedException {
        new ProcessBuilder("say", text).start().waitFor();
    }

    // Linux does not ship a speech engine by default, so this relies on espeak
    // being installed (sudo apt install espeak). If it's missing, we just log
    // the text instead of speaking it, so the assistant still keeps working.
    private void speakOnLinux(String text) throws IOException, InterruptedException {
        new ProcessBuilder("espeak", text).start().waitFor();
    }
}
