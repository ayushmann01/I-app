package com.example.i_app.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.i_app.controller.MainActivity;
import com.example.i_app.R;
import com.example.i_app.model.Database;
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
    private ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating");
        progressDialog.show();

        showUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {

                progressDialog.show();

                Uri imageUri = data.getData();
                database.uploadProfilePic(imageUri);

                profileImage.setImageURI(imageUri);
                MainActivity.currentUser.getUserImage().setImageURI(imageUri);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Profile picture updated", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }, 3000);
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
                    database.setProfilePic(profileImage);

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 2000);
                }
            });
        } catch (Exception f) {
            Log.d("Error", "" + f);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
}
