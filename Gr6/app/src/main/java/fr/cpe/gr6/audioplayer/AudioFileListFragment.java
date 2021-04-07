package fr.cpe.gr6.audioplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.*;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.*;

import fr.cpe.gr6.audioplayer.databinding.AudioFileListFragmentBinding;

public class AudioFileListFragment extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AudioFileListFragmentBinding binding = DataBindingUtil.inflate(inflater,R.layout.audio_file_list_fragment,container,false);
        Context context= binding.getRoot().getContext();
        if(context instanceof MainActivity){
            // La carte SD
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            //chemin du fichier, ID, titre, artist
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ALBUM_ID
            };

            Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
            String pathexemp="";
            String nameexemp="";
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int audioTitle = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                        int audioartist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                        int audioduration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                        int audiodata = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        int audioalbum = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                        int audioalbumid = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                        int song_id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                        AudioFile info = new AudioFile();
                        info.setFilePath(cursor.getString(audiodata));
                        pathexemp=cursor.getString(audiodata);
                        nameexemp=cursor.getString(audioTitle);
                        info.setTitle(cursor.getString(audioTitle));
                        info.setDuration((cursor.getInt(audioduration)));
                        info.setArtist(cursor.getString(audioartist));
                        info.setAlbum(cursor.getString(audioalbum));
                        MainActivity.fakeList.add(info);
                    } while (cursor.moveToNext());
                }
            }
            binding.audioFileList.setAdapter(new AudioFileListAdapter(MainActivity.fakeList));
            binding.audioFileList.setLayoutManager(new LinearLayoutManager( binding.getRoot().getContext()));
        }else if(context instanceof MainActivity_lastfm){//les genres
            //Toast.makeText(context, "Veuillez patienter le temps du telechargement!", Toast.LENGTH_LONG).show();
            try {
                String url="http://ws.audioscrobbler.com/2.0/?method=chart.gettoptags&api_key=22af09386e7cdce7665e1d05dfa67161&format=json&limit=20";
                new FilesFromWebserviceTask().execute(url, context,"genre").get();
                binding.audioFileList.setAdapter(new AudioFileListAdapter(MainActivity_lastfm.fakeList));
                binding.audioFileList.setLayoutManager(new LinearLayoutManager( binding.getRoot().getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(context instanceof MainActivity_lastfm_album){//album
            try {
                String url="http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist=cher&api_key=22af09386e7cdce7665e1d05dfa67161&format=json&limit=20";
                new FilesFromWebserviceTask().execute(url, context,"album").get();
                binding.audioFileList.setAdapter(new AudioFileListAdapter(MainActivity_lastfm_album.fakeList));
                binding.audioFileList.setLayoutManager(new LinearLayoutManager( binding.getRoot().getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(context instanceof MainActivity_lastfm_artist){//artist
            try {
                String url="http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=22af09386e7cdce7665e1d05dfa67161&format=json&limit=20";
                new FilesFromWebserviceTask().execute(url, context,"artist").get();
                binding.audioFileList.setAdapter(new AudioFileListAdapter(MainActivity_lastfm_artist.fakeList));
                binding.audioFileList.setLayoutManager(new LinearLayoutManager( binding.getRoot().getContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return binding.getRoot();
    }

}