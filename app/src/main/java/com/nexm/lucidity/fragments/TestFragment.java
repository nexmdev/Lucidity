package com.nexm.lucidity.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.CurrentTopic;
import com.nexm.lucidity.models.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private TextView questionNoTextView,questionTextTextView,right,wrong,next,option1,
            option2,option3,unlock_answer,noTestContinue;
    private ImageView question_image,option1Image,option2Image,option3Image;
    private ProgressBar progressBar;


    // TODO: Rename and change types of parameters
    private String topicID,selectedAnswer,subject;
    private String mParam2;
    private AnimationDrawable myFrameAnimation1;
    private ArrayList<Question> questions = new ArrayList<>();
    private int currentQ = 0,selectedOption;
    private int progress;

    private OnFragmentInteractionListener mListener;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2,String param3) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicID = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            subject = getArguments().getString(ARG_PARAM3);
        }

    }

    private void getQuestions(final View view) {
        LUCIDITY_APPLICATION.root_reference
                .child("QUESTIONS")
                .child(topicID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                Question question = childSnapshot.getValue(Question.class);
                                questions.add(question);
                            }
                            view.findViewById(R.id.question_parent_layout).setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            noTestContinue.setVisibility(View.GONE);
                            setQuestion();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            view.findViewById(R.id.question_parent_layout).setVisibility(View.INVISIBLE);
                            unlock_answer.setVisibility(View.VISIBLE);
                            noTestContinue.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_question, container, false);
        questionNoTextView = view.findViewById(R.id.question_noView);
        questionTextTextView = view.findViewById(R.id.question_text);
        right = view.findViewById(R.id.question_right);
        right.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(getActivity(),R.drawable.ic_done_black_24dp),null);
        wrong = view.findViewById(R.id.question_wrong);
        wrong.setCompoundDrawablesWithIntrinsicBounds(null,null,AppCompatResources.getDrawable(getActivity(),R.drawable.ic_close_black_24dp),null);
        next = view.findViewById(R.id.question_check);
        question_image = view.findViewById(R.id.question_image);
        option1Image = view.findViewById(R.id.question_option1_img);
        option2Image = view.findViewById(R.id.question_option2_img);
        option3Image = view.findViewById(R.id.question_option3_img);
        option1 = view.findViewById(R.id.question_option1);
        option2 = view.findViewById(R.id.question_option2);
        option3 = view.findViewById(R.id.question_option3);
        progressBar = view.findViewById(R.id.question_progressbar);
        noTestContinue = view.findViewById(R.id.test_no_question_continue);
        progressBar.setVisibility(View.VISIBLE);
        option1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

        option2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

        option3.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);


        unlock_answer = view.findViewById(R.id.question_unlock);
        //unlock_answer.setCompoundDrawablesWithIntrinsicBounds(null,AppCompatResources.getDrawable(getActivity(),R.drawable.ic_action_movie),null,null);
        getQuestions(view);



        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "1";
                selectedOption=option1.getId();
                processOptionClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option1,option2,option3);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "2";
                selectedOption=option2.getId();
                processOptionClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option2,option1,option3);

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "3";
                selectedOption=option3.getId();
                processOptionClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option3,option1,option2);

            }
        });
        option1Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "1";
                selectedOption=option1Image.getId();
                processOptionImageClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option1Image,option2Image,option3Image);
            }
        });
        option2Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "2";
                selectedOption=option2Image.getId();
                processOptionImageClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option2Image,option1Image,option3Image);

            }
        });
        option3Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "3";
                selectedOption=option3Image.getId();
                processOptionImageClick(R.drawable.neu_small_pressed, R.drawable.neu_small, option3Image,option1Image,option2Image);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                switch(next.getText().toString()){
                    case "CHECK":


                        if(selectedAnswer.matches(questions.get(currentQ).getCorrectOption())){
                            right.setVisibility(View.VISIBLE);
                            animateRight();
                            wrong.setVisibility(View.GONE);

                           // TextView textView = view.findViewById(selectedOption);
                           // textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led),null,null,null);

                           /* if(selectedOption == option1.getId()){
                                option1.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led),null);
                            }else{
                                option2.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led),null);
                            }*/

                           // animateLeds(textView);
                            next.setText("CONTINUE");
                        }else{

                           // unlock_answer.setVisibility(View.VISIBLE);
                            right.setVisibility(View.GONE);
                            wrong.setVisibility(View.VISIBLE);
                            animateWrong();
                           // TextView textView = view.findViewById(selectedOption);
                           // textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led_red),null,null,null);

                            /*if(selectedOption == option1.getId()){
                                option1.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led_red),null);
                            }else{
                                option2.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led_red),null);
                            }*/
                           // animateLeds(textView);
                            next.setText("TRY AGAIN");
                        }
                        break;
                    case "TRY AGAIN":
                        setDefaultViews();
                        //shuffle();
                        break;
                    case "CONTINUE":
                        currentQ++;

                        if(currentQ >= questions.size()){
                            //Toast.makeText(getActivity(), "Last Q", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                            updateProgress();

                        }else{
                            setQuestion();
                            setDefaultViews();
                        }
                        break;
                }
            }
        });
        noTestContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                updateProgress();
            }
        });
        return view;
    }

    private void updateProgress() {
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child(topicID)
                .child("questions")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                            Integer c = mutableData.getValue(Integer.class);
                            if(c == null){
                                mutableData.setValue(30);
                            }else {
                                //mutableData.setValue(c+30);
                            }

                            return Transaction.success(mutableData);


                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        //Toast.makeText(getActivity(), "Progress updated "+databaseError, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        showSuccessDialog(dataSnapshot.getValue(int.class));
                    }
                });
    }

    private void showSuccessDialog(int p) {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView topic = dialog.findViewById(R.id.dialog_topic_name);
        final TextView continueButton = dialog.findViewById(R.id.dialog_continue);
        final TextView returnButton = dialog.findViewById(R.id.dialog_return);
        final TextView congrats = dialog.findViewById(R.id.dialog_congrat);
        final TextView message = dialog.findViewById(R.id.dialog_message);
        final ImageView image = dialog.findViewById(R.id.dialog_image);
        final ProgressBar progressBar = dialog.findViewById(R.id.dialog_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child(topicID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progress = 0;
                        for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                            progress += childSnapShot.getValue(int.class);
                        }
                       // Toast.makeText(getActivity(),"Progress :"+progress,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        if(progress == 100){
                            updateCurrentTopic();
                            congrats.setText("CONGRATULATIONS");
                            message.setText("You have completed the topic :");
                            image.setVisibility(View.VISIBLE);
                            image.setImageResource(R.drawable.ic_verified_user_black_24dp);
                            returnButton.setVisibility(View.GONE);
                            topic.setText(mParam2);
                        }else if(progress==30){
                            returnButton.setVisibility(View.VISIBLE);
                            congrats.setText("Topic incomplete !");
                            message.setText("Incomplete : NOTES AND EXPLANATION");
                            image.setVisibility(View.VISIBLE);
                            image.setImageResource(R.drawable.ic_assistant_photo_black_24dp);
                            topic.setText(mParam2);
                        }else if(progress==60){
                            returnButton.setVisibility(View.VISIBLE);
                            congrats.setText("Topic incomplete !");
                            message.setText("Incomplete : EXPLANATION");
                            image.setVisibility(View.VISIBLE);
                            image.setImageResource(R.drawable.ic_assistant_photo_black_24dp);
                            topic.setText(mParam2);
                        }else if(progress==70){
                            returnButton.setVisibility(View.VISIBLE);
                            congrats.setText("Topic incomplete !");
                            message.setText("Incomplete : NOTES ");
                            image.setVisibility(View.VISIBLE);
                            image.setImageResource(R.drawable.ic_assistant_photo_black_24dp);
                            topic.setText(mParam2);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();

                getActivity().onBackPressed();

            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.onTestSelection();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updateCurrentTopic() {
        CurrentTopic currentTopic = new CurrentTopic();
        currentTopic.setId(topicID);
        currentTopic.setProgress(progress);
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("Current_Topic")
                .child(subject)
                .setValue(currentTopic)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processOptionImageClick(int tab_background, int tab_default, ImageView selectedoption,
                                         ImageView otherOption1,ImageView otherOption2) {
        next.setVisibility(View.VISIBLE);
        selectedoption.setBackgroundResource(tab_background);
       // selectedoption.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_yellow),null,null,null);
       // otherOption1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
       // otherOption2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        otherOption1.setBackgroundResource(tab_default);
        otherOption2.setBackgroundResource(tab_default);


    }

    private void setDefaultViews() {
        unlock_answer.setVisibility(View.GONE);
        option1.setBackgroundResource(R.drawable.neu_small);
        option2.setBackgroundResource(R.drawable.neu_small);
        option3.setBackgroundResource(R.drawable.neu_small);
        option1Image.setBackgroundResource(R.drawable.neu_small);
        option2Image.setBackgroundResource(R.drawable.neu_small);
        option3Image.setBackgroundResource(R.drawable.neu_small);

       /* option1.getCompoundDrawables();
        option1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        option2.getCompoundDrawables();
        option2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        option3.getCompoundDrawables();
        option3.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);*/

        if(myFrameAnimation1!=null){
            if(myFrameAnimation1.isRunning()){
                myFrameAnimation1.stop();
            }

        }

        if(right.getVisibility()==View.VISIBLE){
            right.setX(next.getX());
            right.setVisibility(View.GONE);
        }

        if(wrong.getVisibility()==View.VISIBLE){
           wrong.setX(next.getX());
            wrong.setVisibility(View.GONE);
        }

        next.setText("CHECK");
        next.setVisibility(View.GONE);
    }
    private void animateLeds(TextView selectedOption) {

        Drawable[] d1 =  selectedOption.getCompoundDrawables();
        if(d1[0]!=null){
            myFrameAnimation1 = (AnimationDrawable) d1[0];
            myFrameAnimation1.start();
        }
    }
    private void animateRight(){

        float a=next.getWidth();
        right.animate()
                .translationX(-a)
                .setDuration(100);

    }
    private void animateWrong(){

        float a=next.getWidth();
        wrong.animate()
                .translationX(-a)
                .setDuration(100);

    }
    private void processOptionClick(int tab_background, int tab_default, TextView selectedoption,
                                    TextView otherOption1,TextView otherOption2) {
        next.setVisibility(View.VISIBLE);
        selectedoption.setBackgroundResource(tab_background);
       // selectedoption.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_yellow),null,null,null);
       // otherOption1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
      //  otherOption2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        otherOption1.setBackgroundResource(tab_default);
        otherOption2.setBackgroundResource(tab_default);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTestSelection();
        }
    }
    private void setQuestion() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_action_document);
        options.error(R.drawable.ic_close_black_24dp);

        if(questions.size()!=0 && currentQ <= questions.size() ){
            questionNoTextView.setText(String.valueOf(currentQ+1) +"/ "+String.valueOf(questions.size()));

            String qText = questions.get(currentQ).getQuestionText();
            if(qText.matches("")){
                questionTextTextView.setVisibility(View.GONE);

            }else{
                questionTextTextView.setVisibility(View.VISIBLE);
                questionTextTextView.setText(questions.get(currentQ).getQuestionText());
            }
            String a1Text = questions.get(currentQ).getAnswer1();
            if(a1Text.matches("")){
                option1.setVisibility(View.GONE);

            }else{
                option1.setVisibility(View.VISIBLE);
                option1.setText(questions.get(currentQ).getAnswer1());
            }
            String a2Text = questions.get(currentQ).getAnswer2();
            if(a2Text.matches("")){
                option2.setVisibility(View.GONE);

            }else{
                option2.setVisibility(View.VISIBLE);
                option2.setText(questions.get(currentQ).getAnswer2());
            }
            String a3Text = questions.get(currentQ).getAnswer3();
            if(a3Text.matches("")){
                option3.setVisibility(View.GONE);

            }else{
                option3.setVisibility(View.VISIBLE);
                option3.setText(questions.get(currentQ).getAnswer3());
            }
            if(questions.get(currentQ).getAnswer1Image().matches("x")){
                option1Image.setVisibility(View.GONE);

            }else{
                option1Image.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(questions.get(currentQ).getAnswer1Image())
                        .apply(options)
                        .into(option1Image);
            }
            if(questions.get(currentQ).getAnswer2Image().matches("x")){
                option2Image.setVisibility(View.GONE);

            }else{
                option2Image.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(questions.get(currentQ).getAnswer2Image())
                        .apply(options)
                        .into(option2Image);
            }
            if(questions.get(currentQ).getAnswer3Image().matches("x")){
                option3Image.setVisibility(View.GONE);

            }else{
                option3Image.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(questions.get(currentQ).getAnswer3Image())
                        .apply(options)
                        .into(option3Image);
            }
            if(questions.get(currentQ).getQuestionImage().matches("x")){
                question_image.setVisibility(View.GONE);

            }else{
                question_image.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(questions.get(currentQ).getQuestionImage())
                        .apply(options)
                        .into(question_image);
            }



        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTestSelection();
    }
}
