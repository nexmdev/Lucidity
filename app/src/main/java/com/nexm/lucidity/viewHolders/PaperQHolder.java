package com.nexm.lucidity.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.PaperQuestion;
import com.nexm.lucidity.models.Test;

public class PaperQHolder extends RecyclerView.ViewHolder {
    private static OnItemClickListener listener;
    private final TextView qNoView,textView,marksView;
    private final ImageView imageView;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener mlistener){
        PaperQHolder.listener = mlistener;
    }

    public PaperQHolder(@NonNull View itemView) {
        super(itemView);
        qNoView = itemView.findViewById(R.id.paper_q_noView);
        textView = itemView.findViewById(R.id.paper_q_textView);
        imageView = itemView.findViewById(R.id.paper_q_imageView);
        marksView = itemView.findViewById(R.id.paper_q_marksView);


    }

    public void bindData(final PaperQuestion question, Context context){

            qNoView.setText(String.valueOf(getAdapterPosition()+1));
            if(question.getText().matches("x")){
                textView.setVisibility(View.GONE);
            }else{
                textView.setVisibility(View.VISIBLE);
                textView.setText(question.getText());
            }
            if(question.getImageUrl().matches("x")){
                imageView.setVisibility(View.GONE);
            }else{
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(question.getImageUrl())
                        .into(imageView);
            }
            marksView.setText("("+question.getMarks()+")");
    }
}
