package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

public class UserSignUp extends AppCompatActivity {
    private ImageView backButton;
    private TextView linkLogin;
    private EditText emailInput, passwordInput, nameInput;
    private AutoCompleteTextView interestInput;
    private Button signUpButton;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser  currentUser  = mAuth.getCurrentUser ();
        if (currentUser  != null) {
            navigateToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        linkLogin = findViewById(R.id.loginRedirect);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nameInput = findViewById(R.id.nameInput);
        interestInput = findViewById(R.id.interestInput);
        signUpButton = findViewById(R.id.signUpButton);

        // Set up click listeners
        backButton.setOnClickListener(onBack);
        linkLogin.setOnClickListener(onLogin);
        signUpButton.setOnClickListener(onSignUp);
    }

    private View.OnClickListener onSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            String interest = interestInput.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(email)) {
                showToast("Please enter your email.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                showToast("Please enter your password.");
                return;
            }
            if (TextUtils.isEmpty(name)) {
                showToast("Please enter your name.");
                return;
            }

            // Create user with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showToast("Account created successfully.");
                                FirebaseUser  currentUser  = mAuth.getCurrentUser ();
                                if (currentUser  != null) {
                                    String userId = currentUser .getUid();
                                    // Store user data in Firebase Realtime Database
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                    userRef.child("name").setValue(name);
                                    userRef.child("interest").setValue(interest);
                                }
                                navigateToMainActivity();
                            } else {
                                showToast("Authentication failed. Please try again.");
                            }
                        }
                    });
        }
    };

    private void navigateToMainActivity() {
        Intent intent = new Intent(UserSignUp.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener onBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserSignUp.this, Welcome.class);
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserSignUp.this, UserLogin.class);
            startActivity(intent);
            finish();
        }
    };

    private void showToast(String message) {
        Toast.makeText(UserSignUp.this, message, Toast.LENGTH_SHORT).show();
    }
}