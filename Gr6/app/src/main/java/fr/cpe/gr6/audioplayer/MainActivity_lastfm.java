package fr.cpe.gr6.audioplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.gr6.audioplayer.databinding.ActivityMainLastfmBinding;

public class MainActivity_lastfm extends AppCompatActivity {
    private ActivityMainLastfmBinding binding;
    private static final int STORAGE_PERMISSION_CODE = 1;
    public static List<AudioFile> fakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStartup();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_lastfm);

        //Lorsquon veut retour au main activity
        binding.returnbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                Intent mainActivity = new Intent(binding.getRoot().getContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });

        //Liste des artistes
        binding.artistes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                binding.artistes.setText("Veuillez patienter!");
                binding.artistes.setBackgroundColor(0x63009688);
                Toast.makeText(binding.getRoot().getContext(), "Veuillez patienter le temps du telechargement!", Toast.LENGTH_SHORT).show();
                Intent mainActivity_lastfm_artist = new Intent(binding.getRoot().getContext(), MainActivity_lastfm_artist.class);
                startActivity(mainActivity_lastfm_artist);
            }
        });

        //Liste des albums
        binding.albums.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                binding.albums.setText("Veuillez patienter!");
                binding.albums.setBackgroundColor(0x63009688);
                Toast.makeText(binding.getRoot().getContext(), "Veuillez patienter le temps du telechargement!", Toast.LENGTH_SHORT).show();
                Intent mainActivity_lastfm_artist = new Intent(binding.getRoot().getContext(), MainActivity_lastfm_album.class);
                startActivity(mainActivity_lastfm_artist);
            }
        });

        //permission pour avoir acces au external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MainActivity_lastfm.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    public void showStartup() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AudioFileListFragment fragment = new AudioFileListFragment();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }
}