package com.voiceassistant.audio;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class SpeechSynthesizer {

    private final Voice voice;

    public SpeechSynthesizer() {
        // "kevin16" ships with freetts by default, no extra download needed
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("kevin16");
        voice.allocate();
    }

    public void speak(String text) {
        System.out.println("Assistant: " + text);
        voice.speak(text);
    }

    public void shutdown() {
        voice.deallocate();
    }
}
