package be.isservers.audiosync.asyncTask;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest extends AsyncTask<String, Integer, String> {


    public PostRequest() {}

    @Override
    protected String doInBackground(String... strings) {
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        String result = null;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type","applocation/json; utf-8");
            con.setRequestProperty("Accept","application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            byte[] input = strings[1].getBytes("utf-8");
            os.write(input,0,input.length);

            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                result = response.toString();
            }

        } catch (IOException e) { e.printStackTrace(); }
        return result; // returns the result
    }
}