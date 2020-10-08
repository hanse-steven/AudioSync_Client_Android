package be.isservers.audiosync.asyncTask;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest extends AsyncTask<String, Integer, String> {


    public HttpRequest() {}

    @Override
    protected String doInBackground(String... strings) {
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        URL url;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

            result = readStream(in); // Read stream
        } catch (IOException e) { e.printStackTrace(); } finally { if (urlConnection != null)
            urlConnection.disconnect();  }

        return result; // returns the result
        //return "{\"ToKeep\":[],\"ToDelete\":[],\"ToDownload\":[],\"SizeToDownload\":\"0 mb\"}";
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}