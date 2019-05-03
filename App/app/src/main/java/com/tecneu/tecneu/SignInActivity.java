package com.tecneu.tecneu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tecneu.tecneu.services.OnLoginRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;


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

        if (UserService.isLoggedIn(this)) {
            Intent intent = new Intent(SignInActivity.this, MainScreenActivity.class);
            finish();
            startActivity(intent);
        }

        _signInButton.setOnClickListener((View v) -> {
            try {
                UserService.login(this, _usernameText.getText().toString(),
                        _passwordText.getText().toString(), new OnLoginRequest() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(SignInActivity.this, MainScreenActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(SignInActivity.this, "Las credenciales estan mal", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
