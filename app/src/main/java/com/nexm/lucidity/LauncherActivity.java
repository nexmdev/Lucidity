package com.nexm.lucidity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nexm.lucidity.fragments.SignInFragment;
import com.nexm.lucidity.fragments.SignupFragment;

import java.util.HashMap;
import java.util.Objects;

public class LauncherActivity extends AppCompatActivity implements
            SignInFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener

{
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    public static final String VERSION_CODE_KEY = "latest_app_version";
    private static final String TAG = "MainActivity";
    private ImageView image;
    private ProgressBar progressBar;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getSupportActionBar().hide();
        progressBar = findViewById(R.id.launcher_progressbar);
        image = findViewById(R.id.launcher_imageView);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LUCIDITY_RUNNING_STATUS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean run = sharedPreferences.getBoolean("FIRST", true);

        if(run){
            editor.putBoolean("FIRST", false);
            editor.apply();
            showPrivacyPolicy();
        }
        checkForUpdate();


    }
    private void showPrivacyPolicy() {
        final Dialog dialog;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.privacy_layout);

        final TextView exit = (dialog).findViewById(R.id.privacy_exit);
        final TextView agree = (dialog).findViewById(R.id.privacy_agree);
        final TextView showPolicy = (dialog).findViewById(R.id.privacy_see);

        showPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("FIRST", true);
                editor.apply();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lucidity-6e2e0.firebaseapp.com/privacy.html"));
                String title = "Open page Using";
                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("FIRST", true);
                editor.apply();
                finish();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void checkForUpdate() {
        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(VERSION_CODE_KEY, getCurrentVersionCode());
        mFirebaseRemoteConfig.setDefaults(firebaseDefaultMap);

        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder()
                        .build());
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    //Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
                    //calling function to check if new version is available or not
                    checkForUpdateAvailability();
                } else {
                        image.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.launcher_frame_layout,new SignInFragment())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                .commit();

                }
            }
        });

       // Log.d(TAG, "Default value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
    }
    private void checkForUpdateAvailability() {
        int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);
        if (latestAppVersion > getCurrentVersionCode()) {
            new AlertDialog.Builder(this).setTitle("Please Update the App")
                    .setMessage("A new version of this app is available. Please update it").setPositiveButton(
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.nexm.lucidity")));
                            dialog.dismiss();
                        }
                    }).
                    setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            image.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.launcher_frame_layout,new SignInFragment())
                                    .commit();
                        }
                    }).setCancelable(false).show();
        } else {
           // Toast.makeText(this,"This app is already up to date", Toast.LENGTH_SHORT).show();
            image.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.launcher_frame_layout,new SignInFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();

        }
    }

    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void onSignInSelection() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.launcher_frame_layout,new SignupFragment())
                .addToBackStack("Sign in")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }

    @Override
    public void signUpComplete() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.launcher_frame_layout,new SignInFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();

    }
}
