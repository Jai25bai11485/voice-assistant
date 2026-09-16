# Java Voice Assistant

An offline, Java-based voice assistant that listens through your microphone,
converts speech to text, matches it against a set of known commands, runs the
matching task on your computer, and speaks a confirmation back to you.

See [`statement.md`](statement.md) for the full problem statement, scope, and
target users, and [`docs/diagrams.md`](docs/diagrams.md) for the architecture,
workflow, use case, class, and sequence diagrams.

## Features

- Tells the current time and date
- Locks or shut down the computer
- Open common desktop apps (calculator, notepad, terminal, code editor)
- Search Google, YouTube, or Wikipedia in the default browser
- Play, pause, or skip local MP3 tracks
- Set a countdown timer / reminder that beeps and speaks when it finishes
- Fetch a one-line weather report (needs internet for this feature only)
- Tell a random pre-written joke

## Technologies & Tools Used

| Purpose | Tool / Library                                                                                                                       |
|---|--------------------------------------------------------------------------------------------------------------------------------------|
| Language | Java 17                                                                                                                              |
| Build tool | Maven                                                                                                                                |
| Speech-to-text (offline) | [Vosk](https://alphacephei.com/vosk/)                                                                                                |
| Text-to-speech | Operating system's own speech engine (Windows `System.Speech` via PowerShell, macOS `say`, Linux `espeak`) - no extra library needed |
| MP3 playback | [JLayer](http://www.javazoom.net/javalayer/javalayer.html)                                                                           |
| JSON parsing (Vosk output, weather API) | org.json                                                                                                                             |
| Microphone/audio capture | Java Sound API (`javax.sound.sampled`, built into Java)                                                                              |
| Launching apps/OS actions | `ProcessBuilder`, `Desktop` API (built into Java)                                                                                    |
| Testing | JUnit 5                                                                                                                              |

## Core Concepts Used

- **Multithreading** - the microphone listens on its own background thread, and each timer runs on its own thread, so nothing freezes the assistant while it waits.
- **Command Design Pattern** - every voice action is its own class implemeniting the `Command` interface, so new commands can be added without touching existing ones.
- **I/O Streams** - reading raw audio bytes from the microphonee and MP3 bytes from disk.
- **ProcessBuilder & Desktop API** - used to launch local apps and open the browser.
- **String manipulation & Regular Expresions** - cleaning transcribed text and pulling numbers out of sentences (e.g. for the timer).
- **Exception handling** - every risky operation (mic access, file access, network calls, launching processes) is wrapped so one failure doesn't crash the whole program.

## Project Structure

```
voice-assistant/
├── pom.xml
├── statement.md
├── README.md
├── docs/
│   ├── diagrams.md
│   ├── non-functional-requirements.md
│   └── diagrams/           (architecture, workflow, use case, class, sequence PNGs)
├── music/                  (put your own .mp3 files here)
└── src/
    ├── main/java/com/voiceassistant/
    │   ├── Main.java
    │   ├── audio/          (SpeechRecognizer, SpeechSynthesizer)
    │   ├── command/        (Command interface + every task class)
    │   └── util/           (MusicPlayer)
    └── test/java/com/voiceassistant/command/   (JUnit tests)
```

## Steps to Install & Run

### 1. Install the tools
- **Java 17+ (JDK)** - check with `java -version` - https://adoptium.net
- **Maven** - check with `mvn -version` - https://maven.apache.org/download.cgi
- **IntelliJ IDEA** (Community edition is free) - https://www.jetbrains.com/idea/download

### 2. Download the Vosk speech model
Vosk needs a language model file, downloaded separately (it is too large to bundle):
1. Go to https://alphacephei.com/vosk/models
2. Download `vosk-model-small-en-us-0.15` (~40 MB)
3. Unzip it
4. Place the unzipped folder inside the project root (same level as `pom.xml`)
5. Confirm the folder name matches `VOSK_MODEL_PATH` in `Main.java`

### 3. Add music files
1. Create (or use the existing) `music` folder in the project root
2. Drop a few `.mp3` files into it

### 4. Open the project
1. Open IntelliJ IDEA → **Open** → select the `voice-assistant` folder (the one with `pom.xml`)
2. IntelliJ detects the Maven project and downloads all dependencies automatically (needs internet the first time)
3. Wait for the progress bar at the bottom to finish

### 5. Allow microphone access
- **Windows:** Settings → Privacy & Security → Microphone → allow apps (and Java) to use the mic
- **Mac:** System Settings → Privacy & Security → Microphone → allow your IDE/terminal
- **Linux:** usually works by default; check `pavucontrol` if input seems muted

### 6. Run it
1. Open `Main.java`
2. Click the green run button next to `public static void main`
3. Say something like "what is the time" or "open calculator"

### 7. Build a standalone jar (optional)
```
mvn clean package
java -jar target/voice-assistant-1.0.jar
```

## Instructions for Testing

Unit tests live under `src/test/java` and cover the command-matching logic
(`TimeCommand`, `DateCommand`, `JokeCommand`, `CommandRegistry`). They do **not**
touch the microphone, speakers, or network, so they run instantly and reliably
anywhere.

Run all tests with:
```
mvn test
```

To test the assistant itself end-to-end, run `Main.java` and try each command
listed under **Features** above, one at a time, confirming both the console log
and the spoken response.

## Known Limitations

- Recognition accuracy is limited by the small ofline Vosk model - trading
  accuracy for speed, size, and zero cost. A larger Vosk model or a cloud
  speech API would improve accuracy at the cost of size/latency/privacy/money.
- Command matching uses plain keyword checks, not true natural language
  understanding, so phrasing has to roughly match the coded keywords.
- Voice quality depends on your OS's built-in voice - Windows and Mac sound
  reasonably natural, Linux's `espeak` sounds robotic by comparison.
- On Linux, `espeak` must be installed separately (`sudo apt install espeak`)
  since Linux does not ship a speech engine by default. Windows and macOS need
  no install - their spech engines are already built in.
- JLayer does not support true pause / resume, so "pause" currently stops
  playback at the current track rather than resuming mid-song.

## Future Enhancements

- Swap in a larger Vosk model or a cloud STT / NLU service for better accuracy
- Add fuzzy / synonym matching so more phrasings of the same command work
- Add a small GUI instead of console-only output
- Persist a command history / log to disk for review
