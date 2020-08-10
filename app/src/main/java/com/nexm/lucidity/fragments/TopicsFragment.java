package com.nexm.lucidity.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.ContentActivity;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.TestAdapter;
import com.nexm.lucidity.models.CurrentTopic;
import com.nexm.lucidity.models.Topic;
import com.nexm.lucidity.viewHolders.TopicHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    // TODO: Rename and change types of parameters
    private String unitNo;
    private String unitName;
    private String unitID;
    private String subject,description;
    public static String currentTopicID = "x";
    private RecyclerView topicsRecyclerView;
    private TestAdapter testAdapter;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private OnFragmentInteractionListener mListener;
    public static int detailIndex = -1;
    public TopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3
     * @param current
     * @return A new instance of fragment TopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicsFragment newInstance(String param1, String param2, String param3, String param4, String param5, String current) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, current);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unitNo = getArguments().getString(ARG_PARAM1);
            unitName = getArguments().getString(ARG_PARAM2);
            unitID = getArguments().getString(ARG_PARAM3);
            subject = getArguments().getString(ARG_PARAM4);
            description = getArguments().getString(ARG_PARAM5);
            currentTopicID = getArguments().getString(ARG_PARAM6);
        }
        setupAdapter();
    }
    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        detailIndex=-1;
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topics, container, false);

        final TextView unitname = view.findViewById(R.id.unit_name);
        final TextView unitNumber = view.findViewById(R.id.topics_unitNo);

        final TextView intro = view.findViewById(R.id.topics_intro);

        unitname.setText(unitName);
        unitNumber.setText(unitNo);
        intro.setText(description);

        topicsRecyclerView = view.findViewById(R.id.topics_recyclerView);
        //testAdapter = new TestAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        topicsRecyclerView.setLayoutManager(linearLayoutManager);
        topicsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

   private void changeStatusBarColor(int color) {
        Window window = getActivity().getWindow();
        //int statusBarColor = Color.parseColor(color);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);

    }
    private void getCurrentTopic(){
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("Current_Topic")
                .child(subject)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            CurrentTopic currentTopic = dataSnapshot.getValue(CurrentTopic.class);
                            currentTopicID = currentTopic.getId();
                        }else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
    private void setupAdapter() {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child("TOPICS")
                .child(LUCIDITY_APPLICATION.standard)
                .child(subject)
                .orderByChild("unitID")
                .equalTo(unitID);
        FirebaseRecyclerOptions<Topic> options =  new FirebaseRecyclerOptions.Builder<Topic>()
                .setQuery(query, Topic.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Topic, TopicHolder>(options) {
            @Override
            public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topics_item_layout, parent, false);

                return new TopicHolder(view);
            }
            @Override
            protected void onBindViewHolder(final TopicHolder holder, int position, Topic model) {
                holder.bindData(model,getActivity());
                holder.setOnItemClickListner(new TopicHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int currentposition, String caller) {
                        if(caller.matches("overflow")){
                            firebaseRecyclerAdapter.notifyItemChanged(currentposition);
                        }else {
                            Topic c_topic =getItem(currentposition);
                            Intent intent = new Intent(getActivity(), ContentActivity.class);
                            intent.putExtra("UNIT_NO",c_topic.getUnitNo());
                            intent.putExtra("UNIT_ID",c_topic.getUnitID());
                            intent.putExtra("UNIT_NAME",c_topic.getUnitName());
                            intent.putExtra("TOPIC_ID",c_topic.getId());
                            intent.putExtra("TOPIC_NAME",c_topic.getName());
                            intent.putExtra("VIDEO_ID",c_topic.getVideoID());
                            intent.putExtra("VIDEO_DURATION",c_topic.getVideoDuration());
                            intent.putExtra("SUBJECT",subject);
                            intent.putExtra("CALLER","TOPIC");
                            getActivity().startActivity(intent);

                        }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTopicSelection(uri);
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
        void onTopicSelection(Uri uri);
    }
}
