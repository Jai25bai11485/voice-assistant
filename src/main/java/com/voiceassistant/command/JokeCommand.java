package com.voiceassistant.command;

import java.util.List;
import java.util.Random;

public class JokeCommand implements Command {

    private final List<String> jokes = List.of(
            "Why do programmers prefer dark mode? Because light attracts bugs.",
            "Why did the developer go broke? Because he used up all his cache.",
            "How many programmers does it take to change a light bulb? None, that's a hardware problem.",
            "Why do Java developers wear glasses? Because they cannot C sharp."
    );

    private final Random random = new Random();

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("joke");
    }

    @Override
    public String execute(String spokenText) {
        return jokes.get(random.nextInt(jokes.size()));
    }
}
