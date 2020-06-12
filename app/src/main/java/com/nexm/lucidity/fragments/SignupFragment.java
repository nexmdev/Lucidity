package com.nexm.lucidity.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nexm.lucidity.LUCIDITY_APPLICATION;
import com.nexm.lucidity.R;
import com.nexm.lucidity.models.Student;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private String standard;
    private EditText namefield,addressfield,moNofield,altmoNofield,pinfield1,pinfield2,motherField;
    private String name,address,moNo,moNo2,pin1,pin2,uid,mother;

    private OnFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);
        progressBar=view.findViewById(R.id.signup_progressbar);
        radioGroup = view.findViewById(R.id.profile_radioGroup);
        namefield = view.findViewById(R.id.signup_nameInput);
        addressfield = view.findViewById(R.id.signup_addressInput);
        moNofield = view.findViewById(R.id.signup_monoInput);
        altmoNofield = view.findViewById(R.id.signup_monoAltInput);
        pinfield1 = view.findViewById(R.id.signup_pin1);
        pinfield2 = view.findViewById(R.id.signup_pin2);
        motherField = view.findViewById(R.id.signup_nameInput_mother);
        progressBar.setVisibility(View.GONE);
        final TextView registerButton = view.findViewById(R.id.signup_registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(checkedId);
                standard = radioButton.getText().toString();
                if (!validateProfile()) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                createAc();
               
            }
        });
        return view;
    }

    private void createAc() {

        progressBar.setVisibility(View.VISIBLE);
        String email = moNo;
        email = email+"@domain.com";
        String password = pin2;
        password = password+"@go";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                                uid = user.getUid();
                                Student student = new Student();
                                assignStudent(student);
                                saveStudent(student);
                            }                        

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void saveStudent(Student student) {
        LUCIDITY_APPLICATION.root_reference
                .child("Students")
                .child(uid)
                .setValue(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        showSuccessDialog();
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
    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Oops....!")
                .setMessage("Something went wrong .... Please try again\n\n" +
                        "If problem persists contact Tutor pro customer care(7775971543) ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Success !")
                .setMessage("Your Account is successfully created")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final SharedPreferences preferences = getActivity().getSharedPreferences("STUDENT",MODE_PRIVATE);
                        final SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Name",name);
                        editor.putString("MoNo",moNo);
                        editor.putString("Pin",pin1);
                        editor.apply();

                        if(mListener != null)
                            mListener.signUpComplete();
                    }
                });
        builder.show();
    }
    private void assignStudent(Student student) {
        Date date = new Date();
        long today = date.getTime();
        student.setDate(today);
        student.setAddress(address);
        student.setAltMoNo(moNo2);
        student.setMoNo(moNo);
        student.setName(name);
        student.setStd(standard);
        student.setMotherName(mother);
    }

    private boolean validateProfile() {
        boolean valid = true;
        name = namefield.getText().toString();
        if (TextUtils.isEmpty(name)) {
            namefield.setError("Enter name.");
            valid = false;
        } else {
            namefield.setError(null);
        }
        mother = motherField.getText().toString();
        if (TextUtils.isEmpty(mother)) {
            motherField.setError("Enter mother's name.");
            valid = false;
        } else {
            motherField.setError(null);
        }
        address = addressfield.getText().toString();
        if (TextUtils.isEmpty(address)) {
            addressfield.setError("Enter address.");
            valid = false;
        } else {
            addressfield.setError(null);
        }

        moNo = moNofield.getText().toString();
        if(moNo != null)moNo.trim();
        if (TextUtils.isEmpty(moNo) || moNo.length()!=10) {
            moNofield.setError("Enter proper mobile number.");
            valid = false;
        } else {
            moNofield.setError(null);
        }
        pin1 = pinfield1.getText().toString();
        if(pin1 != null)pin1.trim();
        if (TextUtils.isEmpty(pin1) || pin1.length()!=4) {
            pinfield1.setError("Enter proper 4 digit pin.");
            valid = false;
        } else {
            pinfield1.setError(null);
        }
        pin2 = pinfield2.getText().toString();
        if(pin2 != null)pin2.trim();
        if (TextUtils.isEmpty(pin2) || pin2.length()!=4 || !pin1.matches(pin2)) {
            pinfield2.setError("Pin does not match.");
            valid = false;
        } else {
            pinfield2.setError(null);
        }
        moNo2 = altmoNofield.getText().toString();
        if(moNo2 != null)moNo2.trim();
        if (TextUtils.isEmpty(moNo2) || moNo2.length()!=10) {
            altmoNofield.setError("Enter proper mobile number.");
            valid = false;
        } else {
            altmoNofield.setError(null);
        }

        return valid;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.signUpComplete();
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
        void signUpComplete();
    }
}
