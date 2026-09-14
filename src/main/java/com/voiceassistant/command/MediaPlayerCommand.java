package com.voiceassistant.command;

import com.voiceassistant.util.MusicPlayer;

public class MediaPlayerCommand implements Command {

    private final MusicPlayer musicPlayer;

    // pass in the folder where your mp3s live, e.g. "C:/Users/you/Music"
    public MediaPlayerCommand(String musicFolderPath) {
        this.musicPlayer = new MusicPlayer(musicFolderPath);
    }

    @Override
    public boolean matches(String spokenText) {
        return spokenText.contains("play music") || spokenText.contains("pause music")
                || spokenText.contains("skip song") || spokenText.contains("next song");
    }

    @Override
    public String execute(String spokenText) {
        if (!musicPlayer.hasTracks()) {
            return "I could not find any mp3 files in your music folder";
        }
        if (spokenText.contains("pause")) {
            musicPlayer.pause();
            return "Music paused";
        } else if (spokenText.contains("skip") || spokenText.contains("next")) {
            musicPlayer.skip();
            return "Skipping to " + musicPlayer.currentTrackName();
        } else {
            musicPlayer.play();
            return "Playing " + musicPlayer.currentTrackName();
        }
    }
}
