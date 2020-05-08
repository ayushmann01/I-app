package com.example.i_app.ui.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_app.R;
import com.example.i_app.data.DownModel;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Notes notesFragment;
    ArrayList<DownModel> downModels;

    public MyAdapter(Notes notesFragment, ArrayList<DownModel> downModels) {
        this.notesFragment = notesFragment;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(notesFragment.getContext());
        View view = layoutInflater.inflate(R.layout.notes_list,null,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.pdf_name.setText( downModels.get(position).getPdfName() );
        holder.uploader.setText( downModels.get(position).getUploader() );
        holder.button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFiles(holder.pdf_name.getContext(),downModels.get(position).getPdfName(),".pdf",
                        DIRECTORY_DOWNLOADS, downModels.get(position).getLink());
            }
        });

    }

    public void downloadFiles(Context context,String filename, String fileExtension
            , String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,filename + fileExtension);

        downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
