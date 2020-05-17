package com.example.i_app.view.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_app.R;
import com.example.i_app.model.QuestionDownModel;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsViewHolder> {
    private QnA qnaFragment;
    private ArrayList<QuestionDownModel> downModels;

    public QuestionsAdapter(QnA qnaFragment, ArrayList<QuestionDownModel> downModels) {
        this.qnaFragment = qnaFragment;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(qnaFragment.getContext());
        View view = layoutInflater.inflate(R.layout.questions_view,null,false);

        return new QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position) {

        holder.text_uploader.setText(downModels.get(position).getUploader());
        holder.text_question.setText(downModels.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
