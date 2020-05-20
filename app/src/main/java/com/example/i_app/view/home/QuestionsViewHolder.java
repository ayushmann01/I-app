package com.example.i_app.view.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_app.R;

public class QuestionsViewHolder extends RecyclerView.ViewHolder {
     TextView text_question;
     TextView text_uploader;
     CardView card_answer;
     CardView card_see_answer;

    public QuestionsViewHolder(@NonNull View itemView) {
        super(itemView);

        text_question = itemView.findViewById(R.id.question);
        text_uploader = itemView.findViewById(R.id.text_uploader);
        card_answer  = itemView.findViewById(R.id.card_answer);
        card_see_answer = itemView.findViewById(R.id.card_see_answer);

    }
}
