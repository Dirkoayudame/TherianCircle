package com.theriancircle.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.theriancircle.app.R;
import com.theriancircle.app.auth.AuthInputValidator;
import com.theriancircle.app.auth.SessionPrefs;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private SessionPrefs sessionPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        sessionPrefs = new SessionPrefs(this);

        EditText emailInput = findViewById(R.id.registerEmailInput);
        EditText passwordInput = findViewById(R.id.registerPasswordInput);
        EditText usernameInput = findViewById(R.id.registerUsernameInput);
        EditText speciesInput = findViewById(R.id.registerSpeciesInput);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        Button goLoginButton = findViewById(R.id.goLoginButton);

        goLoginButton.setOnClickListener(v -> finish());

        createAccountButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String username = usernameInput.getText().toString().trim();
            String species = speciesInput.getText().toString().trim();

            if (!AuthInputValidator.isEmailValid(email)) {
                emailInput.setError(getString(R.string.invalid_email));
                return;
            }
            if (!AuthInputValidator.isPasswordValid(password)) {
                passwordInput.setError(getString(R.string.invalid_password));
                return;
            }

            if (username.isEmpty()) {
                username = getString(R.string.default_username);
            }
            if (species.isEmpty()) {
                species = getString(R.string.default_species);
            }

            String finalUsername = username;
            String finalSpecies = species;
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        sessionPrefs.saveProfile(finalUsername, finalSpecies);
                        openMain(finalUsername, finalSpecies);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    RegisterActivity.this,
                                    R.string.register_failed,
                                    Toast.LENGTH_SHORT
                            ).show());
        });
    }

    private void openMain(String username, String species) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("species", species);
        startActivity(intent);
        finish();
    }
}
