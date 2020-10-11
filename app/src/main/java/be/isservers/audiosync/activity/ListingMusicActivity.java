package be.isservers.audiosync.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.Music;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ListingMusicActivity extends AppCompatActivity {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static NotificationManagerCompat notificationManager;

    ListView lv_list;
    ArrayList<Music> musicTab;
    boolean isHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_music);

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();

        lv_list = findViewById(R.id.lv_list);

        Intent intent = getIntent();
        isHome = intent.getBooleanExtra("isHome",true);

        if (isHome){
            getSupportActionBar().setTitle("Acceuil");

            musicTab = new ArrayList<>();
            File directory = new File(Music.PathToMusic);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile())
                        musicTab.add(new Music(file.getName()));
                }
                Collections.sort(musicTab);
            }
        }
        else {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            musicTab = (ArrayList<Music>) intent.getSerializableExtra("data");
            if (musicTab == null) musicTab = new ArrayList<>();
        }

        MusicAdapter musicAdapter = new MusicAdapter(this,musicTab);

        lv_list.setAdapter(musicAdapter);
        lv_list.setOnItemClickListener((AdapterView.OnItemClickListener) (parent, view, position, id) -> {
            if (isHome){
                Music music = musicTab.get(position);
                Intent openMusicPlayer = new Intent(ListingMusicActivity.this,MusicPlayer.class);
                openMusicPlayer.putExtra("music",(Serializable) music);
                startActivity(openMusicPlayer);
            }
            else {
                Toast.makeText(this,"Pas de lecture depuis cette endroit !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isHome){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.action_bar_support_menu,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            Intent activiy = new Intent(ListingMusicActivity.this, SynchronizationActivity.class);
            startActivity(activiy);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 1");
            channel2.setDescription("This is Channel 2");
            channel2.setDescription("This is Channel 3");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}