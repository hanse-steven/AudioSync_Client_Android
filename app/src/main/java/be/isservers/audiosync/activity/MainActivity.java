package be.isservers.audiosync.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import be.isservers.audiosync.authorization.AutorizationRequest;
import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.ListingMusic;
import be.isservers.audiosync.convert.Music;
import be.isservers.audiosync.asyncTask.HttpRequest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Music> musicTab = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutorizationRequest.requestStoragePermission(this);

        File directory = new File(Music.PathToMusic);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    musicTab.add(new Music(file.getName()));
            }
            Collections.sort(musicTab);
            ArrayAdapter<Music> arrayMusic = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicTab);
            ((ListView) findViewById(R.id.lv_list)).setAdapter(arrayMusic);
        }
    }

    public void Reload_Button_web_Click(View view) {
        AutorizationRequest.requestStoragePermission(this);
        HttpRequest task = new HttpRequest();
        try {
            Log.d("JSON ENVOYE", ConvertListToStringList());
            String result = task.execute("http://audiosync.isservers.be/httpRequest/script.php?v=" + ConvertListToStringList()).get();

            Gson gson = new Gson();
            final ListingMusic listingMusic = gson.fromJson(result, ListingMusic.class);
            Toast.makeText(this,listingMusic.getSizeToDownload(),Toast.LENGTH_SHORT).show();

            musicTab = new ArrayList<>();

            File directory = new File(Music.PathToMusic);
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile())
                        musicTab.add(new Music(file.getName()));
                }
                for (Music file : listingMusic.getToDownload()){
                    musicTab.add(new Music(file.getHash(),file.getName()));
                }
                Collections.sort(musicTab);
                ArrayAdapter<Music> arrayMusic = new ArrayAdapter<Music>(this, android.R.layout.simple_list_item_1, musicTab){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position,convertView,parent);
                        TextView text = view.findViewById(android.R.id.text1);

                        text.setText(text.getText().toString().replaceFirst("-","\n"));

                        String buffer = text.getText().toString();
                        buffer = buffer.substring(buffer.indexOf('\n') + 1);

                        if(listingMusic.searchMusic(ListingMusic.TO_KEEP,buffer))
                            text.setTextColor(Color.GREEN);
                        else if (listingMusic.searchMusic(ListingMusic.TO_DELETE,buffer))
                            text.setTextColor(Color.RED);
                        else text.setTextColor(Color.YELLOW);

                        return view;
                    }
                };
                ((ListView)findViewById(R.id.lv_list)).setAdapter(arrayMusic);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Reload_Button_local_Click(View view) {
        //new DownloadFile().execute();

        Intent listingmusic = new Intent(MainActivity.this, listingMusicActivity.class);
        listingmusic.putExtra("tab",musicTab);
        startActivity(listingmusic);
    }

    private String ConvertListToStringList(){
        ArrayList<String> listString = new ArrayList<>();

        for (Music music : musicTab) {
            if (music.getHash() != null)
                listString.add(music.toString());
        }

        return new Gson().toJson(listString);
    }


}