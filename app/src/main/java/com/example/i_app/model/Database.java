package com.example.i_app.model;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.i_app.controller.MainActivity;
import com.example.i_app.view.notes.Notes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Database {
    private FirebaseFirestore firestoreDB;
    private StorageReference storage;
    private String currentUserId;
    public Result upload_result = new Result(false);

    public Database() {
        firestoreDB = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDB.setFirestoreSettings(settings);

        storage = FirebaseStorage.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public FirebaseFirestore getDb() {
        return firestoreDB;
    }

    public void registerUser(final String userId, final String name, final String email) {
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

    }

    public DocumentReference getUserData() {
        return firestoreDB.collection("users").document(currentUserId);
    }

    public void uploadProfilePic(Uri imageUri) {
        StorageReference file = storage.child("users/" + currentUserId + "/profile.jpg");

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

    public void setProfilePic(final ImageView profileImage) {
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

    public void uploadNotes(Uri noteUri, final String noteName) {
        final StorageReference notesRef = storage.child("notes/" + noteName + ".pdf");
        final CollectionReference collectionReference = firestoreDB.collection("Notes");


        notesRef.putFile(noteUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    upload_result.setSuccess(true);
                }

                notesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Name", noteName);
                        map.put("Url", uri.toString());
                        map.put("Uploader", MainActivity.currentUser.getUsername());

                        collectionReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("URL upload", "Success");
                                Notes.progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("URL upload", "Failed");
                                upload_result.setSuccess(false);
                            }
                        });
                    }
                });
            }
        });
    }

    public Task<Void> uploadQuestion(String question) {
        DocumentReference questionDocument = firestoreDB.collection("Questions").document(question);

        HashMap<String, String> file = new HashMap<>();
        file.put("question", question);
        file.put("Uploader", MainActivity.currentUser.getUsername());

        return questionDocument.set(file);
    }

    public Task<Void> uploadAnswer(String question, String answer) {
        DocumentReference quesRef = firestoreDB.collection("Questions").document(question);

        Map<String, Object> ans = new HashMap<>();
        ans.put("answer", answer);

        return quesRef.update(ans);
    }
}
