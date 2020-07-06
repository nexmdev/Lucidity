package com.nexm.lucidity.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nexm.lucidity.R;
import com.nexm.lucidity.fragments.NotesFragment;
import com.nexm.lucidity.fragments.TestFragment;
import com.nexm.lucidity.fragments.VideoFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;
    private String topic_id,videoID,videoDuration,topic_Name,Subject,unit_ID;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String topicid, String vID, String vDuration, String topicName,String subject,String unitID) {
        super(fm);
        mContext = context;
        topic_id = topicid;
        videoID = vID;
        videoDuration = vDuration;
        topic_Name = topicName;
        Subject = subject;
        unit_ID = unitID;
    }


    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0 :
              return VideoFragment.newInstance(videoID,videoDuration,topic_id);

            case 1 :
                return NotesFragment.newInstance(topic_id,"x");
            case 2 :
                return TestFragment.newInstance(topic_id,topic_Name,Subject,unit_ID);

                default:
                    return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }



}