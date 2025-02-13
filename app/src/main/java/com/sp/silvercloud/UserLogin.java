package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class UserLogin extends AppCompatActivity {
    private ImageView backButton;
    private TextView linkSignUp;

    private EditText emailField, passwordField;
    private Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(UserLogin.this, MainActivity.class);
            startActivity(intent);
            // To close current Activity class and exit
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Link UI Elements
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(onLogin);
        mAuth = FirebaseAuth.getInstance();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(onBack);

        linkSignUp = findViewById(R.id.signUpLink);
        linkSignUp.setOnClickListener(onSignUp);
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



    View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            String email, password;
            email = String.valueOf(emailField.getText());
            password = String.valueOf(passwordField.getText());


            if(TextUtils.isEmpty(email)){
                Toast.makeText(UserLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(UserLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserLogin.this, "Login Successful.",
                                        Toast.LENGTH_SHORT).show();
                                // Vera, please change the "HomeFragment.class" to your main Organizer Homepage file name
                                Intent intent = new Intent(UserLogin.this, MainActivity.class);
                                startActivity(intent);
                                // To close current Activity class and exit
                                finish();
                            } else {
                                Toast.makeText(UserLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    };
}