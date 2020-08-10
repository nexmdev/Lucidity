package com.nexm.lucidity.viewHolders;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.lucidity.R;
import com.nexm.lucidity.models.LiveClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LiveHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView subject,unit,topic,teacherName,time,liveBadge;
    private final ImageView teacherImage ;
    private final ConstraintLayout constraintLayout;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener mlistener){
        LiveHolder.listener = mlistener;
    }

    public LiveHolder(@NonNull final View itemView) {
        super(itemView);
        subject = itemView.findViewById(R.id.live_item_subject);
        unit = itemView.findViewById(R.id.live_item_unit);
        topic = itemView.findViewById(R.id.live_item_topic);
        teacherName = itemView.findViewById(R.id.live_item_teachername);
        teacherImage = itemView.findViewById(R.id.live_item_teacher_image);
        time = itemView.findViewById(R.id.live_item_time);
        constraintLayout = itemView.findViewById(R.id.live_item_constraint_layout);
        liveBadge = itemView.findViewById(R.id.live_item_livebadge);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(getAdapterPosition());
            }
        });

    }

    public void bindData(final LiveClass liveclass, final Context context){
        checkTime(liveclass);
        String subjectS ="";
        if(liveclass.getSubject().matches("S-1")){
            subjectS = "SCIENCE-I";
            constraintLayout.setBackgroundResource(R.drawable.gradient_background);

        }else if(liveclass.getSubject().matches("S-2")){
            subjectS = "SCIENCE-II";
            constraintLayout.setBackgroundResource(R.drawable.gradient_background_p);
        }else{
            subjectS = liveclass.getSubject();
        }
        subject.setText(subjectS);
        unit.setText("UNIT. "+liveclass.getUnitNo()+" - "+liveclass.getUnitName());
        topic.setText(liveclass.getName());

        time.setText(liveclass.getStime() +" to "+liveclass.getEtime()+" am");
        teacherName.setText("by : "+liveclass.getTeacherName());

    }
    private void checkTime(com.nexm.lucidity.models.LiveClass today) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String current = sdf.format(c.getTime());

        if(current.compareTo(today.getStime())<0 ){
            liveBadge.setText("Scheduled");
        }else if (current.compareTo(today.getEtime())>0){

            liveBadge.setText("Recorded");
        }else{
            liveBadge.setText("Live now");
        }
    }


}
