package com.example.labw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEt;
    private TextView userEmailEt;
    private TextView passwordEt;
    private Button signupBtn;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEt = findViewById(R.id.username);
        userEmailEt = findViewById(R.id.userEmail);
        passwordEt = findViewById(R.id.password);

        signupBtn = findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = userEmailEt.getText().toString();
                SharedPreferences users_acc_sp = getSharedPreferences(getString(R.string.USERS_ACC_DATA_SP), 0);
                String user_existence = users_acc_sp.getString(userEmail, getString(R.string.NO_SUCH_USER_LOGIN_ERROR));

                if (!user_existence.equals(getString(R.string.NO_SUCH_USER_LOGIN_ERROR)))
                {
                    Toast.makeText(SignUpActivity.this, "User already exists, please sign up with another Email", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences.Editor acc_editor = users_acc_sp.edit();
                    acc_editor.putString(userEmail, passwordEt.getText().toString());
                    acc_editor.apply();

                    String username = usernameEt.getText().toString();
                    SharedPreferences usernames_sp = getSharedPreferences(getString(R.string.USERNAME_DATA_SP), 0);
                    SharedPreferences.Editor usernames_editor = usernames_sp.edit();
                    usernames_editor.putString(userEmail, username);
                    usernames_editor.apply();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra(getString(R.string.LOGGED_USERNAME_TAG), username);
                    startActivity(intent);
                }
            }
        });

        loginBtn = findViewById(R.id.signin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
    }
}