package com.example.i_app.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView text_editImage;
    private ImageView profileImage;
    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = new Database();

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        text_editImage = findViewById(R.id.text_editImage);
        profileImage = findViewById(R.id.image_profile);


        text_editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });
        showUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
                database.uploadProfilePic(imageUri);
            }
        }
    }

    public void showUser() {
        try {
            docRef = database.getUserData();
            docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    textName.setText(documentSnapshot.getString("Name"));
                    textEmail.setText(documentSnapshot.getString("Email"));
                    database.getProfilePic(profileImage);
                }
            });
        } catch (FirebaseFirestoreException f) {
            Log.d("Error", "" + f);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
