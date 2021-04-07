package fr.cpe.gr6.audioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.io.IOException;


import fr.cpe.gr6.audioplayer.databinding.AudioFilePlayFragmentBinding;

public class AudioFilePlayFragment extends Fragment{
    private final MediaPlayer mp = new MediaPlayer();
    private AudioFilePlayFragmentBinding binding;
    private int media_length_when_pause;
    private boolean isOnPause=false;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.audio_file_play_fragment, container, false);
        Context context = binding.getRoot().getContext();

        //play / pause
        binding.play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                // la music est en cour de lecteur, donc on pause
                if(mp.isPlaying()){
                    doPause();
                }
                //on etait sur pause on replay la zik
                else if(isOnPause) {
                    doReplayAfterPause();
                }
                //la music nest pas encore lancé
                else{
                    int indice;
                    if (binding.play.getTag() == null) {
                        indice = 0;
                    } else {
                        indice = (int) binding.play.getTag();
                    }
                    AudioFile audio = MainActivity.fakeList.get(indice);
                    binding.play.setTag(indice);
                    audioPlayer(audio.getFilePath(), audio.getTitle());
                }
            }
        });

        //bouton suivant
        binding.suivant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int indice;
                if (binding.play.getTag() == null){
                    indice =0;
                }else{
                    indice =(int) binding.play.getTag();
                    //si on arrive à la fin de la liste ( de music)
                    //on repart à 0 cest a dire au debut de la liste
                    if( ( (int)binding.play.getTag() +1 ) == MainActivity.fakeList.size()){//on ajoute 1 car la liste commence par 0et non 1
                        indice =0;
                    }
                    //sinon on lit simplement le suivant sur la liste
                    else{
                        indice = indice +1;
                    }
                }
                AudioFile audio = MainActivity.fakeList.get(indice);
                binding.play.setTag(indice);
                audioPlayer(audio.getFilePath(),audio.getTitle());
            }
        });

        //precedent
        binding.precedent.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 int indice;
                 if (binding.play.getTag() == null){
                     indice =0;
                 }else{
                     indice =(int) binding.play.getTag();
                     //si on arrive à la fin de la liste ( de music)
                     //on repart à 0 cest a dire au debut de la liste
                     if(  (int)binding.play.getTag()  == 0 ){
                         indice = MainActivity.fakeList.size() -1 ;
                     }
                     //sinon on lit simplement le suivant sur la liste
                     else{
                         indice = indice - 1;
                     }
                 }
                 AudioFile audio = MainActivity.fakeList.get(indice);
                 binding.play.setTag(indice);
                 audioPlayer(audio.getFilePath(),audio.getTitle());
             }
        });
        //end
        return binding.getRoot();
    }


    @SuppressLint("SetTextI18n")
    public void killmp() {
        if (this.mp != null) {
            this.mp.stop();
            binding.play.setText("Play");
            this.isOnPause = false;
            mp.reset();
        }
    }

    @SuppressLint("SetTextI18n")
    private void stopPlaying() {
        if (this.mp != null) {
            this.mp.stop();
            binding.play.setText("Play");
            this.isOnPause = false;
        }
    }

    @SuppressLint("SetTextI18n")
    private void doPause() {
        if (this.mp != null) {
            this.mp.pause();
            this.media_length_when_pause=mp.getCurrentPosition();
            int indice =(int) binding.play.getTag();
            binding.play.setText("Play");
            this.isOnPause = true;
        }
    }

    @SuppressLint("SetTextI18n")
    private void doReplayAfterPause(){
        if (this.mp != null) {
            mp.seekTo(media_length_when_pause);
            mp.start();
            binding.play.setText("Pause");
        }
    }

    @SuppressLint("SetTextI18n")
    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        mp.reset();
        try {
            this.mp.setDataSource(path);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mp.start();
        binding.description.setText(fileName);
        binding.play.setText("Pause");

        //à la fin de lecture, lire la zik suivante dans la liste
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                //next audio file
                stopPlaying();
                int indice;
                if (binding.play.getTag() == null){
                    indice =0;
                }else{
                    indice =(int) binding.play.getTag();
                    //si on arrive à la fin de la liste ( de music)
                    //on repart à 0 cest a dire au debut de la liste
                    if( ( (int)binding.play.getTag()+1 ) == MainActivity.fakeList.size() ){//on ajoute 1 car la liste commence par 0et non 1
                        indice =0;
                    }
                    //sinon on lit simplement le suivant sur la liste
                    else{
                        indice = indice +1;
                    }
                }
                AudioFile audio = MainActivity.fakeList.get(indice);
                binding.play.setTag(indice);
                audioPlayer(audio.getFilePath(),audio.getTitle());
            }
        });
    }

}
