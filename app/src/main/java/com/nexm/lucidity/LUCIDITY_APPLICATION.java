package com.nexm.lucidity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ComplexColorCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LUCIDITY_APPLICATION extends Application {

    public static String studentID,studentName,standard;
    public static boolean paid;
    public static DatabaseReference root_reference;

    public static void updateProgress(String video,String topicID,int percentage) {
        LUCIDITY_APPLICATION.root_reference.child("Progress")
                .child(LUCIDITY_APPLICATION.studentID)
                .child(topicID)
                .child(video)
                .setValue(percentage)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("PROGRESS",e.getMessage());
            }
        });
    }


    //  public static Typeface typeface;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
       // MobileAds.initialize(this, "ca-app-pub-6219444241621852~3964680149");

        // typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        root_reference = database.getReference();
        root_reference.keepSynced(true);

        super.onCreate();
    }

    public static int darker (int color,float factor){
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                (int)Math.max((int)r*factor,0),
                (int)Math.max((int)g*factor,0),
                (int)Math.max((int)b*factor,0));
    }




}
