package com.example.i_app.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i_app.R;
import com.example.i_app.model.Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class AskQuestion extends AppCompatActivity {
private EditText text_question;
private Button add_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        text_question = findViewById(R.id.text_question);
        add_question = findViewById(R.id.btn_add_question);

        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = text_question.getText().toString();
                if ( question.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Invalid Question",Toast.LENGTH_SHORT).show();
                }else{
                    Database db = new Database();
                    Task<DocumentReference> result = db.uploadQuestion(question);
                    result.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          Toast.makeText(getApplicationContext(),"upload failed",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                          Toast.makeText(getApplicationContext(),"uploaded successfully",Toast.LENGTH_SHORT).show();
                          text_question.setText(null);
                        }
                    });
                }
            }
        });
    }
}
