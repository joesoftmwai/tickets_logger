package com.joesoft.ticketslogger.issues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.Issue;
import com.joesoft.ticketslogger.utils.SpinnerAdapter;
import com.joesoft.ticketslogger.utils.SpinnerResources;

public class NewIssueActivity extends AppCompatActivity {
    private static final String TAG = "NewIssueActivity";
    private Spinner mIssueTypeSpinner, mPrioritySpinner;
    private TextInputEditText mIssueTitle, mIssueDescription;
    private TextView mAddAttachment;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_issue);

        mIssueTypeSpinner = findViewById(R.id.issue_type_spinner);
        mPrioritySpinner = findViewById(R.id.priority_spinner);
        mIssueTitle = findViewById(R.id.title);
        mIssueDescription = findViewById(R.id.issue_description);
        mAddAttachment = findViewById(R.id.add_attachment);
        mProgressBar = findViewById(R.id.progress_bar);

        initIssueTypeSpinner();
        initPrioritySpinner();
    }

    private void initIssueTypeSpinner() {
        final String[] issueTypes = SpinnerResources.issue_types_spinner;
        int[] issueTypeImages = SpinnerResources.issue_type_images_spinner;

        final SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, issueTypes, issueTypeImages);
        mIssueTypeSpinner.setAdapter(spinnerAdapter);

        mIssueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerAdapter.setSelectedText(issueTypes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: nothing
            }
        });
    }

    private void initPrioritySpinner() {
        final String[] issuePriorities = SpinnerResources.issue_priorities_spinner;
        int[] issuePriorityImages = SpinnerResources.issue_priority_images_spinner;
        final SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, issuePriorities, issuePriorityImages);
        mPrioritySpinner.setAdapter(spinnerAdapter);
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerAdapter.setSelectedText(issuePriorities[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: nothing
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_issue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            finish();
            return true;
        } else if (id == R.id.action_save) {
            saveNewIssue();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewIssue() {
        if (mIssueTitle.getText().toString().isEmpty()) {
            mIssueTitle.setError("Required Field");
        } else if (mIssueDescription.getText().toString().isEmpty()) {
            mIssueDescription.setError("Required Field");
        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference newIssueRef = firestore
                    .collection(getString(R.string.issues_collection))
                    .document();

            Issue issue = new Issue();
            issue.setTitle(mIssueTitle.getText().toString());
            issue.setIssue_type(((SpinnerAdapter)mIssueTypeSpinner.getAdapter()).getSelectedText());
            issue.setDescription(mIssueDescription.getText().toString());
            issue.setPriority(issue.getPriorityInteger(((SpinnerAdapter)mPrioritySpinner.getAdapter()).getSelectedText()));
            issue.setAssignee("none");
            issue.setReporter(FirebaseAuth.getInstance().getCurrentUser().getUid());
            issue.setTime_reported(null);
            issue.setStatus(Issue.NEW);
            issue.setIssue_id(newIssueRef.getId());
            issue.setComments("");

            newIssueRef.set(issue).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "New issue Created", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d(TAG, "onComplete: Could not Create new issue " + task.getException());
                        Toast.makeText(getApplicationContext(), "Could not Create new issue", Toast.LENGTH_SHORT).show();
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
