package fr.cpe.gr6.audioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.*;

import java.util.*;

import fr.cpe.gr6.audioplayer.databinding.AudioFileItemBinding;

public class AudioFileListAdapter  extends
        RecyclerView.Adapter<AudioFileListAdapter.ViewHolder> {
    List<AudioFile> audioFileList;
    public AudioFileListAdapter(List<AudioFile> fileList) {
        assert fileList != null;
        audioFileList = fileList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AudioFileItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.audio_file_item, parent,false);
        Context context=binding.getRoot().getContext();
        //pour lapi last fm
        //ici on gere la redirection en ligne lorsquon clique sur un artist, un genre ou un album
        if( context instanceof MainActivity_lastfm || context instanceof MainActivity_lastfm_album || context instanceof MainActivity_lastfm_artist) {
            binding.itemAlone.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View v) {
                    Toast.makeText(binding.getRoot().getContext(), "Vous allez être rediriger!", Toast.LENGTH_SHORT).show();
                    //un exemple pour tester
                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.stackoverflow.com/"));//Mais malheureusement sa marche pas
                    //startActivity(viewIntent);
                }
            });
        }
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioFile file = audioFileList.get(position);
        holder.viewModel.setAudioFile(file);
/*
        holder.binding.title.setText(file.getTitle());
        holder.binding.artist.setText(file.getArtist());
        holder.binding.album.setText(file.getAlbum());
        holder.binding.duration.setText(file.getDurationText());
 */
    }
    @Override
    public int getItemCount() {
        return audioFileList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AudioFileItemBinding binding;
        private final AudioFileViewModel viewModel = new AudioFileViewModel();

        ViewHolder(AudioFileItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAudioFileViewModel(viewModel);
        }
    }

}
