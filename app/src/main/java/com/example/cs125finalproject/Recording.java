package com.example.cs125finalproject;

public class Recording {
    static String uri, filename;
    static boolean isPlaying = false;


    public Recording(String setUri, String setFilename, boolean setIsPlaying) {
        this.uri = setUri;
        this.filename = setFilename;
        this.isPlaying = setIsPlaying;
    }
    public static String getUri() {
        return uri;
    }
    public static String getFilename() {
        return filename;
    }
    public boolean getIsPlaying() {
        return isPlaying;
    }
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }
}
