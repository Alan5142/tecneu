package com.tecneu.tecneu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    private EditText _usernameText;
    private EditText _passwordText;
    private Button _signInButton;
    private Button _forgottenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        _usernameText = findViewById(R.id.login_username);
        _passwordText = findViewById(R.id.login_password);
        _signInButton = findViewById(R.id.login_sign_in_btn);
        _forgottenButton = findViewById(R.id.login_forgotten_password_btn);

        _signInButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, MainScreenActivity.class);
            finish();
            startActivity(intent);
        });
    }
}
