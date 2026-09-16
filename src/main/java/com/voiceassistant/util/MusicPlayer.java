package com.voiceassistant.util;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

// keeps track of a folder full of mp3s and plays them one at a time
// JLayer does not support true pause, so "pause" here just stops playback at the current track
public class MusicPlayer {

    private final List<File> tracks = new ArrayList<>();
    private int currentIndex = 0;
    private AdvancedPlayer player;
    private Thread playerThread;

    public MusicPlayer(String musicFolderPath) {
        File folder = new File(musicFolderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
        if (files != null) {
            for (File f : files) tracks.add(f);
        }
    }

    public boolean hasTracks() {
        return !tracks.isEmpty();
    }

    public void play() {
        if (!hasTracks()) return;
        stop(); // make sure nothing else is playing at the same time

        // playback has to run on its own thread or it would freezes the whole assistant
        playerThread = new Thread(() -> {
            try {
                File track = tracks.get(currentIndex);
                FileInputStream fis = new FileInputStream(track);
                player = new AdvancedPlayer(new BufferedInputStream(fis));
                player.play();
            } catch (Exception e) {
                System.out.println("Playback error: " + e.getMessage());
            }
        });
        playerThread.start();
    }

    public void pause() {
        stop();
    }

    public void skip() {
        currentIndex = (currentIndex + 1) % tracks.size();
        play();
    }

    private void stop() {
        if (player != null) {
            player.close();
        }
    }

    public String currentTrackName() {
        if (!hasTracks()) return "no tracks found";
        return tracks.get(currentIndex).getName();
    }
}
