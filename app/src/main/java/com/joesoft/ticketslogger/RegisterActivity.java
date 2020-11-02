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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joesoft.ticketslogger.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private TextInputEditText mName, mEmail, mPassword, mConfirmPassword;
    private Button mBtnRegister;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        mBtnRegister = findViewById(R.id.btn_register);
        mProgressBar = findViewById(R.id.progress_bar);

        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            if (!mName.getText().toString().isEmpty() && !mEmail.getText().toString().isEmpty() &&
                    !mPassword.getText().toString().isEmpty() && !mConfirmPassword.getText().toString().isEmpty()) {
                if (validateEmail(mEmail.getText().toString())) {
                    if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                        registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerNewEmail(String email, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail: success");
                            // sendVerification
                            sendEmailVerification();
                            // add user to firestore
                            User user = new User();
                            user.setName(mName.getText().toString());
                            user.setPhone("");
                            user.setRole("");
                            user.setProfile_image("");
                            user.setUser_id(mAuth.getCurrentUser().getUid());
                            user.setEmail(mAuth.getCurrentUser().getEmail());
                            user.setLast_login(null);
                            user.setDate_created(null);

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            DocumentReference newUserRef = firestore
                                    .collection(getString(R.string.users_collection))
                                    .document(mAuth.getCurrentUser().getUid());

                            newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.signOut();
                                        redirectToLoginScreen();
                                    } else {
                                        mAuth.signOut();
                                        redirectToLoginScreen();
                                        Log.d(TAG, "onFailure: " + task.getException());
                                        Toast.makeText(getApplicationContext(), "Something went wrong "
                                                + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            mProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Log.d(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "createUserWithEmail: failure" + task.getException(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sendEmailVerification: success");
                        Toast.makeText(RegisterActivity.this, "Sent email verification; Check your email box",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "sendEmailVerification: failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Could not send email verification.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;

        mEmail.setError("Invalid email address");
        return false;
    }
}
