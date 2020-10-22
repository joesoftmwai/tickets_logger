package com.joesoft.ticketslogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joesoft.ticketslogger.issues.IssuesActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputEditText mEmail, mPassword;
    private TextView mRegister, mForgotPassword, mResendVerification;
    private Button mBtnSignIn;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegister = findViewById(R.id.txt_register);
        mForgotPassword = findViewById(R.id.txt_forgot_password);
        mResendVerification = findViewById(R.id.txt_resend_verification);
        mBtnSignIn = findViewById(R.id.btn_signin);
        mProgressBar = findViewById(R.id.progress_bar);

        mBtnSignIn.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mResendVerification.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signin) {
            authenticateUser(mEmail.getText().toString(), mPassword.getText().toString());
        }
        if (view.getId() == R.id.txt_register) {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        }
        if (view.getId() == R.id.txt_forgot_password) {

        }
        if (view.getId() == R.id.txt_resend_verification) {

        }
    }

    private void authenticateUser(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            if (validateEmail(email)) {
                mProgressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail: success");
                                    Intent issuesIntent = new Intent(LoginActivity.this, IssuesActivity.class);
                                    startActivity(issuesIntent);
                                    finish();
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this,
                                            "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;

        mEmail.setError("Invalid email address");
        return false;
    }

    private void setupFirebaseAuthListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged: signed_in " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with " + user.getEmail()
                                , Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), "Check your email box for a verification link"
                                + user.getEmail(), Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();

                    }
                }
            }
        };
    }
}
