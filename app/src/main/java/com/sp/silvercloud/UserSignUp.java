package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {
    private ImageView backButton;
    private TextView linkLogin;

    private EditText emailInput, passwordInput, nameInput;
    private AutoCompleteTextView interestInput;
    private Button signUpButton;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(onBack);

        linkLogin = findViewById(R.id.loginRedirect);
        linkLogin.setOnClickListener(onLogin);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Link UI Elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nameInput = findViewById(R.id.nameInput);
        interestInput = findViewById(R.id.interestInput);
        signUpButton = findViewById(R.id.signUpButton);

        // OnClickListener for Sign Up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String interest = interestInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || interest.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log values before storing
        Log.d("DEBUG", "Email: " + email);
        Log.d("DEBUG", "Password: " + password);
        Log.d("DEBUG", "Name: " + name);
        Log.d("DEBUG", "Interest: " + interest);

        // Create a unique key for each user
        String userId = databaseReference.push().getKey();

        // Create a User object
        UserDetails user = new UserDetails(email, password, name, interest);

        // Store user in Firebase
        databaseReference.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("DEBUG", "User stored successfully: " + userId);
                    Toast.makeText(UserSignUp.this, "User Registered Successfully!\nPress 'Click Here to Login", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("DEBUG", "Firebase Error: " + e.getMessage());
                    Toast.makeText(UserSignUp.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    View.OnClickListener onBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(UserSignUp.this, Welcome.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
    View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(UserSignUp.this, UserLogin.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
}