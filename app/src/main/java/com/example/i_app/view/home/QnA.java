package com.example.i_app.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.i_app.R;
import com.example.i_app.model.Database;
import com.example.i_app.model.QuestionDownModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class QnA extends Fragment {
    private RecyclerView questionsRecyclerView;
    private ArrayList<QuestionDownModel> questionDownModels = new ArrayList<>();
    private QuestionsAdapter questionsAdapter;
    static public String question;

    public QnA() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qna, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionsRecyclerView = view.findViewById(R.id.recycler_questions);
        questionsRecyclerView.setHasFixedSize(true);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));

        Database database = new Database();

        database.getDb().collection("Questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                            QuestionDownModel downModel = new QuestionDownModel(
                                    documentSnapshot.getString("Uploader"),
                                    documentSnapshot.getString("question")
                            );
                            questionDownModels.add(downModel);
                        }
                        questionsAdapter = new QuestionsAdapter(QnA.this, questionDownModels);
                        questionsRecyclerView.setAdapter(questionsAdapter);
                    }
                });
    }

    public void writeAnswer(String question){
        QnA.question = question;
        startActivity( new Intent(this.getActivity(), Answers.class) );
    }
}
