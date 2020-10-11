package be.isservers.audiosync.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.Music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ListingMusicActivity extends AppCompatActivity {

    ListView lv_list;
    ArrayList<Music> musicTab;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_music);

        lv_list = findViewById(R.id.lv_list);

        Intent intent = getIntent();
        musicTab = (ArrayList<Music>) intent.getSerializableExtra("data");
        if (musicTab == null) musicTab = new ArrayList<>();

        MusicAdapter musicAdapter = new MusicAdapter(this,musicTab);

        /*ArrayAdapter<Music> arrayMusic = new ArrayAdapter<Music>(this, android.R.layout.simple_list_item_1, musicTab){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position,convertView,parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setText(substring_separator(text.getText().toString(),"-"));
                return view;
            }
        };*/
        lv_list.setAdapter(musicAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = musicTab.get(position);
                Intent openMusicPlayer = new Intent(ListingMusicActivity.this,MusicPlayer.class);
                openMusicPlayer.putExtra("music",(Serializable) music);
                startActivity(openMusicPlayer);
            }
        });


    }

    /*public static String substring_separator(String buffer, String charac){
        int pos = buffer.lastIndexOf(charac);
        return buffer.substring(0,pos);
    }*/
}