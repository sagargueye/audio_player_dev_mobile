package fr.cpe.gr6.audioplayer;


public class Album {
    private String title;
    private String artist;
    private int year;
    private int nbaudioFile;

    public Album(String title, String artist, String album, String genre, int year, int nbaudioFile) {
        this.title=title;
        this.artist=artist;
        this.year=year;
        this.nbaudioFile=nbaudioFile;
    }

    public Album(String title) {
        this.title=title;
    }

    public Album() {}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setNbaudioFile(int nbaudioFile) {
        this.nbaudioFile = nbaudioFile;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", year=" + year +
                '}';
    }
}