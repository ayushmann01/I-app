package com.example.i_app.view.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.i_app.R;

public class Answers extends AppCompatActivity {
    TextView text_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        text_question = findViewById(R.id.text_question);
        text_question.setText(QnA.question);
    }
}
