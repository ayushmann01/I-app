package com.example.i_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.i_app.R;
import com.example.i_app.model.Database;
import com.example.i_app.view.auth.Login;
import com.example.i_app.view.prev_year_questions.Assignments;
import com.example.i_app.view.home.Home;
import com.example.i_app.view.notes.Notes;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    public static NavigationView navigationView;
    private Fragment fragment;
    private TextView text_username;
    private   ImageView userImage;
    public static CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***************************HOOKS*****************************/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        /**************************Tool Bar**************************/
        setSupportActionBar(toolbar);

        /*******************Navigation Drawer Menu*******************/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        /***********************Home Fragment************************/
        fragment = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_view, fragment, "home")
                .addToBackStack("home").commit();

        // navigationView.setCheckedItem(R.id.nav_home);
        /***********************NavigationVew Header**********************/

        View header_view = navigationView.inflateHeaderView(R.layout.header);

        text_username =  header_view.findViewById(R.id.text_username);
        userImage = header_view.findViewById(R.id.image_profile);

        try {
            final Database database = new Database();
            DocumentReference docRef = database.getUserData();
            docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    try {
                        assert documentSnapshot != null;
                        String username  =  documentSnapshot.getString("Name");
                        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

                        text_username.setText(username);
                        database.setProfilePic(userImage);

                        currentUser = new CurrentUser(username,userImage,userEmail);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            FragmentManager fg = getSupportFragmentManager();
//            FragmentTransaction ft = fg.beginTransaction();
//
//            ft.remove(fragment).commit();

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else super.onBackPressed();
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = null;
        String tag = null;

        switch (item.getItemId()) {
            case R.id.nav_home: {
                fragment = new Home();
                tag = "Home";
                break;
            }
            case R.id.nav_notes: {
                fragment = new Notes();
                tag = "notes";
                break;
            }
            case R.id.nav_assignment: {
                fragment = new Assignments();
                tag = "assignments";
                break;
            }
            case R.id.nav_logout: {
                FirebaseAuth.getInstance().signOut();
                //FirebaseAuth.GoogleSignInApi.signOut(auth);
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_share: {
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_rate: {
                Toast.makeText(this, "High Rated", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (fragment != null & tag != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen_view, fragment, tag);
            fragmentTransaction.addToBackStack(tag).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fragments_menu,menu);
        return true;
    }
}
