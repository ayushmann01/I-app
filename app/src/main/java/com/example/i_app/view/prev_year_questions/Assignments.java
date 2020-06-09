package com.example.i_app.view.prev_year_questions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.i_app.controller.MainActivity;
import com.example.i_app.R;

import java.util.Objects;

public class Assignments extends Fragment {
    private ListView subList;
    private String subjects[] = {"Operating Systems", "Data Structures", "Theory of Computation",
                                    "Mathematics-3", "Discrete Mathematics"};
    private String subId[] = {"O.S", "D.S", "T.C", "M.3", "D.M"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assignments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Assignments");
        MainActivity.navigationView.setCheckedItem(R.id.nav_assignment);

        subList = view.findViewById(R.id.sub_list);

        MyAdapter adapter = new MyAdapter(getContext(),subId,subjects);
        subList.setAdapter(adapter);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.add_pdf);
        item.setVisible(false);
        //super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
    }

    static class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String sub_id[];
        String sub_name[];

        public MyAdapter(Context context, String[] sub_id, String[] sub_name){
            super(context, R.layout.subjects_view, R.id.sub_name, sub_name);
            this.context = context;
            this.sub_id = sub_id;
            this.sub_name = sub_name;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.subjects_view, parent, false);
            TextView subId = row.findViewById(R.id.sub_id);
            TextView subName = row.findViewById(R.id.sub_name);

            subId.setText(sub_id[position]);
            subName.setText(sub_name[position]);


            return row;
        }
    }
}
