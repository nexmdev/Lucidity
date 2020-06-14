package com.nexm.lucidity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.PaperQuestion;
import com.nexm.lucidity.viewHolders.PaperQHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaperFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaperFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";

    // TODO: Rename and change types of parameters
    private String testID,marks,time,unitNo,unitName,subkect,desc;
    private String mParam2;
    private TextView header1,header2,marksView,timeView,noTestView;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private OnFragmentInteractionListener mListener;

    public PaperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param marks Parameter 2.
     * @param time
     * @param subject
     * @param unit
     * @param unitNo
     * @return A new instance of fragment PaperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaperFragment newInstance(String id, String marks, String time, String subject, String unit, String unitNo,String desc) {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, marks);
        args.putString(ARG_PARAM3, time);
        args.putString(ARG_PARAM4, subject);
        args.putString(ARG_PARAM5, unit);
        args.putString(ARG_PARAM6, unitNo);
        args.putString(ARG_PARAM7, desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testID = getArguments().getString(ARG_PARAM1);
            marks = getArguments().getString(ARG_PARAM2);
            time = getArguments().getString(ARG_PARAM3);
            subkect = getArguments().getString(ARG_PARAM4);
            unitName = getArguments().getString(ARG_PARAM5);
            unitNo = getArguments().getString(ARG_PARAM6);
            desc = getArguments().getString(ARG_PARAM7);
        }
        setupAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_paper, container, false);
        header1 = root.findViewById(R.id.paper_header);
        header2 = root.findViewById(R.id.paper_desc);
        marksView = root.findViewById(R.id.paper_marks);
        timeView = root.findViewById(R.id.paper_time);
        noTestView = root.findViewById(R.id.paper_no_testsView);
        noTestView.setVisibility(View.GONE);
        header1.setText(subkect+"/Unit. "+unitNo+"/"+unitName);
        header2.setText(desc);
        marksView.setText("Marks : "+marks);
        timeView.setText("Remaing time : "+ time+" min.");

        recyclerView = root.findViewById(R.id.pape_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPaperselection(uri);
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
    private void setupAdapter() {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child("TEST_QUESTIONS")
                .child(testID);

        FirebaseRecyclerOptions<PaperQuestion> options =  new FirebaseRecyclerOptions.Builder<PaperQuestion>()
                .setQuery(query, PaperQuestion.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PaperQuestion, PaperQHolder>(options) {
            @Override
            public PaperQHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paper_question_layout, parent, false);

                return new PaperQHolder(view);
            }
            @Override
            protected void onBindViewHolder(final PaperQHolder holder, int position, PaperQuestion model) {
                holder.bindData(model,getActivity());
                holder.setOnItemClickListner(new PaperQHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int currentposition) {





                    }
                });
            }
            @Override
            public void onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                if(firebaseRecyclerAdapter.getItemCount()<= 0){
                    noTestView.setVisibility(View.GONE);
                }
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
        void onPaperselection(Uri uri);
    }
}
