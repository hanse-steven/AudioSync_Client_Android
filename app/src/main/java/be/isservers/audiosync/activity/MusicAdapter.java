package be.isservers.audiosync.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.Music;

public class MusicAdapter extends ArrayAdapter<Music> {
    public MusicAdapter(@NonNull Context context, @NonNull List<Music> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);

        TextView tvTitle = convertView.findViewById(R.id.tv_item_song_title);
        TextView tvArtist = convertView.findViewById(R.id.tv_item_song_artiste);

        Music music = getItem(position);
        tvTitle.setText(music.getName().replace("_"," ").replace(".mp3",""));
        tvArtist.setText(music.getHash());

        return convertView;
    }
}
