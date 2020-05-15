package com.example.i_app.view.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_app.R;
import com.example.i_app.model.Database;
import com.example.i_app.model.DownModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import static com.example.i_app.controller.MainActivity.navigationView;

public class Notes extends Fragment {
    private StorageReference storageReference;
    private RecyclerView notesRecyclerView;
    private MyAdapter myAdapter;
    private ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    private Button button_upload;
    private EditText text_noteName;
    private String noteName;
    private Database database;
    public static ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView.setCheckedItem(R.id.nav_notes);


        progressDialog = new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Light_Dialog);

        /*******************Setting up RecycleView********************************/
        notesRecyclerView = view.findViewById(R.id.recycle);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));


        database = new Database();

        database.getDb().collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot documentSnapshot: task.getResult()){
                            DownModel downModel = new DownModel(documentSnapshot.getString("Name"),
                                    documentSnapshot.getString("Url"), documentSnapshot.getString("Uploader") );
                            downModelArrayList.add(downModel);
                        }

                        myAdapter = new MyAdapter(Notes.this,downModelArrayList);
                        notesRecyclerView.setAdapter(myAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //inflater.inflate(R.menu.fragments_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_pdf){
           // Toast.makeText(getContext(),"button clicked",Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View addView = getLayoutInflater().inflate(R.layout.custom_adddialog,null);

                button_upload = addView.findViewById(R.id.button_upload);
                text_noteName = addView.findViewById(R.id.notes_name);

                alert.setView(addView);

                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);

                button_upload.setOnClickListener(new View.OnClickListener() {
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
                return  true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK && data != null) {
            final Uri noteUri = data.getData();

            database.uploadNotes(noteUri, noteName);

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();


                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(database.upload_result.isSuccess()) {
                            Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 30*1000);

        }
       // text_noteName.setText(null);
    }
}
