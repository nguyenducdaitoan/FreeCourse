package com.android.test.freecourse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by TOAN on 5/18/2016.
 */
public class Detail extends Activity {
    // JSON node keys
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    //private static final String TAG_THUMBNAIL = "thumbnail";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String title = in.getStringExtra(TAG_TITLE);
        String description = in.getStringExtra(TAG_DESCRIPTION);
        //String thumbnail = in.getStringExtra(TAG_THUMBNAIL);

        // Displaying all values on the screen
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        TextView lblDescription = (TextView) findViewById(R.id.description_label);
        TextView lblThumbnail = (TextView) findViewById(R.id.thumbnail_label);

        lblTitle.setText(title);
        lblDescription.setText(description);
        //lblThumbnail.setText(thumbnail);
    }
}
