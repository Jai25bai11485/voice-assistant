# Non-Functional Requirements

These describe **how well** the system behaves, not what features it has.

## 1. Performance
The assistant must respond to a recognized command within about 1-2 seconds on a
normal laptop, so the conversation feels natural instead of laggy. This is why the
microphone listening loop and the timer countdown each run on their own background
thread - neither one is allowed to block the main response path.

## 2. Reliability
A single failure (microphone briefly disconnecting, a website not opening, a weather
API timeout) must not crash the whole assistant. Every risky operation - file I/O,
network calls, launching external processes - is wrapped in a try/catch block that
reports the problem out loud and keeps listening for the next command.

## 3. Usability
Commands should work with natural, short phrases ("what's the time", "open
calculator") rather than requiring an exact rigid syntax. Every action also gets a
spoken confirmation, so the user always knows whether the assistant understood them,
even without looking at the screen.

## 4. Resource Efficiency
The assistant uses a small (~40MB) offline Vosk model instead of a large one, and
loads the speech model only once at startup, not on every command. This keeps both
memory use and startup time low enough to run comfortably on an average laptop.

## 5. Maintainability
New commands can be added by creating one new class that implements the `Command`
interface and registering it in `Main.java` - no existing command classes need to be
changed. This is the direct benefit of using the Command design pattern.

## 6. Error Handling Strategy
Every command follows the same pattern: try the action, and if it fails, return a
plain-English spoken explanation instead of letting an exception crash the program or
silently doing nothing. The user is always told when something didn't work, not left
guessing.
