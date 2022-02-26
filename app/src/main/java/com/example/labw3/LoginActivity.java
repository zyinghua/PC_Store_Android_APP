package com.example.labw3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button signUpBtn;
    private EditText userEmailEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.userEmailEt = findViewById(R.id.userEmail);
        this.passwordEt = findViewById(R.id.password);

        this.loginBtn = findViewById(R.id.signup);
        this.loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userEmail = userEmailEt.getText().toString();
                String passwordEntered = passwordEt.getText().toString();

                SharedPreferences user_acc_sp = getSharedPreferences(getString(R.string.USERS_ACC_DATA_SP), 0);
                String password = user_acc_sp.getString(userEmail, getString(R.string.NO_SUCH_USER_LOGIN_ERROR));

                if (password.equals(getString(R.string.NO_SUCH_USER_LOGIN_ERROR)))
                {
                    Snackbar.
                            make(v,
                                    getString(R.string.NO_SUCH_USER_ERROR_DISPLAY_MSG), Snackbar.LENGTH_LONG).
                            setAction("Sign Up", new signUpClickListener()).
                            show();
                }
                else
                {
                    if (passwordEntered.equals(password))
                    {
                        SharedPreferences usernames_sp = getSharedPreferences(getString(R.string.USERNAME_DATA_SP), 0);
                        String username = usernames_sp.getString(userEmail, getString(R.string.USERNAME_RETRIEVE_ERROR));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(getString(R.string.LOGGED_USERNAME_TAG), username);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(LoginActivity.this, getString(R.string.PASSWORD_MISMATCH_LOGIN_ERROR), Toast.LENGTH_LONG).show();
                }
            }
        });

        this.signUpBtn = findViewById(R.id.signin);
        this.signUpBtn.setOnClickListener(new signUpClickListener());
    }

    private class signUpClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }
}