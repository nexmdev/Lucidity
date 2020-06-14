package com.nexm.lucidity.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexm.lucidity.BottomNavigationActivity;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Student;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView signup,nameView,forgotPin;
    private EditText pinField,mobileField;
    private TextView signinButton;
    private  String name,moNo,moNoForpreferences,sellerID;
    private FirebaseAuth auth ;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        auth = FirebaseAuth.getInstance();
          preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("STUDENT",MODE_PRIVATE);
          editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
       // getActivity().setTitle("Sign in");
        signup = view.findViewById(R.id.signin_signUp);
        nameView = view.findViewById(R.id.signin_emailDisplay);
        pinField = view.findViewById(R.id.signin_pinInput);
        signinButton = view.findViewById(R.id.signin_submitButton);
        mobileField = view.findViewById(R.id.signin_mobileInput);
        progressBar = view.findViewById(R.id.signinProgressbar);
        forgotPin = view.findViewById(R.id.signin_forgotPin);
        ShowName();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onSignInSelection();
            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String pin = pinField.getText().toString();
                moNo = mobileField.getText().toString();
                pin = pin.trim();
                if(pin.length() != 4){
                    pinField.setError("Enter proper pin");
                    progressBar.setVisibility(View.GONE);
                    return;
                }else if(moNo.length()!=10){
                    mobileField.setError("Enter proper mobile number");
                    progressBar.setVisibility(View.GONE);
                    return;
                }else{
                    moNoForpreferences = moNo;
                    moNo= moNo+"@domain.com";
                    pin = pin+"@go";
                    auth.signInWithEmailAndPassword(moNo,pin)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    sellerID = user.getUid();
                                    //showSuccessDialog();
                                   updateSharedPreferences();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    showFailureDialog();
                                }
                            });
                }
            }
        });
        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showresetPinDialog();
            }
        });
        return view;
    }

    private void showresetPinDialog() {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pin_reset_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText mother = dialog.findViewById(R.id.reset_mother);
        final TextView submitMother = dialog.findViewById(R.id.reset_submit_mother);
        final TextView revealPin = dialog.findViewById(R.id.reset_pin);
        revealPin.setVisibility(View.GONE);
        submitMother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String motherName = mother.getText().toString();
                if (TextUtils.isEmpty(motherName)) {
                    mother.setError("Enter mother name.");
                    return;
                } else {
                    mother.setError(null);
                    if(motherName.equalsIgnoreCase(preferences.getString("Mother","x"))){
                        revealPin.setText("Your pin : "+preferences.getString("Pin","0000"));
                        revealPin.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(getActivity(),"Mother's name does not match",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Oops....!")
                .setMessage("Mobile number or pin is incorrect\n" +
                        "If problem persists contact Tutor pro customer care(7775971543) ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void updateSharedPreferences() {
        FirebaseDatabase.getInstance().getReference()
                .child("Students")
                .child(sellerID)

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Student student = dataSnapshot.getValue(Student.class);
                            name = student.getName();
                            LUCIDITY_APPLICATION.studentName = name;
                            LUCIDITY_APPLICATION.studentID = sellerID;
                            LUCIDITY_APPLICATION.standard = student.getStd();
                            LUCIDITY_APPLICATION.paid = student.isPaid();
                            editor.putString("Name",name);
                            editor.putString("Mother",student.getMotherName());
                            editor.putString("MoNo",moNoForpreferences);
                            editor.apply();
                            if(student.isPaid() ||checkValidity(student.getDate())){
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                showTrialExpiredDialog();
                            }


                        }
                       
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private boolean checkValidity(long date) {
        boolean valid = false;
        long today = System.currentTimeMillis();
        long daysPassed = today-date;
        long trialPeriod = 20*24*60*60*1000;
        if(daysPassed <= trialPeriod)valid=true;
        return valid;
    }

    private void showTrialExpiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Trial Period Over !")
                .setMessage("Your free trial period of 8 days is over , to continue using app contact OM Coaching classes (Mo. no. 7775971543)")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().finish();

                    }
                });
        builder.setCancelable(false);
        builder.show();
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSignInSelection();
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
    private void ShowName(){

        name = preferences.getString("Name",null);
        moNo = preferences.getString("MoNo",null);
        if(name != null){
            signup.setVisibility(View.GONE);
            nameView.setVisibility(View.VISIBLE);
            nameView.setText(name);
            mobileField.setText(moNo);
        }else{
            nameView.setText("");
            signup.setVisibility(View.VISIBLE);
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
        void onSignInSelection();
    }
}
