package com.nexm.lucidity.viewHolders;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Subject;
import com.nexm.lucidity.models.Test;

public class TestHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView testNoView,subjectView,unitNoView,unitnameView,descView,marksView,timeView,startView;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener mlistener){
        TestHolder.listener = mlistener;
    }

    public TestHolder(@NonNull View itemView) {
        super(itemView);
        testNoView = itemView.findViewById(R.id.test_no);
        subjectView = itemView.findViewById(R.id.test_subject);
        unitNoView = itemView.findViewById(R.id.test_unit_no);
        unitnameView = itemView.findViewById(R.id.test_unit_name);
        descView = itemView.findViewById(R.id.test_description);
        marksView = itemView.findViewById(R.id.test_marks);
        timeView = itemView.findViewById(R.id.test_time);
        startView = itemView.findViewById(R.id.test_start);
        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition());
                }
            }
        });

    }

    public void bindData(final Test test, Context context){
        testNoView.setText("TEST NO : "+String.valueOf(getAdapterPosition()+1));
        subjectView.setText(test.getSubject());
        unitNoView.setText("Unit no. : "+test.getUnitNo());
        unitnameView.setText(test.getUnit());
        descView.setText(test.getDescription());
        marksView.setText("MARKS : "+test.getMarks());
        timeView.setText("TIME : "+test.getTime()+" min");

    }
}
