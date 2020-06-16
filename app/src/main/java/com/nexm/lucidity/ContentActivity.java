package com.nexm.lucidity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.nexm.lucidity.fragments.NotesFragment;
import com.nexm.lucidity.fragments.TestFragment;
import com.nexm.lucidity.fragments.VideoFragment;
import com.nexm.lucidity.models.CustomViewPager;
import com.nexm.lucidity.ui.main.SectionsPagerAdapter;

public class ContentActivity extends AppCompatActivity implements
        VideoFragment.OnFragmentInteractionListener,
        NotesFragment.OnFragmentInteractionListener,
        TestFragment.OnFragmentInteractionListener {
    private CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView title = findViewById(R.id.title);
        String unitNo = getIntent().getStringExtra("UNIT_NO");
        String unitName = getIntent().getStringExtra("UNIT_NAME");

        title.setText("Unit no. "+unitNo+" : "+unitName);
        final TextView subtitle = findViewById(R.id.action_bar_subtitle);
        subtitle.setText(getIntent().getStringExtra("TOPIC_NAME"));

        //getSupportActionBar().setTitle("Unit 13 : Amines");
       // getSupportActionBar().setSubtitle("Classification of amines");
        String topic_id = getIntent().getStringExtra("TOPIC_ID");
        String videoID = getIntent().getStringExtra("VIDEO_ID");
        String videoDuration = getIntent().getStringExtra("VIDEO_DURATION");
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(ContentActivity.this, getSupportFragmentManager(),topic_id ,videoID,videoDuration,getIntent().getStringExtra("TOPIC_NAME"),getIntent().getStringExtra("SUBJECT") );
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onTestSelection() {
        viewPager.setCurrentItem(0,true);
    }

    @Override
    public void onNotesSelection(String page) {
        if(page.matches("previous"))viewPager.setCurrentItem(0,true);
        if(page.matches("next"))viewPager.setCurrentItem(2,true);
    }
    @Override
    public void onFragmentInteraction() {
        viewPager.setCurrentItem(1,true);
    }


}