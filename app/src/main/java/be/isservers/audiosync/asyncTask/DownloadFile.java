package be.isservers.audiosync.asyncTask;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import be.isservers.audiosync.R;
import be.isservers.audiosync.activity.HomeActivity;
import be.isservers.audiosync.convert.Music;

import static be.isservers.audiosync.activity.HomeActivity.CHANNEL_2_ID;

public class DownloadFile extends AsyncTask<String,Integer,String> {

    private AppCompatActivity parent;
    private List<Music> listingMusic;

    private int progressCurrent = 1;
    private int progressMax = 0;

    public DownloadFile(AppCompatActivity parent,List<Music> listingMusic) {
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

        ((HomeActivity) parent).notificationManager.notify(2,notification.build());

        new Thread(() -> {
            SystemClock.sleep(500);
            while (progressCurrent < progressMax) {
                String filename = listingMusic.get(progressCurrent).getName();

                notification.setProgress(progressMax,progressCurrent,false)
                    .setContentText("(" + progressCurrent + "/" + progressMax + ")   " + filename);
                ((HomeActivity) parent).notificationManager.notify(2,notification.build());
                SystemClock.sleep(1000);
            }
            notification.setContentText("Téléchargement terminé")
                    .setProgress(0,0,false)
                    .setOngoing(false);
            ((HomeActivity) parent).notificationManager.notify(2,notification.build());
        }).start();
    }

    @Override
    protected String doInBackground(String... strings) {

        for (Music music : listingMusic) {
            try {
                publishProgress(1);
                String filename = music.getName().replace(" ","_");

                URLConnection conn = new URL("http://audiosync.isservers.be/music/"+ filename + ".mp3").openConnection();
                InputStream is = conn.getInputStream();

                OutputStream outputStream = new FileOutputStream(new File(Music.PathToMusic + "/"+filename+".mp3"));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(parent,"Téléchargement terminé",Toast.LENGTH_SHORT).show();

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressCurrent++;
    }
}
