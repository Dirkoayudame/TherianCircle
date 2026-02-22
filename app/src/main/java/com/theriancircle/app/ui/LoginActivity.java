package com.theriancircle.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.theriancircle.app.R;
import com.theriancircle.app.auth.AuthInputValidator;
import com.theriancircle.app.auth.SessionPrefs;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private SessionPrefs sessionPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        sessionPrefs = new SessionPrefs(this);

        if (firebaseAuth.getCurrentUser() != null) {
            openMain(sessionPrefs.getUsername(), sessionPrefs.getSpecies());
            return;
        }

        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button enterButton = findViewById(R.id.enterButton);
        Button goRegisterButton = findViewById(R.id.goRegisterButton);

        goRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        enterButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (!AuthInputValidator.isEmailValid(email)) {
                emailInput.setError(getString(R.string.invalid_email));
                return;
            }
            if (!AuthInputValidator.isPasswordValid(password)) {
                passwordInput.setError(getString(R.string.invalid_password));
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        String derivedName = AuthInputValidator.deriveUsernameFromEmail(
                                email, getString(R.string.default_username));
                        String username = sessionPrefs.getUsername();
                        if (username.equals(getString(R.string.default_username))) {
                            username = derivedName;
                        }
                        openMain(username, sessionPrefs.getSpecies());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.auth_failed,
                                    Toast.LENGTH_SHORT
                            ).show());
        });
    }

    private void openMain(String username, String species) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("species", species);
        startActivity(intent);
        finish();
    }
}
