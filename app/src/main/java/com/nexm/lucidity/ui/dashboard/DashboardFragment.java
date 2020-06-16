package com.nexm.lucidity.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Student;

public class DashboardFragment extends Fragment {

    private TextView name,std,changestd,address,mono,mono2;
    private RadioGroup radioGroup;
    private String newStd;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        name = root.findViewById(R.id.profile_name);
        std = root.findViewById(R.id.profile_std);
        changestd = root.findViewById(R.id.profile_change_std);
        address = root.findViewById(R.id.profile_address);
        mono = root.findViewById(R.id.profile_mono);
        mono2 = root.findViewById(R.id.profile_alt_mono);
        radioGroup = root.findViewById(R.id.profile_radioGroup);
        progressBar = root.findViewById(R.id.profile_progressBar);
        radioGroup.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        name.setText(LUCIDITY_APPLICATION.studentName);
        std.setText(LUCIDITY_APPLICATION.standard);
        getStudent();
        changestd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                if(changestd.getText().toString().matches("Change")){
                    radioGroup.setVisibility(View.VISIBLE);
                    changestd.setText("Save Changes");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = root.findViewById(checkedId);
                    newStd = radioButton.getText().toString();
                    if(newStd.matches(LUCIDITY_APPLICATION.standard)){
                        progressBar.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);
                        changestd.setText("Change");
                        return;

                    }
                    saveStd();
                }

            }
        });
        return root;
    }

    private void saveStd() {
        LUCIDITY_APPLICATION.root_reference
                .child("Students")
                .child(LUCIDITY_APPLICATION.studentID)
                .child("std")
                .setValue(newStd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);
                        changestd.setText("Change");
                        Toast.makeText(getActivity(),"Standard changed successfully",Toast.LENGTH_SHORT).show();
                        std.setText(newStd );
                        LUCIDITY_APPLICATION.standard = newStd;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.GONE);
                        changestd.setText("Change");
                        Toast.makeText(getActivity(),"Standard change unsuccessfull",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getStudent() {
        LUCIDITY_APPLICATION.root_reference
                .child("Students")
                .child(LUCIDITY_APPLICATION.studentID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Student student = dataSnapshot.getValue(Student.class);
                            address.setText(student.getAddress());
                            mono.setText(student.getMoNo());
                            mono2.setText(student.getAltMoNo());
                        }else{
                            Toast.makeText(getActivity(),"Something went wrong , try again 1!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(),"Something went wrong , try again 2 !",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}