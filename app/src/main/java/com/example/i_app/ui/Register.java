package com.example.i_app.ui;

import android.app.ProgressDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.i_app.MainActivity;
import com.example.i_app.R;
import com.example.i_app.data.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Button btn_register;
    private EditText text_name;
    private EditText text_email;
    private EditText text_password;
    private EditText text_confirmPassword;
    private FirebaseAuth auth;
    private String userId;


    public void onRegister(View view) {

        if (!isDataValid()) {
            onFail();
            return;
        }

        btn_register.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account");
        progressDialog.show();


        final String email = text_email.getText().toString().trim();
        String password = text_password.getText().toString().trim();
        final String name = text_name.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Registration Result", "createUserWithEmail:success");
                            //Stores the Registered user Data
                            FirebaseUser user = auth.getCurrentUser();
                            userId = user.getUid();
                            Database.registereUser(userId, name, email);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Registration Result", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);

    }

    public boolean isDataValid() {

        boolean valid = true;

        String name = text_name.getText().toString();
        String email = text_email.getText().toString();
        String password = text_password.getText().toString();
        String confirmPassword = text_confirmPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            text_name.setError("at least 3 characters");
            valid = false;
        } else text_name.setError(null);

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_email.setError("enter valid email address");
            valid = false;
        } else text_email.setError(null);

        if (password.isEmpty() || password.length() < 5 || password.length() > 15 || !password.equals(confirmPassword)) {
            if (!password.equals(confirmPassword)) {
                text_password.setError("Password does not match");
            } else text_password.setError("between 5 and 15 alphanumeric characters");
            valid = false;
        } else text_password.setError(null);

        return valid;
    }

    public void updateUI(FirebaseUser user) {
        String name = user.getEmail();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        //Toast.makeText(getApplicationContext(),"Registration Successful! " +name,Toast.LENGTH_SHORT).show();
    }

    public void onFail() {
        Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show();
        btn_register.setEnabled(true);
    }

    public void onChangeLogin(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        text_name = findViewById(R.id.text_name);
        text_email = findViewById(R.id.text_email);
        text_password = findViewById(R.id.text_password);
        text_confirmPassword = findViewById(R.id.text_confirmPassword);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            updateUI(user);

        }

        text_confirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onRegister(v);
                }
                return false;
            }
        });

        TextView registerTextview = findViewById(R.id.registerTextview);

        registerTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });
    }

    public void onBackground(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
