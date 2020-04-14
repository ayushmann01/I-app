package com.example.i_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Session2Command;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_app.MainActivity;
import com.example.i_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText text_email;
    private EditText text_password;
    private Button btn_login;
    private FirebaseAuth auth;

    public void onChangeRegister(View view){
        startActivity(new Intent(Login.this,Register.class));
        finish();
    }

    public void login(View view){
        if(!isDataValid()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog( this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in");
        progressDialog.show();

        String email = text_email.getText().toString();
        String password = text_password.getText().toString();

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() ){
                            Log.d("result","SigninWithEmail: Success");

                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUI(user);
                                    progressDialog.dismiss();
                                }
                            },1500);
                        }else{
                            new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("result","SigninWithEmail: Failed");
                                Toast.makeText(Login.this,"Invalid username or Password",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        },1000);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user){
        String name = user.getEmail();
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
        //Toast.makeText(getApplicationContext(),"Welcome! " +name,Toast.LENGTH_SHORT).show();
    }

    public boolean isDataValid(){

        boolean valid = true;

        String email = text_email.getText().toString();
        String password = text_password.getText().toString();

        if( email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            text_email.setError("enter valid email address");
            valid = false;
        }else text_email.setError(null);

        if(password.isEmpty() || password.length() < 5 || password.length() > 15) {
            text_password.setError("between 5 and 15 alphanumeric characters");
            valid = false;
        } else text_password.setError(null);

        return valid;
    }

    public void onBackground(View view){
        InputMethodManager  inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_email = findViewById(R.id.email);
        text_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        TextView loginTextview = findViewById(R.id.loginTextview);

        auth = FirebaseAuth.getInstance();

        text_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    login(v);
                }
                return false;
            }
        });

        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager  inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });
    }
}
