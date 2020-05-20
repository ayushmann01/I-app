package com.example.i_app.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.i_app.controller.MainActivity;
import com.example.i_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.navigationView.setCheckedItem(R.id.nav_home);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(getActivity(),R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.add_pdf){
           Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(getActivity(), AskQuestion.class);
           startActivity(intent);
       }
        return super.onOptionsItemSelected(item);
    }

}
