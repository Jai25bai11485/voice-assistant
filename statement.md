# Project Statement

## Problem Statement
Most people interact with their computer using a keyboard and mouse for small,
repetitive tasks — checking the time, opening an app, searching something online,
or setting a timer. This is slower than simply asking out loud. This project solves
that by building a **Java-based offline voice assistant** that listens through the
microphone, understands simple spoken commands, and carries out the matching task
directly on the machine, then confirms it out loud.

## Scope
- The assistant runs fully **offline**, except for the weather feature, which needs
  internet access to reach a weather API.
- It listens continuously in the background using a dedicated thread, so it does not
  freeze while waiting for speech.
- It is a **desktop command-line Java application** — there is no graphical user
  interface in this version.
- It is built for a **single user on a single machine at a time** — it is not a
  multi-user or networked system.
- Out of scope: natural conversation, follow-up context ("do that again"), and
  understanding phrasing outside the fixed keyword patterns coded into each command.

## Target Users
- A single person who wants faster, hands-free access to common desktop tasks.
- Students or hobbyists who want to see how offline speech recognition, text-to-speech,
  and the Command design pattern come together in a real, working Java program.

## High-Level Features
- Tell the current time and date
- Lock or shut down the computer
- Open common desktop apps (calculator, notepad, terminal, code editor)
- Search Google, YouTube, or Wikipedia through the default browser
- Play, pause, or skip local MP3 tracks
- Set a countdown timer or reminder that beeps and speaks when it finishes
- Fetch a one-line weather report
- Tell a random pre-written joke
