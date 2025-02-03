package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OrganizerLogin extends AppCompatActivity {
    private ImageView backButton;
    private TextView linkSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_login);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(onBack);

        linkSignUp = findViewById(R.id.signUpLink);
        linkSignUp.setOnClickListener(onSignUp);
    }

    View.OnClickListener onBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(OrganizerLogin.this, Welcome.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };

    View.OnClickListener onSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(OrganizerLogin.this, OrganizerSignUp.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
}