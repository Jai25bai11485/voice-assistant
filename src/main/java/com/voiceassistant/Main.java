package com.voiceassistant;

import com.voiceassistant.audio.SpeechRecognizer;
import com.voiceassistant.audio.SpeechSynthesizer;
import com.voiceassistant.command.*;

public class Main {

    // change this to wherever you will unzip the Vosk model folder downloaded by you
    private static final String VOSK_MODEL_PATH = "D:\\Java_All_Codes\\Java_project_vityarthi\\voice-assistant\\vosk-model-small-en-us-0.15";

    // change this to your own music folder where you have your favorite songs
    private static final String MUSIC_FOLDER_PATH = "music" ;

    public static void main( String[] args) {
        SpeechSynthesizer synthesizer = new SpeechSynthesizer() ;
        CommandRegistry registry = buildRegistry( synthesizer);

        synthesizer.speak("Voice assistant is ready, I am listening");

        try {
            SpeechRecognizer recognizer = new SpeechRecognizer(VOSK_MODEL_PATH) ;

            // the mic has to run on its own thread, otherwise it would freze
            // everything else while it waits for you to speak
            Thread listenerThread = new Thread(() ->
                    recognizer.startListening(spokenText -> {
                        System.out.println("You said: " + spokenText) ;

                        if (spokenText.contains("exit") || spokenText.contains("stop listening")) {
                            synthesizer.speak("Goodbye") ;
                            recognizer.stopListening() ;
                            System.exit(0);
                        }

                        String response = registry.handle(spokenText);
                        synthesizer.speak(response) ;
                    })
            );
            listenerThread.start() ;

        } catch (Exception e) {
            System.out.println("Could not start speech recognizer: " + e.getMessage()) ;
            synthesizer.speak("I could not access the microphone or the voice model") ;
        }
    }

    // this is where every comand gets wired up
    // order matters a little: more specific matches should usualy come first

    private static CommandRegistry buildRegistry(SpeechSynthesizer synthesizer) {
        CommandRegistry registry = new CommandRegistry() ;

        registry.register(new TimeCommand());
        registry.register(new DateCommand());
        registry.register(new SystemPowerCommand()) ;
        registry.register(new OpenAppCommand());
        registry.register(new WebBrowserCommand()) ;
        registry.register(new MediaPlayerCommand( MUSIC_FOLDER_PATH));
        registry.register(new TimerCommand(synthesizer));
        registry.register(new WeatherCommand()) ;
        registry.register(new JokeCommand());

        return registry ;
    }
}
