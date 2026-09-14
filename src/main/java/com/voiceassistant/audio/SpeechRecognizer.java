package com.voiceassistant.audio;

import org.json.JSONObject;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class SpeechRecognizer {

    // this gets called every time Vosk finishes understanding one full sentence
    public interface Listener {
        void onTextRecognized(String text);
    }

    private final Model model;
    private volatile boolean listening = false;

    public SpeechRecognizer(String modelPath) throws Exception {
        // this loads the offline language model from disk, it does NOT need internet
        model = new Model(modelPath);
    }

    // this method blocks forever, so it must always be called from its own thread
    public void startListening(Listener listener) {
        listening = true;

        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try (Recognizer recognizer = new Recognizer(model, 16000)) {
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            byte[] buffer = new byte[4096];

            while (listening) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);

                // acceptWaveForm returns true once it has heard a complete phrase
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String jsonResult = recognizer.getResult();
                    String text = extractText(jsonResult);
                    if (!text.isBlank()) {
                        listener.onTextRecognized(text);
                    }
                }
            }
            microphone.stop();
            microphone.close();
        } catch (Exception e) {
            System.out.println("Microphone error: " + e.getMessage());
        }
    }

    public void stopListening() {
        listening = false;
    }

    // Vosk gives back JSON like {"text" : "open calculator"}, pull just the words out of it
    private String extractText(String jsonResult) {
        JSONObject obj = new JSONObject(jsonResult);
        return obj.optString("text", "");
    }
}
