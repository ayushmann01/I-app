package com.example.i_app.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Database {
    private static FirebaseFirestore firestoreDB;
    private static FirebaseAuth auth;

    static {
        firestoreDB = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDB.setFirestoreSettings(settings);
    }

    public static void registereUser(final String userId, final String name, final String email) {
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

    public static DocumentReference getUserData() throws FirebaseFirestoreException {
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        DocumentReference docRef = firestoreDB.collection("users").document(userId);
        return docRef;
    }
}
