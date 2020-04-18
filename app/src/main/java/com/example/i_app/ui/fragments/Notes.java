package com.example.i_app.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.i_app.MainActivity;
import com.example.i_app.R;
import com.example.i_app.data.Database;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;

import io.grpc.Context;

import static com.example.i_app.MainActivity.navigationView;

public class Notes extends Fragment {

    private String noteName;
    private EditText text_noteName;
    private StorageReference storageReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView.setCheckedItem(R.id.nav_notes);

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference noteRef = storageReference.child("notes/");
        String note = noteRef.getDownloadUrl().toString();


         text_noteName = view.findViewById(R.id.editText);

        view.findViewById(R.id.button_uploadNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteName = text_noteName.getText().toString();
                if (noteName.isEmpty()){
                    text_noteName.setError("Enter a valid name");
                    return;
                }else text_noteName.setError(null);

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 2000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK && data != null) {
            final Uri noteUri = data.getData();

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.Theme_AppCompat_Light_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading");
            progressDialog.show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Database().uploadNotes(noteUri, noteName);
                    Toast.makeText(getActivity(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }, 1000);
        }
      text_noteName.setText(null);
    }
}
