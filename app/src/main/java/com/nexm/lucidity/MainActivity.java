package com.nexm.lucidity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.nexm.lucidity.fragments.PaperFragment;
import com.nexm.lucidity.fragments.Sub_UnitFragment;
import com.nexm.lucidity.fragments.TodayClassFragment;
import com.nexm.lucidity.fragments.TopicsFragment;
import com.nexm.lucidity.fragments.UnitFragment;
import com.nexm.lucidity.models.PaperQuestion;

public class MainActivity extends AppCompatActivity implements TopicsFragment.OnFragmentInteractionListener,
        Sub_UnitFragment.OnFragmentInteractionListener,
        TodayClassFragment.OnFragmentInteractionListener,
        UnitFragment.OnFragmentInteractionListener,
        PaperFragment.OnFragmentInteractionListener {
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //getSupportActionBar().setElevation(0);
        String caller = getIntent().getStringExtra("CALLER");
        if(caller.matches("TOPICS")){
            String unitNo = getIntent().getStringExtra("UNIT_NO");
            String unitName = getIntent().getStringExtra("UNIT_NAME");
            String unitID = getIntent().getStringExtra("UNIT_ID");
            String subject = getIntent().getStringExtra("SUBJECT");
            String description = getIntent().getStringExtra("DESCRIPTION");
            String current = getIntent().getStringExtra("CURRENT_TOPIC");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentlayout,TopicsFragment.newInstance(unitNo,unitName,unitID,subject,description,current ))
                    .commit();
        }else{
            String unitNo = getIntent().getStringExtra("UNIT_NO");
            String unitName = getIntent().getStringExtra("UNIT_NAME");
            String testID = getIntent().getStringExtra("TEST_ID");
            String subject = getIntent().getStringExtra("SUBJECT");
            String marks = getIntent().getStringExtra("TEST_MARKS");
            String time = getIntent().getStringExtra("TEST_TIME");
            String desc = getIntent().getStringExtra("DESC");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentlayout, PaperFragment.newInstance(testID,marks,time,subject,unitName,unitNo,desc))
                    .commit();

        }

    }

    @Override
    public void onTopicSelection(Uri uri) {

    }

    @Override
    public void onSubUnitSelection(String id, String subName, String color) {
        UnitFragment unitFragment = UnitFragment.newInstance(id,subName, color);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentlayout,unitFragment)
                .addToBackStack("sub")
                .commit();
    }

    @Override
    public void onUnitSelection(String id, String unitName, String color) {

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        TopicsFragment topicsFragment = TopicsFragment.newInstance(id,unitName,color,"","", "");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentlayout,topicsFragment)
                .addToBackStack("unit")
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPaperselection(Uri uri) {

    }
}
