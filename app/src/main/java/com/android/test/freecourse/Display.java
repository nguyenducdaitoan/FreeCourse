package com.android.test.freecourse;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.test.freecourse.model.Course;
import com.android.test.freecourse.adapter.MyRecyclerAdapter;
import com.android.test.freecourse.listener.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Display
 * <p></p>
 * @author ToanNDD
 * @version 1.0.0
 * created 2016/29/05
 * company bonsey
 */
public class Display extends Activity {

    private final String TAG = "RecyclerViewCourseList";

    private List<Course> courseList = new ArrayList<Course>();

    private List<Course> courseListDisplay = new ArrayList<Course>();

    private RecyclerView mRecyclerView;

    private MyRecyclerAdapter adapter;

    private ProgressBar progressBar;

    private SwipeRefreshLayout swipeContainer;

    protected Handler handler;

    private int numberItemOfSroll = 3;

    //Downloading json data from below url
    private final String url = "https://api.myjson.com/bins/1j38k";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Allow activity to show indeterminate progressbar
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.display);

        //Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Your code to refresh the list here.
                //Make sure you call swipeContainer.setRefreshing(false)
                //once the network request has completed successfully.
                fetchTimelineAsync();
            }
        });

        //Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        //Initialize recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new AsyncHttpTask().execute(url);

        handler = new Handler();

    }

    public void fetchTimelineAsync() {
        courseList.clear();
        courseListDisplay.clear();
        adapter.clear();
        mRecyclerView.setAdapter(adapter);
        new AsyncHttpTask().execute(url);
        swipeContainer.setRefreshing(false);
        Log.e(TAG, "Refresh JSON data!");
    }

    public enum FetchStatus {
        STATUS_FAILED(0), STATUS_SUCCESS(1), STATUS_OK(200);
        private final int value;

        FetchStatus(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            HttpURLConnection urlConnection;

            try {

                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                //for Get request
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                //200 represents HTTP OK
                if (statusCode ==  FetchStatus.STATUS_OK.getValue()) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = FetchStatus.STATUS_SUCCESS.getValue(); //Successful
                } else {
                    result = FetchStatus.STATUS_FAILED.getValue(); //Failed to fetch data
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {

            setProgressBarIndeterminateVisibility(false);

            //ownload complete. Lets update UI
            progressBar.setVisibility(View.GONE);

            int i;

            if (result == 1) {

                for (i = 1 ;  i < numberItemOfSroll ;i++) {
                    courseListDisplay.add(courseList.get(i));
                }
                adapter = new MyRecyclerAdapter(Display.this, courseListDisplay, mRecyclerView);
                mRecyclerView.setAdapter(adapter);
            } else {
                Log.e(TAG, "Failed to fetch data!");
                Toast temp = Toast.makeText(Display.this , "Failed to fetch data please check your connection and try to login again!" , Toast.LENGTH_SHORT);
                temp.show();
                return;
            }

            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           //Log.e(TAG, "onLoadMore recyleview called!");
                            int start = courseListDisplay.size();
                            int end = start + numberItemOfSroll;
                            int i;
                            for (i = start + 1; i <= end; i++) {
                                if(i < courseList.size() - 1) {
                                    courseListDisplay.add(courseList.get(i));
                                    adapter.notifyItemInserted(courseListDisplay.size());
                                }
                            }
                            adapter.setLoaded();
                            //or you can add all at once but do not forget to call notifyDataSetChanged();
                        }
                    }, 100);
                }
            });
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray courses = response.optJSONArray("course");

            //Initialize array if null
            if (null == courseList) {
                courseList = new ArrayList<Course>();
            }
            int i;
            //int j;
            for (i = 0; i < courses.length(); i++) {
                JSONObject courseObj = courses.optJSONObject(i);
                Course item = new Course();
                item.setTitle(courseObj.getString("id"));
                item.setDescription(courseObj.getString("name"));
                //JSONArray images = courseObj.getJSONArray("image");
                //for (j = 0; j < images.length(); j++) {
                    //JSONObject image = images.optJSONObject(i);
                    //item.setThumbnail(image.getString("url"));
                //}
                courseList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
