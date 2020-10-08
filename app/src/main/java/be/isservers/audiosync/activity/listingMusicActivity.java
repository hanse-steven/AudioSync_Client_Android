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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class listingMusicActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_music);

        Intent intent = getIntent();
        ArrayList<Music> musicTab = (ArrayList<Music>) intent.getSerializableExtra("data");
        if (musicTab == null) musicTab = new ArrayList<>();

        ArrayAdapter<Music> arrayMusic = new ArrayAdapter<Music>(this, android.R.layout.simple_list_item_1, musicTab){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position,convertView,parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setText(substring_separator(text.getText().toString(),"-"));
                return view;
            }
        };
        ((ListView) findViewById(R.id.lv_list)).setAdapter(arrayMusic);
    }

    public static String substring_separator(String buffer, String charac){
        int pos = buffer.lastIndexOf(charac);
        return buffer.substring(0,pos);
    }
}