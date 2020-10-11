package be.isservers.audiosync.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.Music;

public class MusicPlayer extends AppCompatActivity {

    //https://github.com/nadimgouia/MusicPlayerApp

    TextView tvTime, tvDuration, tvTitle, tvArtist;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;

    MediaPlayer musicPlayer;

    Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        music = (Music) getIntent().getSerializableExtra("music");

        tvTime = findViewById(R.id.tv_time);
        tvDuration = findViewById(R.id.tv_duration);
        tvTitle = findViewById(R.id.tv_song_title);
        tvArtist = findViewById(R.id.tv_song_artist);
        seekBarTime = findViewById(R.id.seekBarTime);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnPlay = findViewById(R.id.b_play);

        tvTitle.setText(music.getName().replace("_"," ").replace(".mp3",""));
        tvArtist.setText(music.getHash());

        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(Music.PathToMusic + "/"+ music.getName());
            musicPlayer.prepare();
            musicPlayer.setLooping(true);
            musicPlayer.seekTo(0);
            musicPlayer.setVolume(0.5f,0.5f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvDuration.setText(millisecondsToString(musicPlayer.getDuration()));

        seekBarVolume.setProgress(50);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                musicPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTime.setMax(musicPlayer.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(() -> {
            while (musicPlayer != null) {
                if (musicPlayer.isPlaying()) {
                    try {
                        final double current = musicPlayer.getCurrentPosition();
                        double duration = musicPlayer.getDuration();
                        final double position = (100.0/duration) * current;
                        final String elapsedTime = millisecondsToString((int) current);

                        runOnUiThread(() -> {
                            tvTime.setText(elapsedTime);
                            seekBarTime.setProgress((int) current);
                        });

                        Thread.sleep(250);
                    }
                    catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    public String millisecondsToString(int time){
        String elpaseTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elpaseTime = minutes + ":";
        if (seconds < 10){
            elpaseTime += "0";
        }
        elpaseTime += seconds;
        return elpaseTime;
    }

    public void b_play_music_click(View view) {
        if (musicPlayer.isPlaying()){
            musicPlayer.pause();
            btnPlay.setBackgroundResource(R.drawable.ic_play);
        }
        else {
            musicPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();

            if (musicPlayer.isPlaying()){
                musicPlayer.stop(); 
            }
        }

        return super.onOptionsItemSelected(item);
    }
}