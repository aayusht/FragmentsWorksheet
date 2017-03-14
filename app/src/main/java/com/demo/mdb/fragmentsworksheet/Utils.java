package com.demo.mdb.fragmentsworksheet;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hp on 3/14/2017.
 */

public class Utils {
    static class GetJSONfromURL implements Callable<JSONObject/*Return type*/> {
        String urlString;

        //can't pass in a string to call method, so we'll pass it in the constructor
        public GetJSONfromURL(String urlString) {
            this.urlString = urlString;
        }

        //now we can use that urlString to run this block of code and run the JSONObject
        @Override
        public JSONObject call() throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = convertStreamToString(in);
            return new JSONObject(response);
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    static JSONObject getJSON(String urlString) throws Exception {
        //set number of threads you want to process the callables you will submit. Since we only
        // want to submit one, there's no point suing more than one thread
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<JSONObject> result = executor.submit(new GetJSONfromURL(urlString));
        return result.get(); //takes unknown time to execute, always call in AsyncTask's doInBackground
    }

    static class NotifyDataSetChanged implements Runnable {
        ListAdapter adapter;

        public NotifyDataSetChanged(ListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    }

    static void notifyDataSetChanged(Activity activity, ListAdapter adapter) {
        //an example of a case in which you'd want a Runnable, now you can run things on the UI
        // thread if necessary. notifyDataSetChanged() can't normally be called off the UI Thread!
        activity.runOnUiThread(new NotifyDataSetChanged(adapter));
    }
}
