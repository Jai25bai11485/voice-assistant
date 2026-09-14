package com.voiceassistant.command;

import java.io.IOException;

public class SystemPowerCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("lock") || spokenText.contains("shut down") || spokenText.contains("shutdown");
    }

    @Override
    public String execute(String spokenText) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (spokenText.contains("lock")) {
                runLock(os);
                return "Locking your computer";
            } else {
                runShutdown(os);
                return "Shutting down your computer";
            }
        } catch (IOException e) {
            // if the OS command fails, don't crash the whole assistant, just report it
            return "I could not do that, something went wrong: " + e.getMessage();
        }
    }

    private void runLock(String os) throws IOException {
        if (os.contains("win")) {
            new ProcessBuilder("rundll32.exe", "user32.dll,LockWorkStation").start();
        } else if (os.contains("mac")) {
            new ProcessBuilder("/System/Library/CoreServices/Menu Extras/User.menu/Contents/Resources/CGSession", "-suspend").start();
        } else {
            new ProcessBuilder("xdg-screensaver", "lock").start();
        }
    }

    private void runShutdown(String os) throws IOException {
        if (os.contains("win")) {
            new ProcessBuilder("shutdown", "/s", "/t", "5").start();
        } else if (os.contains("mac")) {
            new ProcessBuilder("osascript", "-e", "tell app \"System Events\" to shut down").start();
        } else {
            new ProcessBuilder("shutdown", "-h", "now").start();
        }
    }
}
