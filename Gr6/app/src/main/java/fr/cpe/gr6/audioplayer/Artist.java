package fr.cpe.gr6.audioplayer;

import java.util.ArrayList;

public class Artist {
    private String title;
    private ArrayList<Album> listAlbum= new ArrayList<Album> ();
    private ArrayList<AudioFile> listAudio= new ArrayList<AudioFile> ();

    public Artist(String title, ArrayList<Album> listAlbum, ArrayList<AudioFile> listAudio) {
        this.title = title;
        this.listAlbum = listAlbum;
        this.listAudio = listAudio;
    }
    public Artist(String title) {
        this.title=title;
    }

    public Artist() {}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "title='" + title + '\'' +
                '}';
    }
}