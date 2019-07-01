package com.tecneu.tecneu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SignInActivity extends AppCompatActivity {

    private EditText _usernameText;
    private EditText _passwordText;
    private Button _signInButton;
    private Button _forgottenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NukeSSLCerts.nuke();
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
            if(_usernameText.getText().toString().equals("")||_passwordText.getText().toString().equals(""))
            {
                Toast.makeText(SignInActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    UserService.login(this, _usernameText.getText().toString(),
                            _passwordText.getText().toString(), new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
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
            }
        });

    }

    public Context getContext()
    {
        return this;
    }
}
