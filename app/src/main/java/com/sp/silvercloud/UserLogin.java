package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.content.SharedPreferences;

public class UserLogin extends AppCompatActivity {
    private ImageView backButton;
    private TextView linkSignUp;

    private EditText emailField, passwordField;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "UserLogin";  // Log tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Link UI Elements
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginBtn);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(onBack);

        linkSignUp = findViewById(R.id.signUpLink);
        linkSignUp.setOnClickListener(onSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getString("userId", null) != null) {
            redirectToMainActivity();
        }

        // Login Button Click Listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(UserLogin.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                validateUser(email, password);
            }
        });
    }

    private void validateUser(String email, String password) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String storedEmail = userSnapshot.child("email").getValue(String.class);
                    String storedPassword = userSnapshot.child("password").getValue(String.class);

                    if (storedEmail != null && storedPassword != null &&
                            storedEmail.equals(email) && storedPassword.equals(password)) {

                        // Save user ID to SharedPreferences
                        String userId = userSnapshot.getKey();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", userId);
                        editor.apply();

                        // Redirect to MainActivity
                        redirectToMainActivity();
                        return;
                    }
                }
                // If no matching user is found
                Toast.makeText(UserLogin.this, "Authentication failed. Invalid email or password.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserLogin.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(UserLogin.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears backstack
        startActivity(intent);
        finish();
    }

    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserLogin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserLogin.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    View.OnClickListener onBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(UserLogin.this, Welcome.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };

    View.OnClickListener onSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(UserLogin.this, UserSignUp.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
}