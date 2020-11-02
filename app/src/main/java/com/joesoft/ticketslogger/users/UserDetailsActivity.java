package com.joesoft.ticketslogger.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.Issue;
import com.joesoft.ticketslogger.models.User;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserDetailsActivity";
    private TextInputEditText mName, mPhone, mEmail;
    private ImageView mProfileImage;
    private ProgressBar mProgressBar;
    private String mCurrentUserId;
    private User mUser;
    private Issue mIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mEmail = findViewById(R.id.email);
        mProfileImage = findViewById(R.id.profile_img);
        mProgressBar = findViewById(R.id.progress_bar);

        mProfileImage.setOnClickListener(this);

        getAccountDetails();

    }

    private void getAccountDetails() {
        mProgressBar.setVisibility(View.VISIBLE);
        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.users_collection))
                .document(mCurrentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        mUser = task.getResult().toObject(User.class);
                        setAccountDetails();
                        Log.d(TAG, "onComplete: user " + mUser.toString());
                    }
                } else {
                    Log.d(TAG, "onComplete: Error fetching user details, " + task.getException());
                    Toast.makeText(getApplicationContext(), "Error fetching user details",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setAccountDetails() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mName.setText(mUser.getName());
        mPhone.setText(mUser.getPhone());
        mEmail.setText(userEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_technician_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            finish();
            return true;
        } else if (id == R.id.action_save) {
            updateUserAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUserAccount() {
        mProgressBar.setVisibility(View.VISIBLE);

        User user = new User();
        user.setName(mName.getText().toString());
        user.setPhone(mPhone.getText().toString());
        user.setRole(mUser.getRole());
        user.setProfile_image(mUser.getProfile_image());
        user.setUser_id(mUser.getUser_id());
        user.setEmail(mUser.getEmail());
        user.setLast_login(mUser.getLast_login());
        user.setDate_created(mUser.getDate_created());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.users_collection))
                .document(mCurrentUserId)
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateIssueAssignee();
                    Toast.makeText(getApplicationContext(), "Updated user account", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "onComplete: Failed to update user account, " + task.getException());
                    Toast.makeText(getApplicationContext(), "Failed to update user account", Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setProgress(View.INVISIBLE);
            }
        });
    }

    private void updateIssueAssignee() {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.issues_collection))
                .whereEqualTo("assignee", mUser.getName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                        mIssue = documentSnapshot.toObject(Issue.class);
                        mIssue.setTitle(mIssue.getTitle());
                        mIssue.setIssue_type(mIssue.getIssue_type());
                        mIssue.setDescription(mIssue.getDescription());
                        mIssue.setPriority(mIssue.getPriority());
                        mIssue.setAssignee(mName.getText().toString());
                        mIssue.setReporter(mIssue.getReporter());
                        mIssue.setTime_reported(mIssue.getTime_reported());
                        mIssue.setStatus(mIssue.getStatus());
                        mIssue.setIssue_id(mIssue.getIssue_id());
                        mIssue.setComments(mIssue.getComments());

                        firestore.collection(getString(R.string.issues_collection))
                                .document(mIssue.getIssue_id()).set(mIssue)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Toast.makeText(getApplicationContext(), "updated issue's assignee", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.priority_img) {
            // do something
        }
    }
}
