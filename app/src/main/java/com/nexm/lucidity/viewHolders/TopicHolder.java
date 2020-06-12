package com.nexm.lucidity.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.ContentActivity;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.fragments.TopicsFragment;
import com.nexm.lucidity.models.Topic;

public class TopicHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView progress_percentage,videoTime,notes,questions,continueButton;
    private final ImageView overflow , timeDot;
    private final ConstraintLayout detail_layout;
    private final TextView topicName;
    private final ProgressBar progressBar;
    private final TextView dottedLine;

    public interface OnItemClickListener {
        void onItemClick(int position, String caller);
    }

    public void setOnItemClickListner(OnItemClickListener mlistener){
        TopicHolder.listener = mlistener;
    }

    public TopicHolder(@NonNull View itemView) {
        super(itemView);
        progress_percentage = itemView.findViewById(R.id.today_card_progress_percentage);
        videoTime = itemView.findViewById(R.id.today_card_videotime_textView);
        notes = itemView.findViewById(R.id.today_card_notes_textView);
        questions = itemView.findViewById(R.id.today_card_question_textView);
        continueButton = itemView.findViewById(R.id.today_card_continue);
        detail_layout = itemView.findViewById(R.id.today_cardView);
        overflow = itemView.findViewById(R.id.topics_recyler_item_overflow);
        timeDot = itemView.findViewById(R.id.topics_recyler_item_timeline_dot);
        topicName = itemView.findViewById(R.id.topics_recyler_item_topic_name);
        progressBar = itemView.findViewById(R.id.today_card_progressbar);
        dottedLine = itemView.findViewById(R.id.topics_recyler_item_timeline_dotted_line);


    }
    private void setDetails(Context context, int i) {
        if( detail_layout.getVisibility()== View.GONE){
            detail_layout.setVisibility(View.VISIBLE);

            timeDot.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            topicName.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
            overflow.setImageResource(R.drawable.ic_expand_less_black_24dp);
            int prevIndex = TopicsFragment.detailIndex;
            TopicsFragment.detailIndex=i;
            if(listener!=null){
                listener.onItemClick(prevIndex, "overflow");
            }

        }else{
            overflow.setImageResource(R.drawable.ic_expand_more_black_24dp);
            detail_layout.setVisibility(View.GONE);

            timeDot.setImageResource(R.drawable.ic_brightness_1_black_24dp);
            topicName.setTextColor(ContextCompat.getColor(context,android.R.color.background_dark));
        }
    }

    public void bindData(final Topic topic, final Context context){
        if(TopicsFragment.detailIndex==-1&&TopicsFragment.currentTopicID.matches(topic.getId())){
            TopicsFragment.detailIndex=getAdapterPosition();}
        getProgress(topic.getId());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Jost-Regular.ttf");
        //topicName.setTypeface(typeface);
        topicName.setText(topic.getName());
        //dottedLine.setBackgroundResource(R.drawable.dotted_line);
        videoTime.setText(topic.getVideoDuration()+" min");
        notes.setText(String.valueOf(topic.getNotes())+ " cards");
        questions.setText(String.valueOf(topic.getQuestions())+" questions");
       if(TopicsFragment.detailIndex == getAdapterPosition()){

            overflow.setImageResource(R.drawable.ic_expand_less_black_24dp);
            detail_layout.setVisibility(View.VISIBLE);
            timeDot.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            topicName.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

        }else{
            overflow.setImageResource(R.drawable.ic_expand_more_black_24dp);
            detail_layout.setVisibility(View.GONE);
            timeDot.setImageResource(R.drawable.ic_brightness_1_black_24dp);

            topicName.setTextColor(ContextCompat.getColor(context,R.color.grey));
        }
        topicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setDetails(context,getAdapterPosition());
            }
        });
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetails(context,getAdapterPosition());
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),"continue");
                }
               /* Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("UNIT_NO",topic.getUnitNo());
                intent.putExtra("UNIT_NAME",topic.getUnitName());
                intent.putExtra("TOPIC_ID",topic.getId());
                intent.putExtra("TOPIC_NAME",topic.getName());
                intent.putExtra("VIDEO_ID",topic.getVideoID());
                intent.putExtra("VIDEO_DURATION",topic.getVideoDuration());
                context.startActivity(intent);*/
            }
        });

    }
    private void getProgress(String id) {
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int progress = 0;
                        if(dataSnapshot.exists()) {

                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                progress += childSnapShot.getValue(int.class);
                            }
                        }
                        progressBar.setProgress(progress);
                        progress_percentage.setText("Completed "+String.valueOf(progress)+" %");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setProgress(0);
                        progress_percentage.setText("Completed "+String.valueOf(0)+" %");
                    }
                });
    }

}
