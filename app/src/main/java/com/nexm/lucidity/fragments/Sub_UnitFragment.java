package com.nexm.lucidity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Subject;
import com.nexm.lucidity.viewHolders.SubjectHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Sub_UnitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Sub_UnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sub_UnitFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private OnFragmentInteractionListener mListener;

    public Sub_UnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sub_UnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Sub_UnitFragment newInstance(String param1, String param2) {
        Sub_UnitFragment fragment = new Sub_UnitFragment();
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
        View view = inflater.inflate(R.layout.fragment_sub__unit, container, false);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));

        final ImageView title = view.findViewById(R.id.sub_unit_title);
        title.setImageResource(R.drawable.main_banner);
        view.findViewById(R.id.unit_title).setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.sub_unit_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String id) {
        if (mListener != null) {
            mListener.onSubUnitSelection(id,"","" );
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
        setupAdapter("SUBJECTS","STD-12-SCI");
    }

    private void setupAdapter(String child1 , String child2) {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child(child1)
                .child(child2);
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
                            mListener.onSubUnitSelection(getItem(currentposition).getId(),
                                    getItem(currentposition).getName(),
                                   "#ffffff");
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onSubUnitSelection(String id, String subName, String color);
    }
}
