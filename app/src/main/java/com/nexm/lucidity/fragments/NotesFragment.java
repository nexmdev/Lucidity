package com.nexm.lucidity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.NewZoomableImageView;
import com.nexm.lucidity.models.Notes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String topic_id;
    private String mParam2;
    private TextView text,counter,noNotesContinue;
    private ImageView previous,next;
    private NewZoomableImageView image;
    private ArrayList<Notes> notes = new ArrayList<>();
    private int index=0;
    private ProgressBar progressBar;
    private OnFragmentInteractionListener mListener;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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
            topic_id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getNotes();
    }

    private void getNotes() {

        notes.clear();
        LUCIDITY_APPLICATION.root_reference
                .child("NOTES")
                .child(topic_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                Notes note = childSnapshot.getValue(Notes.class);
                                notes.add(note);
                            }

                            setNote();
                            noNotesContinue.setVisibility(View.GONE);
                            previous.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE);
                        }else{

                            showNoNotes();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showNoNotes() {
        text.setVisibility(View.VISIBLE);
        text.setText("Notes for this topic are in preparation !\nCheck after few days.");
        image.setVisibility(View.INVISIBLE);
        counter.setVisibility(View.INVISIBLE);
        previous.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        noNotesContinue.setVisibility(View.VISIBLE);

    }

    private void setNote() {

            if(notes.get(index).getText()!= null){
                text.setVisibility(View.VISIBLE);
                text.setText(notes.get(index).getText());
            }else{
                text.setVisibility(View.GONE);
            }
            if(notes.get(index).getImageUrl().matches("x")){
                image.setVisibility(View.GONE);

            }else{
                image.setVisibility(View.VISIBLE);
                Glide.with(getActivity())
                        .load(notes.get(index).getImageUrl())
                        .placeholder(R.drawable.ic_action_document)
                        .into(image);
            }
            counter.setText(index+1+" / "+ notes.size());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        text = view.findViewById(R.id.notes_text);
        image = view.findViewById(R.id.notes_image);
        counter = view.findViewById(R.id.notes_counter);
        previous = view.findViewById(R.id.notes_previous);
        noNotesContinue = view.findViewById(R.id.notes_no_notes_continue);
        progressBar = view.findViewById(R.id.notes_progress);
        progressBar.setVisibility(View.GONE);
        next = view.findViewById(R.id.notes_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if(index+1>notes.size()){

                    showTestAlert();

                }else{
                    setNote();
                }

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if(index<0){
                    if(mListener!=null){
                        mListener.onNotesSelection("previous");
                    }
                }else{
                    setNote();
                }

            }
        });
        noNotesContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProgress();
            }
        });
        return view;
    }


    private void updateProgress() {
        progressBar.setVisibility(View.VISIBLE);
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child(topic_id)
                .child("notes")
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
                        if(mListener!=null){

                            mListener.onNotesSelection("next");
                        }
                    }
                });
    }

    private void showTestAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Go To Test ?");
        if(notes.size()>0){
            builder.setMessage("It is advised that you go through notes once again sothat you can answer test easily.");
            builder.setNegativeButton("REVISE NOTES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    index=0;
                    setNote();
                }
            });
        }

        builder.setPositiveButton("GO TO TEST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //if(notes.size()>0) LUCIDITY_APPLICATION.updateProgress("notes",topic_id,30);
                updateProgress();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onNotesSelection(uri);
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
        void onNotesSelection(String page);
    }
}
