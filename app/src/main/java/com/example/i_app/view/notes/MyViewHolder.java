package com.example.i_app.view.notes;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.i_app.R;

public class MyViewHolder extends ViewHolder {
    TextView pdf_name;
    TextView uploader;
    Button   button_download;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

       pdf_name = itemView.findViewById(R.id.pdf_name);
       uploader  = itemView.findViewById(R.id.pdf_uploader);
       button_download = itemView.findViewById(R.id.button_download);
    }
}
