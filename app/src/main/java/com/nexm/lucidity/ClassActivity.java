package com.nexm.lucidity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nexm.lucidity.fragments.VideoFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ClassActivity extends AppCompatActivity implements
            VideoFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String videoID = getIntent().getStringExtra("VIDEO_ID");
        String videoDuration = getIntent().getStringExtra("VIDEO_DURATION");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.class_framelayout, VideoFragment.newInstance(videoID,videoDuration,"LIVE"))
                .commit();

    }

    @Override
    public void onFragmentInteraction() {

    }
}
