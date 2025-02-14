package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class OrganizerSignUp extends AppCompatActivity {
    EditText codeInput,emailInput,passwordInput;
    Button signUpButton;
    ImageButton backButton;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(OrganizerSignUp.this, UserMain.class);
            startActivity(intent);
            // To close current Activity class and exit
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_sign_up);

        backButton = findViewById(R.id.backButton);
        codeInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        mAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.loginRedirect);

        backButton.setOnClickListener(onBack);
        signUpButton = findViewById(R.id.signUpButton);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrganizerLogin.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, code, password;
                email = String.valueOf(emailInput.getText());
                password = String.valueOf(passwordInput.getText());
                code = String.valueOf(codeInput.getText());

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(OrganizerSignUp.this, "Enter Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(OrganizerSignUp.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(OrganizerSignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code.equals("Company")) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(OrganizerSignUp.this, " Account Created.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(OrganizerSignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(OrganizerSignUp.this, "Enter vaild code", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private View.OnClickListener onBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(OrganizerSignUp.this, Welcome.class);
            startActivity(intent);
            finish();
        }
    };
}