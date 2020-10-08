package be.isservers.audiosync.asyncTask;

import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.util.List;

import be.isservers.audiosync.R;
import be.isservers.audiosync.activity.homeActivity;
import be.isservers.audiosync.convert.Music;

import static be.isservers.audiosync.activity.homeActivity.CHANNEL_2_ID;

public class DeleteFile extends AsyncTask<String,Integer,String> {

    private AppCompatActivity parent;
    private List<Music> listingMusic;

    private int progressCurrent = 0;
    private int progressMax = 0;

    public DeleteFile(AppCompatActivity parent,List<Music> listingMusic) {
        this.parent = parent;
        this.listingMusic = listingMusic;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressMax = listingMusic.size();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(parent, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("AudioSync")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(progressMax,progressCurrent,false);

        ((homeActivity) parent).notificationManager.notify(2,notification.build());

        new Thread(() -> {
            SystemClock.sleep(500);
            while (progressCurrent < progressMax) {
                String filename = listingMusic.get(progressCurrent).getName();
                if (filename.length() > 43)
                    filename = filename.substring(0,42 - ("(" + progressCurrent + "/" + progressMax + ")").length());


                notification.setProgress(progressMax,progressCurrent,false)
                        .setContentText(filename + "  (" + progressCurrent + "/" + progressMax + ")");
                ((homeActivity) parent).notificationManager.notify(2,notification.build());
                SystemClock.sleep(1000);
            }
            notification.setContentText("Suppresion terminée")
                    .setProgress(0,0,false)
                    .setOngoing(false);
            ((homeActivity) parent).notificationManager.notify(2,notification.build());
        }).start();
    }

    @Override
    protected String doInBackground(String... strings) {
        for (Music music : listingMusic) {
            publishProgress(1);
            String filename = music.getName().replace(" ","_");
            new File(Music.PathToMusic + "/"+filename+".mp3").delete();
        }
        
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressCurrent++;
    }
}
