package com.nexm.lucidity.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.ContentActivity;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.MainActivity;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.CurrentTopic;
import com.nexm.lucidity.models.Subject;
import com.nexm.lucidity.models.Topic;
import com.nexm.lucidity.viewHolders.SubjectHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayClassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean chooseTopic = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView subjectView,unitView,topicView,progressView,videoView,notesView,
            questionView,continueView,commingSoon,welcome,name,lastSeen,ticker;
    private ProgressBar progressBar,top_progressbar;
    private Topic topic;
    private ConstraintLayout card;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private String currentSubject;
    private boolean STD10 = false;
    private ImageView info;

    public TodayClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayClassFragment newInstance(String param1, String param2) {
        TodayClassFragment fragment = new TodayClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_class, container, false);
        final TextView physics = view.findViewById(R.id.today_physics_textView);
        final TextView chemistry = view.findViewById(R.id.today_chemistry_textView);
        final TextView maths = view.findViewById(R.id.today_maths_textView);
        info = view.findViewById(R.id.today_info_button);
        ticker = view.findViewById(R.id.today_ticker);
        ticker.setSelected(true);
        fetchTicker();
        lastSeen = view.findViewById(R.id.today_lastseen);
       if(LUCIDITY_APPLICATION.standard.matches("STD-10-SEMI-ENG")){
            physics.setText("S-1");
            chemistry.setText("S-2");
            maths.setText("M");
            STD10 = true;
        }else{
            physics.setText("P");
            chemistry.setText("C");
            maths.setText("M");
           STD10 = false;
        }
        subjectView = view.findViewById(R.id.today_subjectView);
        unitView = view.findViewById(R.id.today_card_unit_textView);
        topicView = view.findViewById(R.id.today_card_topic_textView);
        videoView = view.findViewById(R.id.today_card_videotime_textView);
        notesView = view.findViewById(R.id.today_card_notes_textView);
        questionView = view.findViewById(R.id.today_card_question_textView);
        progressView = view.findViewById(R.id.today_card_progress_percentage);
        progressBar = view.findViewById(R.id.today_card_progressbar);
        continueView = view.findViewById(R.id.today_card_continue);
        top_progressbar = view.findViewById(R.id.today_top_progressBar);
        commingSoon = view.findViewById(R.id.today_comming_soon_textView);
        card = view.findViewById(R.id.today_cardView);
        welcome = view.findViewById(R.id.today_welcome);
        name = view.findViewById(R.id.today_name);
        welcome.setText(LUCIDITY_APPLICATION.standard);
        name.setText("Welcome - "+LUCIDITY_APPLICATION.studentName);

        top_progressbar.setVisibility(View.VISIBLE);
        commingSoon.setVisibility(View.GONE);
        currentSubject = STD10 ? "S-1":"PHYSICS";
        showTopicCard(currentSubject);
        setupAdapter("UNITS",LUCIDITY_APPLICATION.standard,currentSubject);

        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //card.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.nue_button));
                top_progressbar.setVisibility(View.VISIBLE);
                currentSubject = STD10 ? "S-1":"PHYSICS";
                showTopicCard(currentSubject);
                toggleBackground(physics,chemistry,maths);
                //continueView.setBackgroundResource(R.drawable.nue_button);
                updataRecycleView(currentSubject);


            }
        });
        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //card.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.neu_blue));
                top_progressbar.setVisibility(View.VISIBLE);
                currentSubject = STD10 ? "S-2":"CHEMISTRY";
                showTopicCard(currentSubject);
                toggleBackground(chemistry,physics,maths);
               // continueView.setBackgroundResource(R.drawable.neu_blue);
                //setupAdapter("UNITS","STD-12-SCI","CHEMISTRY");
                updataRecycleView(currentSubject);


            }
        });
        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // card.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.neu_yellow));
                top_progressbar.setVisibility(View.VISIBLE);
                currentSubject = STD10 ? "M":"MATHS";
                showTopicCard(currentSubject);
                toggleBackground(maths,chemistry,physics);
                //continueView.setBackgroundResource(R.drawable.neu_yellow);
                updataRecycleView(currentSubject);


            }
        });
        continueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("UNIT_NO",topic.getUnitNo());
                intent.putExtra("UNIT_ID",topic.getUnitID());
                intent.putExtra("UNIT_NAME",topic.getUnitName());
                intent.putExtra("TOPIC_ID",topic.getId());
                intent.putExtra("TOPIC_NAME",topic.getName());
                intent.putExtra("VIDEO_ID",topic.getVideoID());
                intent.putExtra("VIDEO_DURATION",topic.getVideoDuration());
                intent.putExtra("SUBJECT",currentSubject);
                getActivity().startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("CALLER","About");
                getActivity().startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.today_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

    private void fetchTicker() {
        LUCIDITY_APPLICATION.root_reference
                .child("TICKER")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            ticker.setText(dataSnapshot.getValue(String.class));
                        }else{
                            ticker.setText("* Welcome to Tutor pro , your personal tution at home . *** Attention **** For students taking admission to class 11 science - a free foundation course is available on Tutor pro.  ");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        ticker.setText("* Welcome to Tutor pro , your personal tution at home . * For any doubts contact Mahalley sir on 7775971543 ");
                    }
                });
    }

    private void updataRecycleView(String subject) {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child("UNITS")
                .child(LUCIDITY_APPLICATION.standard)
                .child(subject);
        FirebaseRecyclerOptions<Subject> options =  new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(query, Subject.class)
                .build();
        firebaseRecyclerAdapter.updateOptions(options);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TodayClassFragment.OnFragmentInteractionListener) {
            mListener = (TodayClassFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        setupAdapter("UNITS",LUCIDITY_APPLICATION.standard,"PHYSICS");
    }

    private void setupAdapter(String child1 , String child2,String child3) {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child(child1)
                .child(child2)
                .child(child3);
        FirebaseRecyclerOptions<Subject> options =  new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(query, Subject.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Subject, SubjectHolder>(options) {
            @Override
            public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sub_unit_item, parent, false);

                return new SubjectHolder(view);
            }
            @Override
            protected void onBindViewHolder(SubjectHolder holder, int position, final Subject model) {
                holder.bindData(model,getActivity());
                holder.setOnItemClickListner(new SubjectHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int currentposition) {

                        // Toast.makeText(getActivity(),getItem(currentposition).getName(),Toast.LENGTH_SHORT).show();
                        if(mListener != null){
                           /* mListener.onSubUnitSelection(getItem(currentposition).getId(),
                                    getItem(currentposition).getName(),
                                    getItem(currentposition).getColor());*/
                        }
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("UNIT_ID",getItem(currentposition).getId());
                        intent.putExtra("UNIT_NAME",getItem(currentposition).getName());
                        intent.putExtra("UNIT_NO",getItem(currentposition).getNumber());
                        intent.putExtra("DESCRIPTION",getItem(currentposition).getDescription());
                        intent.putExtra("SUBJECT",currentSubject);
                        intent.putExtra("CURRENT_TOPIC",topic.getId());
                        intent.putExtra("CALLER","TOPICS");
                        getActivity().startActivity(intent);

                    }
                });
            }
            @Override
            public void onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...

            }

            @Override
            public void onError(DatabaseError e) {
                // Called when there is an error getting data. You may want to update
                // your UI to display an error message to the user.
                // ...
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void toggleBackground(TextView maths, TextView chemistry, TextView physics) {
        maths.setBackgroundResource(R.drawable.subject_active_neu);
        maths.setTextColor(getActivity().getResources().getColor(R.color.red));
        chemistry.setBackgroundResource(R.drawable.subject_default_neu);
        chemistry.setTextColor(getActivity().getResources().getColor(R.color.grey));
        physics.setBackgroundResource(R.drawable.subject_default_neu);
        physics.setTextColor(getActivity().getResources().getColor(R.color.grey));
    }

    private void showTopicCard(String physics) {
        if(STD10 && physics.matches("S-1")) subjectView.setText("SCIENCE-1");
        if(STD10 && physics.matches("S-2")) subjectView.setText("SCIENCE-2");
        if(STD10 && physics.matches("M")) subjectView.setText("MATHS");
        if(!STD10)subjectView.setText(physics);
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("Current_Topic")
                .child(physics)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            CurrentTopic currentTopic = dataSnapshot.getValue(CurrentTopic.class);
                            getTopic(currentTopic.getProgress(),currentTopic.getId(),currentTopic.getUnitid());
                        }else{
                            getTopic(0,"x","x");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getTopic(final int progress, final String topicID, String unitID) {
        Query query;
        if(topicID.matches("x")){
            chooseTopic=true;
            query = LUCIDITY_APPLICATION.root_reference
                    .child("TOPICS")
                    .child(LUCIDITY_APPLICATION.standard)
                    .child(currentSubject)
                    .limitToFirst(1);
        }else {
            query = LUCIDITY_APPLICATION.root_reference
                    .child("TOPICS")
                    .child(LUCIDITY_APPLICATION.standard)
                    .child(currentSubject)
                    .orderByChild("unitID")
                    .equalTo(unitID);

        }/*else{
            query = LUCIDITY_APPLICATION.root_reference
                    .child("TOPICS")
                    .child("STD-12-SCI")
                    .child(currentSubject)
                    .startAt(topicID)
                    .limitToFirst(1);

        }*/


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                top_progressbar.setVisibility(View.GONE);
               // ArrayList<Topic> topics = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        Topic topic1 =child.getValue(Topic.class);
                        if(chooseTopic){
                            topic = topic1;
                            chooseTopic = false;
                        }
                        if(topicID.matches(topic1.getId())){
                            chooseTopic = true;
                            lastSeen.setText(topic1.getName());
                        }

                    }
                   // topic = topics.get(0);
                    card.setVisibility(View.VISIBLE);
                    commingSoon.setVisibility(View.GONE);
                    unitView.setText("UNIT NO : "+topic.getUnitNo()+" :: "+topic.getUnitName());
                    topicView.setText(topic.getName());
                    notesView.setText(String.valueOf(topic.getNotes())+" cards");
                    questionView.setText(String.valueOf(topic.getQuestions())+" questions");
                    videoView.setText(topic.getVideoDuration()+" min");
                    getProgress(topic.getId(),topic.getUnitID());


                }else{
                    card.setVisibility(View.INVISIBLE);
                    commingSoon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Something went wrong ,please try again !",Toast.LENGTH_SHORT).show();
                top_progressbar.setVisibility(View.GONE);
                card.setVisibility(View.INVISIBLE);
                commingSoon.setVisibility(View.VISIBLE);

            }
        });

    }

    private void getProgress(final String id, final String unitID) {
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
                            progressView.setText(String.valueOf(progress)+" %");
                            if(progress==100){
                                updateCurrentTopic(id,progress,unitID);
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setProgress(0);
                        progressView.setText(String.valueOf(0)+" %");
                    }
                });
    }
    private void updateCurrentTopic(String topicID,int progress,String unit_ID) {
        CurrentTopic currentTopic = new CurrentTopic();
        currentTopic.setId(topicID);
        currentTopic.setProgress(progress);
        currentTopic.setUnitid(unit_ID);
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("Current_Topic")
                .child(currentSubject)
                .setValue(currentTopic)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showTopicCard(currentSubject);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
