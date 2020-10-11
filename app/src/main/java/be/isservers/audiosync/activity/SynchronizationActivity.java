package be.isservers.audiosync.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import be.isservers.audiosync.R;
import be.isservers.audiosync.asyncTask.DeleteFile;
import be.isservers.audiosync.asyncTask.PostRequest;
import be.isservers.audiosync.convert.ListingMusic;
import be.isservers.audiosync.convert.Music;
import be.isservers.audiosync.asyncTask.DownloadFile;

import static be.isservers.audiosync.authorization.AutorizationRequest.requestStoragePermission;

public class SynchronizationActivity extends AppCompatActivity {


    private ListingMusic listingMusic;
    private boolean animationActivate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);

        requestStoragePermission(this);
        hideAllBeforeSynchronisation();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Synchronisation");
    }

    private void hideAllBeforeSynchronisation(){
        findViewById(R.id.ll_tokeep).setVisibility(View.INVISIBLE);
        findViewById(R.id.ll_todelete).setVisibility(View.INVISIBLE);
        findViewById(R.id.ll_todownload).setVisibility(View.INVISIBLE);
        findViewById(R.id.view_seperator).setVisibility(View.INVISIBLE);

        TextView tv_toKeep = findViewById(R.id.tv_tokeep_name);
        tv_toKeep.setText(R.string.TEXT_TV_TOKEEP_NAME);

        Button b_download = findViewById(R.id.b_download);
        b_download.setVisibility(View.INVISIBLE);
        b_download.setText(R.string.BUTTON_DOWNLOAD);
    }

    public void b_sync_Click(View view) {
        launchAnimationButton();

        requestStoragePermission(this);
        hideAllBeforeSynchronisation();

        ArrayList<Music> musicTab = new ArrayList<>();
        File directory = new File(Music.PathToMusic);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    musicTab.add(new Music(file.getName()));
            }
            Collections.sort(musicTab);
        }

        PostRequest task = new PostRequest();
        Gson gson = new Gson();
        try {
            String result = task.execute("http://isservers.be:9090/synchronization",gson.toJson(musicTab)).get();


            listingMusic = gson.fromJson(result, ListingMusic.class);

            Button button_tokeep = findViewById(R.id.button_tokeep_value);
            button_tokeep.setText(String.format(Locale.FRENCH,"%d",listingMusic.getToKeep().size()));
            if (listingMusic.getToKeep().size() > 0) findViewById(R.id.ll_tokeep).setVisibility(View.VISIBLE);

            Button button_todelete = findViewById(R.id.button_todelete_value);
            button_todelete.setText(String.format(Locale.FRENCH,"%d",listingMusic.getToDelete().size()));
            if (listingMusic.getToDelete().size() > 0) findViewById(R.id.ll_todelete).setVisibility(View.VISIBLE);

            Button button_todownload = findViewById(R.id.button_todownload_value);
            button_todownload.setText(String.format(Locale.FRENCH,"%d",listingMusic.getToDownload().size()));
            if (listingMusic.getToDownload().size() > 0) findViewById(R.id.ll_todownload).setVisibility(View.VISIBLE);

            findViewById(R.id.view_seperator).setVisibility(View.VISIBLE);

            Button b_download = findViewById(R.id.b_download);
            b_download.setText(String.format(Locale.FRENCH,"%s  (%s)", getResources().getString(R.string.BUTTON_DOWNLOAD), listingMusic.getSizeToDownload()));

            if (listingMusic.getToDelete().size() > 0 || listingMusic.getToDownload().size() > 0){
                findViewById(R.id.b_download).setVisibility(View.VISIBLE);
            }
            else{
                TextView tv_toKeep = findViewById(R.id.tv_tokeep_name);
                tv_toKeep.setText("Synchronisation à jour");

                findViewById(R.id.ll_tokeep).setVisibility(View.VISIBLE);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        animationActivate = false;
    }

    private void launchListingActivity(List<Music> list,String title){
        Intent activiy = new Intent(SynchronizationActivity.this, ListingMusicActivity.class);
        activiy.putExtra("isHome",false);
        activiy.putExtra("title",title);
        activiy.putExtra("data", (Serializable) list);
        startActivity(activiy);
    }

    public void b_tokeep_look_Click(View view) {
        launchListingActivity(listingMusic.getToKeep(),"A garder");
    }

    public void b_todelete_look_Click(View view) {
        launchListingActivity(listingMusic.getToDelete(),"A supprimer");
    }

    public void b_todownload_look_Click(View view) {
        launchListingActivity(listingMusic.getToDownload(),"A télécharger");
    }

    public void b_download_Click(View view) {
        new DownloadFile(this,listingMusic.getToDownload()).execute();
        new DeleteFile(this,listingMusic.getToDelete()).execute();
    }

    public void launchAnimationButton() {
        new Thread(() -> {
            animationActivate = true;
            int degrees = 0;
            FloatingActionButton fab = findViewById(R.id.floatingActionButton);
            while (animationActivate){
                try {
                    Thread.sleep(50);

                    int finalDegrees = degrees;
                    runOnUiThread(() -> fab.setRotation(finalDegrees));

                    degrees += 10;
                    if (degrees == 360) degrees = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (fab.getRotation() != 180 && fab.getRotation() != 360){
                try {
                    Thread.sleep(50);

                    int finalDegrees = degrees;
                    runOnUiThread(() -> fab.setRotation(finalDegrees));

                    degrees += 10;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}