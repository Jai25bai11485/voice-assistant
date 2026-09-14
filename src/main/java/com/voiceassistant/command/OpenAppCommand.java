package com.voiceassistant.command;

import java.io.IOException;

public class OpenAppCommand implements Command {

    @Override
    public boolean matches(String spokenText) {
        return spokenText.startsWith("open") &&
                (spokenText.contains("calculator") || spokenText.contains("notepad")
                        || spokenText.contains("terminal") || spokenText.contains("code editor")
                        || spokenText.contains("vscode"));
    }

    @Override
    public String execute(String spokenText) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (spokenText.contains("calculator")) {
                launch(os, "calc", "Calculator", "gnome-calculator");
                return "Opening calculator";
            } else if (spokenText.contains("notepad")) {
                launch(os, "notepad", "TextEdit", "gedit");
                return "Opening notepad";
            } else if (spokenText.contains("terminal")) {
                launch(os, "cmd", "Terminal", "gnome-terminal");
                return "Opening terminal";
            } else {
                // code editor / vscode
                launch(os, "code", "code", "code");
                return "Opening your code editor";
            }
        } catch (IOException e) {
            return "I could not open that app: " + e.getMessage();
        }
    }

    // picks the right binary name per operating system and starts it as its own process
    private void launch(String os, String windowsCmd, String macCmd, String linuxCmd) throws IOException {
        if (os.contains("win")) {
            new ProcessBuilder(windowsCmd).start();
        } else if (os.contains("mac")) {
            new ProcessBuilder("open", "-a", macCmd).start();
        } else {
            new ProcessBuilder(linuxCmd).start();
        }
    }
}
