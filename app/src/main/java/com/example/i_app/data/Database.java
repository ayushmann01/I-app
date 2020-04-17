package com.example.i_app.data;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Database {
    private  FirebaseFirestore firestoreDB;
    private  FirebaseAuth auth;
    private  StorageReference storage;
    private  String currentUserId;

    public Database(){
        firestoreDB = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDB.setFirestoreSettings(settings);

        storage = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
    }

    public  void registereUser(final String userId, final String name, final String email) {
        try {
            DocumentReference document = firestoreDB.collection("users").document(userId);
            Map<String, Object> users = new HashMap<>();
            users.put("Name", name);
            users.put("Email", email);
            document.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "User profie created for " + userId);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Cannot create user profile create" + e.toString());
                }
            });
        } catch (Exception f) {
            return;
        }
    }

    public  DocumentReference getUserData() throws FirebaseFirestoreException {
        DocumentReference docRef = firestoreDB.collection("users").document(currentUserId);
        return docRef;
    }

    public void uploadProfilePic(Uri imageUri) {
        StorageReference file = storage.child("users/" + currentUserId +"/profile.jpg");

        file.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("image upload", "Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("image upload", "Failed");
            }
        });
    }

    public void getProfilePic(final ImageView profileImage) {
        StorageReference profileRef = storage.child("users/" + currentUserId + "/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
                Log.d("set profile", "Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("set profile", "Failed");
            }
        });
    }
}
