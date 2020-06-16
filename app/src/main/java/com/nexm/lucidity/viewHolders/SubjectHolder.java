package com.nexm.lucidity.viewHolders;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Subject;

public class SubjectHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView backColor,frontColor,name;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener mlistener){
        SubjectHolder.listener = mlistener;
    }

    public SubjectHolder(@NonNull View itemView) {
        super(itemView);
        backColor = itemView.findViewById(R.id.sub_unit_item_color_textview_back);
        frontColor = itemView.findViewById(R.id.sub_unit_item_color_textview_front);
        name = itemView.findViewById(R.id.sub_unit_item_color_textview_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!= null){
                    listener.onItemClick(getAdapterPosition());
                }
            }
        });

    }

    public void bindData(final Subject subject, Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Jost-Regular.ttf");
        name.setTypeface(typeface);
        name.setText(subject.getName());
        frontColor.setText(subject.getNumber());
        if(subject.getName().matches("CHEMISTRY")){
            backColor.setBackgroundResource(R.drawable.chemm);
            frontColor.setVisibility(View.GONE);
        }else if(subject.getName().matches("PHYSICS")){
            backColor.setBackgroundResource(R.drawable.phyy);
            frontColor.setVisibility(View.GONE);
        }else if(subject.getName().matches("MATHS")){
            backColor.setBackgroundResource(R.drawable.mathss);
            frontColor.setVisibility(View.GONE);
        }else{
           // backColor.setBackgroundColor(Color.parseColor(subject.getColor()));
            frontColor.setVisibility(View.VISIBLE);
        }

    }
}
