package com.example.i_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.i_app.MainActivity;
import com.example.i_app.R;
import com.example.i_app.data.Database;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Profile extends AppCompatActivity {
    private TextView textName;
    private TextView textEmail;
    public static String currentUsername;
    private DocumentReference docRef;
    Database database;


    public String getCurrentUsername() {
           return currentUsername;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textName  = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);

        showUser();
    }

    public void showUser(){
        try {
            docRef = database.getUserData();
            docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    textName.setText(documentSnapshot.getString("Name"));
                    textEmail.setText(documentSnapshot.getString("Email"));
                }
            });
        }catch (FirebaseFirestoreException f){
            Log.d("Error",""+f);
        }
    }

    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
