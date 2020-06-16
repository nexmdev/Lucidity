package com.nexm.lucidity.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView testNoView,subjectView,unitNoView,unitnameView,descView,marksView,
            timeView,startView,dateView;



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
        dateView = itemView.findViewById(R.id.test_dateView);
        dateView.setVisibility(View.GONE);
        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition());
                }
            }
        });

    }

    public void bindData(final Test test, final Context context){
        testNoView.setText("TEST NO : "+String.valueOf(getAdapterPosition()+1));
        subjectView.setText(test.getSubject());
        unitNoView.setText("Unit no. : "+test.getUnitNo());
        unitnameView.setText(test.getUnit());
        descView.setText(test.getDescription());
        marksView.setText("MARKS : "+test.getMarks());
        timeView.setText("TIME : "+test.getTime()+" min");
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("Tests")
                .child(test.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            startView.setVisibility(View.INVISIBLE);

                            long date = dataSnapshot.getValue(long.class);
                            Date date1 = new Date(date);
                            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
                            String dateString = formater.format(date1);
                            dateView.setText("Completed on :\n"+dateString);
                            dateView.setVisibility(View.VISIBLE);
                            itemView.setBackgroundResource(R.drawable.neu_small_pressed);
                        }else{
                            startView.setVisibility(View.VISIBLE);
                            itemView.setBackgroundResource(R.drawable.neu_small);
                            dateView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
