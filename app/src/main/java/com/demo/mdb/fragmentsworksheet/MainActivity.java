package com.demo.mdb.fragmentsworksheet;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static String poo = "bulbasaur";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                poo = query;
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            final View rootView;
            switch(position) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    ((TextView) rootView.findViewById(R.id.section_label)).setText(poo);
                    new AsyncTask<Void, Void, JSONObject>() {
                        protected JSONObject doInBackground(Void... voids) {
                            try {
                                URL url = new URL("http://pokeapi.co/api/v2/pokemon/" + poo.toLowerCase());
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                InputStream in = new BufferedInputStream(conn.getInputStream());
                                String response = ListAdapter.convertStreamToString(in);
                                JSONObject json = new JSONObject(response);
                                return json;
                            }
                            catch (Exception e) {return null;}
                        }

                        protected void onPostExecute(JSONObject json) {
                            try {
                                /*((ImageView) rootView.findViewById(R.id.imageView)).setImageBitmap(Glide.
                                        with(getActivity()).
                                        load(json.getJSONObject("sprites").getString("front_default").replace("\\", "")).
                                        asBitmap().
                                        into(100, 100). // Width and height
                                        get());*/
                                String types = json.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");
                                if (json.getJSONArray("types").length() > 1) {
                                    types = json.getJSONArray("types").getJSONObject(1).getJSONObject("type").getString("name") + " / " + types;
                                }
                                JSONArray statsArr = json.getJSONArray("stats");
                                String stats = "";
                                for (int i = statsArr.length() - 1; i >= 0; i--) {
                                    stats += statsArr.getJSONObject(i).getJSONObject("stat").getString("name");
                                    stats += ": " + statsArr.getJSONObject(i).getInt("base_stat") + "\n";
                                }
                                ((TextView) rootView.findViewById(R.id.textView)).setText(types);
                                ((TextView) rootView.findViewById(R.id.textView2)).setText(stats);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                    new AsyncTask<Void, Void, Bitmap>() {
                        protected Bitmap doInBackground(Void... voids) {
                            try {
                                URL url = new URL("http://pokeapi.co/api/v2/pokemon/" + poo.toLowerCase());
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                InputStream in = new BufferedInputStream(conn.getInputStream());
                                String response = ListAdapter.convertStreamToString(in);
                                JSONObject json = new JSONObject(response);
                                return Glide.
                                        with(getActivity()).
                                        load(json.getJSONObject("sprites").getString("front_default").replace("\\", "")).
                                        asBitmap().
                                        into(100, 100). // Width and height
                                        get();
                            }
                            catch (Exception e) {return null;}
                        }

                        protected void onPostExecute(Bitmap bitmap) {
                            try {
                                ((ImageView) rootView.findViewById(R.id.imageView)).setImageBitmap(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_moves, container, false);
                    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    ListAdapter adapter = new ListAdapter(poo);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    return rootView;
            }
            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "0";
                case 1:
                    return "1";
            }
            return null;
        }
    }
}
