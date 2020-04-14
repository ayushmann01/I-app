package com.example.i_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.i_app.ui.Login;
import com.example.i_app.ui.home.Assignments;
import com.example.i_app.ui.home.Notes;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /***************************HOOKS*****************************/
       drawerLayout = findViewById(R.id.drawer_layout);
       navigationView = findViewById(R.id.nav_view);
       toolbar = (Toolbar)findViewById(R.id.toolbar);

       /**************************Tool Bar**************************/
       setSupportActionBar(toolbar);

       /**************************Navigation Drawer Menu**************************/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:{
                break;
            }
            case R.id.nav_notes:{
                Intent intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_assignment:{
                Intent intent = new Intent(MainActivity.this, Assignments.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout:{
                FirebaseAuth.getInstance().signOut();
                //FirebaseAuth.GoogleSignInApi.signOut(auth);
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_share:{
                Toast.makeText(this,"Shared",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_rate:{
                Toast.makeText(this,"High Rated",Toast.LENGTH_SHORT).show();
                break;
            }
        }
     drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
