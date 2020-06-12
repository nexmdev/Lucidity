package com.nexm.lucidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.Main_ViewHolder> {

    private Context context;
    private int detailIndex = 0;

    public TestAdapter(){

    }

    @NonNull
    @Override
    public Main_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.topics_item_layout,viewGroup,false);
        return new Main_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Main_ViewHolder main_viewHolder, final int i) {

        if(detailIndex == i){
           main_viewHolder.overflow.setImageResource(R.drawable.ic_expand_less_black_24dp);
           main_viewHolder.detail_layout.setVisibility(View.VISIBLE);
           main_viewHolder.timeDot.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
           main_viewHolder.topicName.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

       }else{
            main_viewHolder.overflow.setImageResource(R.drawable.ic_expand_more_black_24dp);
           main_viewHolder.detail_layout.setVisibility(View.GONE);
           main_viewHolder.timeDot.setImageResource(R.drawable.ic_brightness_1_black_24dp);
            main_viewHolder.topicName.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
       }
        main_viewHolder.topicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetails(main_viewHolder, i);
            }
        });
       main_viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setDetails(main_viewHolder, i);

           }
       });


    }

    private void setDetails(@NonNull Main_ViewHolder main_viewHolder, int i) {
        if( main_viewHolder.detail_layout.getVisibility()== View.GONE){
            main_viewHolder.detail_layout.setVisibility(View.VISIBLE);

            main_viewHolder.timeDot.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            main_viewHolder.topicName.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            main_viewHolder.overflow.setImageResource(R.drawable.ic_expand_less_black_24dp);
            int prevIndex = detailIndex;
            detailIndex=i;
            notifyItemChanged(prevIndex);

        }else{
            main_viewHolder.overflow.setImageResource(R.drawable.ic_expand_more_black_24dp);
            main_viewHolder.detail_layout.setVisibility(View.GONE);

            main_viewHolder.timeDot.setImageResource(R.drawable.ic_brightness_1_black_24dp);
            main_viewHolder.topicName.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static  class Main_ViewHolder extends RecyclerView.ViewHolder {

        final ImageView overflow , timeDot;
        final ConstraintLayout detail_layout;
        final TextView topicName;


        public Main_ViewHolder(@NonNull View itemView) {
            super(itemView);

            detail_layout = itemView.findViewById(R.id.today_cardView);
            overflow = itemView.findViewById(R.id.topics_recyler_item_overflow);
            timeDot = itemView.findViewById(R.id.topics_recyler_item_timeline_dot);
            topicName = itemView.findViewById(R.id.topics_recyler_item_topic_name);

        }
    }



}
