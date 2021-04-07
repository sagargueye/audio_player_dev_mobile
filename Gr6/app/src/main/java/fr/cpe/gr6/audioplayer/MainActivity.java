package fr.cpe.gr6.audioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.gr6.audioplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int STORAGE_PERMISSION_CODE = 1;
    public static final List<AudioFile> fakeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStartup();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }
    public void showStartup() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AudioFileListFragment fragment = new AudioFileListFragment();
        transaction.replace(R.id.fragment_container,fragment);

        AudioFilePlayFragment fragment_play = new AudioFilePlayFragment();
        transaction.replace(R.id.fragment_container_play,fragment_play);
        transaction.commit();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.lastfmbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                binding.lastfmbutton.setText("Veuillez patienter!");
                binding.lastfmbutton.setBackgroundColor(0x63009688);
                Toast.makeText(binding.getRoot().getContext(), "Veuillez patienter le temps du telechargement!", Toast.LENGTH_SHORT).show();
                fragment_play.killmp();//pour arreter la musik
                Intent mainActivity_lastfm = new Intent(binding.getRoot().getContext(), MainActivity_lastfm.class);
                startActivity(mainActivity_lastfm);
            }
        });
    }
}