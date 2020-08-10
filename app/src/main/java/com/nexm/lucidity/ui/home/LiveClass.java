package com.nexm.lucidity.ui.home;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.ClassActivity;
import com.nexm.lucidity.ContentActivity;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.MainActivity;
import com.nexm.lucidity.R;
import com.nexm.lucidity.fragments.TodayClassFragment;
import com.nexm.lucidity.viewHolders.LiveHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveClass.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveClass extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ImageView info;
    private TextView ticker,month,date,holidayView;
    private String todayDateString;

    private OnFragmentInteractionListener mListener;

    public LiveClass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveClass.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveClass newInstance(String param1, String param2) {
        LiveClass fragment = new LiveClass();
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
        View root = inflater.inflate(R.layout.fragment_live_class, container, false);
        info = root.findViewById(R.id.live_info);
        ticker =root.findViewById(R.id.live_ticker);
        ticker.setSelected(true);
        fetchTicker();
        month = root.findViewById(R.id.live_month);
        date = root.findViewById(R.id.live_date);
        holidayView = root.findViewById(R.id.live_holidayView);
        setDate();
        recyclerView = root.findViewById(R.id.live_recycler);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("CALLER","About");
                getActivity().startActivity(intent);
            }
        });
        final FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton);
        if(LUCIDITY_APPLICATION.studentName.matches("Om Classes Admin")){
            floatingActionButton.show();
        }else{
            floatingActionButton.hide();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }

    private void setDate() {

        Calendar calendar = Calendar.getInstance();
        int todaydate = calendar.get(Calendar.DATE);
        date.setText(String.valueOf(todaydate));
        int currentmonth = calendar.get(Calendar.MONTH);
        switch (currentmonth){
            case 0: month.setText("JANUARY");break;
            case 1: month.setText("FEBRUARY");break;
            case 2: month.setText("MARCH");break;
            case 3: month.setText("APRIL");break;
            case 4: month.setText("MAY");break;
            case 5: month.setText("JUNE");break;
            case 6: month.setText("JULY");break;
            case 7: month.setText("AUGUST");break;
            case 8: month.setText("SEPTEMBER");break;
            case 9: month.setText("OCTOBER");break;
            case 10: month.setText("NOVEMBER");break;
            case 11: month.setText("DECEMBER");break;
        }

    }

    private void fetchTicker() {
        LUCIDITY_APPLICATION.root_reference
                .child("TICKER")
                .child(LUCIDITY_APPLICATION.standard)
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLiveselection(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TodayClassFragment.OnFragmentInteractionListener) {
            mListener = (LiveClass.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        setupAdapter("LiveClass", LUCIDITY_APPLICATION.standard);
    }

    private void setupAdapter(String child1 , String child2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayDateString = formatter.format(new Date());
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child(child1)
                .child(child2)
                .orderByChild("date")
                .equalTo(todayDateString);
        FirebaseRecyclerOptions<com.nexm.lucidity.models.LiveClass> options =  new FirebaseRecyclerOptions.Builder<com.nexm.lucidity.models.LiveClass>()
                .setQuery(query, com.nexm.lucidity.models.LiveClass.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<com.nexm.lucidity.models.LiveClass, LiveHolder>(options) {
            @Override
            public LiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.live_recycler_item, parent, false);

                return new LiveHolder(view);
            }
            @Override
            protected void onBindViewHolder(LiveHolder holder, int position, final com.nexm.lucidity.models.LiveClass model) {
                holder.bindData(model,getActivity());
                holder.setOnItemClickListner(new LiveHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        com.nexm.lucidity.models.LiveClass today = getItem(position);

                        checkTime(today,position);


                    }
                });
            }
            @Override
            public void onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                if(firebaseRecyclerAdapter.getItemCount()<=0){
                    holidayView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }else{
                    holidayView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
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

    private void checkTime(com.nexm.lucidity.models.LiveClass today,int position) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String current = sdf.format(c.getTime());

        if(current.compareTo(today.getStime())<0 ){
            Toast.makeText(getActivity(), "Class will start at : "+today.getStime()+" am", Toast.LENGTH_SHORT).show();
        }else if(current.compareTo(today.getStime())==0){
            firebaseRecyclerAdapter.notifyItemChanged(position);
        }
        else {


            Intent intent = new Intent(getActivity(), ContentActivity.class);
            intent.putExtra("UNIT_NO",today.getUnitNo());
            intent.putExtra("UNIT_ID",today.getUnitID());
            intent.putExtra("UNIT_NAME",today.getUnitName());
            intent.putExtra("TOPIC_ID",today.getId());
            intent.putExtra("TOPIC_NAME",today.getName());
            intent.putExtra("VIDEO_ID",today.getVideoID());
            intent.putExtra("VIDEO_DURATION",today.getVideoDuration());
            intent.putExtra("SUBJECT",today.getSubject());
            intent.putExtra("CALLER","LIVE");
            getActivity().startActivity(intent);


        }
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
        void onLiveselection(Uri uri);
    }
}
