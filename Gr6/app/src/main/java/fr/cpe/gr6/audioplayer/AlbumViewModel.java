package fr.cpe.gr6.audioplayer;

import androidx.databinding.*;

public class AlbumViewModel extends BaseObservable {
    private Album album = new Album();

    public void setAlbum (Album album ) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }

    @Bindable
    public String getTitle() {
        return album.getTitle();
    }
}