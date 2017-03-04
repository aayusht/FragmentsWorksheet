package com.demo.mdb.fragmentsworksheet;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hp on 3/3/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    ArrayList<String> moveNames = new ArrayList<>();
    ArrayList<String> moveDeets = new ArrayList<>();

    public ListAdapter(final String pokemon) {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                try {
                    /* TODO Question 10
                        initialize a JSON object for the pokemon the same as Question 3
                        lol this should be in a Utils class but that would require a callable rip
                     */
                    JSONArray jsonMoves = /*TODO Question 11 get the moves array*/;
                    int len = jsonMoves.length();
                    for (int i=0;i<len;i++){
                        String urlString = /*TODO Question 12 get the url String from the move at index i*/;
                        URL url2 = new URL(urlString.replace("\\", ""));
                        Log.e("url", url2.toString());
                        /* TODO Question 12
                            use http get to get the JSONObject moveData containing the data for each move
                         */
                        moveNames.add(/*TODO Question 13 get the first name from moveData's name array (the English one)*/);
                        moveDeets.add("Power: " + moveData.getString("power") + " / Accuracy: " + moveData.getString("accuracy"));
                        publishProgress();
                    }
                    if (moveNames.size()==0) {Log.e("rip", "rrip");}
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("bad", "url");
                } catch (ProtocolException p) {
                    p.printStackTrace();
                    Log.e("bad", "protocol");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("bad", "io");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("bad", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return moveNames.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.moveNameTextView.setText(moveNames.get(position));
        holder.moveDetailsTextView.setText(moveDeets.get(position).replace("null", "-"));
    }

    //TODO lol why you looking here i found this on stackoverflow
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView moveNameTextView;
        TextView moveDetailsTextView;

        public CustomViewHolder (View view) {
            super(view);
            this.moveNameTextView = (TextView) view.findViewById(R.id.moveView);
            this.moveDetailsTextView = (TextView) view.findViewById(R.id.moveDetailsView);
        }
    }
}
