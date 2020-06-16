package com.nexm.lucidity.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.MainActivity;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Test;
import com.nexm.lucidity.viewHolders.TestHolder;

public class NotificationsFragment extends Fragment {

    private RecyclerView testsRecyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        testsRecyclerView = root.findViewById(R.id.tests_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        testsRecyclerView.setLayoutManager(manager);
        testsRecyclerView.setAdapter(firebaseRecyclerAdapter);

        return root;
    }
    private void setupAdapter() {
        final Query query = LUCIDITY_APPLICATION.root_reference
                .child("TESTS")
                .child(LUCIDITY_APPLICATION.standard);

        FirebaseRecyclerOptions<Test> options =  new FirebaseRecyclerOptions.Builder<Test>()
                .setQuery(query, Test.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Test, TestHolder>(options) {
            @Override
            public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.test_layout, parent, false);

                return new TestHolder(view);
            }
            @Override
            protected void onBindViewHolder(final TestHolder holder, int position, Test model) {
                holder.bindData(model,getActivity());
                holder.setOnItemClickListner(new TestHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int currentposition) {

                            Test selectedTest =getItem(currentposition);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("UNIT_NO",selectedTest.getUnitNo());
                            intent.putExtra("UNIT_NAME",selectedTest.getUnit());
                            intent.putExtra("TEST_ID",selectedTest.getId());
                            intent.putExtra("CALLER","TESTS");
                            intent.putExtra("TEST_MARKS",selectedTest.getMarks());
                            intent.putExtra("TEST_TIME",selectedTest.getTime());
                            intent.putExtra("SUBJECT",selectedTest.getSubject());
                            intent.putExtra("DESC",selectedTest.getDescription());
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setupAdapter();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}