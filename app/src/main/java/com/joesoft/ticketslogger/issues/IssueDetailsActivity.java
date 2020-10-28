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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.Issue;
import com.joesoft.ticketslogger.models.User;
import com.joesoft.ticketslogger.utils.SpinnerAdapter;
import com.joesoft.ticketslogger.utils.SpinnerResources;

import java.util.ArrayList;
import java.util.Arrays;

public class IssueDetailsActivity extends AppCompatActivity {
    private static final String TAG = "IssueDetailsActivity";
    public static final String ISSUE_DETAILS = "com.joesoft.ticketslogger.issues.ISSUE_DETAILS";
    private TextInputEditText mIssueTitle, mIssueDescription, mReporter;
    private Spinner mIssueTypeSpinner, mPrioritySpinner, mStatusSpinner, mAssigneeSpinner;
    private EditText mComments;
    private ProgressBar mProgressBar;
    private Issue mIssue;
    private ArrayList<User> mUsers = new ArrayList<>();
    private String[] mUserNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        
        mIssueTitle = findViewById(R.id.title);
        mIssueDescription = findViewById(R.id.description);
        mReporter = findViewById(R.id.reporter);
        mIssueTypeSpinner = findViewById(R.id.issue_type_spinner);
        mPrioritySpinner = findViewById(R.id.priority_spinner);
        mStatusSpinner = findViewById(R.id.status_spinner);
        mAssigneeSpinner = findViewById(R.id.assignee_spinner);
        mComments = findViewById(R.id.edit_text_comments);
        mProgressBar =findViewById(R.id.progress_bar);
        
        if (getIssueDetails()) {
            initIssueTypeSpinner();
            initPrioritySpinner();
            initStatusSpinner();
            getUsers();
            setIssueDetails();
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setIssueDetails() {
        mIssueTitle.setText(mIssue.getTitle());
        mIssueDescription.setText(mIssue.getDescription());
        setReporterName();
        mComments.setText(mIssue.getComments());
    }

    private void setReporterName() {
        String reporterId = mIssue.getReporter();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.users_collection))
                .document(reporterId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document= task.getResult();
                    assert document != null;
                    mReporter.setText(document.getString("name"));
                }
            }
        });
    }

    private boolean getIssueDetails() {
        if (getIntent().hasExtra(ISSUE_DETAILS)) {
            mIssue = getIntent().getParcelableExtra(ISSUE_DETAILS);
            return true;
        }
        return false;
    }


    private void getUsers() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.users_collection))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = documentSnapshot.toObject(User.class);
                            mUsers.add(user);
                        }
                        initAssigneeSpinner();
                    }
                }
            }
        });
    }

    private void initAssigneeSpinner() {
        mUserNames = new String[mUsers.size() + 1];
        for (int i=0; i < mUsers.size(); i++) {
            mUserNames[i] = mUsers.get(i).getName();
        }

        mUserNames[mUsers.size()] = "none";

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mUserNames);
        mAssigneeSpinner.setAdapter(adapter);

         setAssigneeSpinner();
    }

    private void setAssigneeSpinner() {
        if (mIssue.getAssignee().equals("none")) {
            int index = Arrays.asList(mUserNames).indexOf("none");
            mAssigneeSpinner.setSelection(index);
        } else {
            int index = Arrays.asList(mUserNames).indexOf(mIssue.getAssignee());
            mAssigneeSpinner.setSelection(index);
        }
    }

    private void initStatusSpinner() {
        final String[] issueStatus = SpinnerResources.issue_status_spinner;
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, issueStatus);
        mStatusSpinner.setAdapter(adapter);

        setStatusSpinner();
    }

    private void setStatusSpinner() {
        switch (mIssue.getStatus()) {
            case Issue.NEW:
                mStatusSpinner.setSelection(0);
                break;
            case Issue.IN_PROGRESS:
                mStatusSpinner.setSelection(1);
                break;
            case Issue.TESTING:
                mStatusSpinner.setSelection(2);
                break;
            case Issue.CLOSED:
                mStatusSpinner.setSelection(3);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mIssue.getStatus());
        }
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
        setPrioritySpinner();
    }

    private void setPrioritySpinner() {
        if (mIssue.getPriorityString().equals(Issue.HIGH)) {
            mPrioritySpinner.setSelection(0);
        } else if (mIssue.getPriorityString().equals(Issue.MEDIUM)) {
            mPrioritySpinner.setSelection(1);
        } else if (mIssue.getPriorityString().equals(Issue.LOW)) {
            mPrioritySpinner.setSelection(2);
        } else {
            mPrioritySpinner.setSelection(0);
        }
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
        setIssueTypeSpinner();
    }

    private void setIssueTypeSpinner() {
        if (mIssue.getIssue_type().equals(Issue.TASK)) {
            mIssueTypeSpinner.setSelection(0);
        } else if (mIssue.getIssue_type().equals(Issue.BUG)) {
            mIssueTypeSpinner.setSelection(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_issue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            finish();
             return true;
        } else if (id == R.id.action_save) {
            updateIssueDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateIssueDetails() {
        if (!mIssueTitle.getText().toString().isEmpty() && !mIssueDescription.getText().toString().isEmpty()
                && !mComments.getText().toString().isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference updateIssueRef = firestore
                    .collection(getString(R.string.issues_collection))
                    .document(mIssue.getIssue_id());

            Issue issue = new Issue();
            issue.setTitle(mIssueTitle.getText().toString());
            issue.setIssue_type(((SpinnerAdapter)mIssueTypeSpinner.getAdapter()).getSelectedText());
            issue.setDescription(mIssueDescription.getText().toString());
            issue.setPriority(issue.getPriorityInteger(((SpinnerAdapter)mPrioritySpinner.getAdapter()).getSelectedText()));
            issue.setAssignee(mAssigneeSpinner.getSelectedItem().toString());
            issue.setReporter(mIssue.getReporter());
            issue.setTime_reported(mIssue.getTime_reported());
            issue.setStatus(mStatusSpinner.getSelectedItem().toString());
            issue.setIssue_id(mIssue.getIssue_id());
            issue.setComments(mComments.getText().toString());

            updateIssueRef.set(issue).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Issue details update success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d(TAG, "onComplete: Failed to Update issue details, " + task.getException());
                        Toast.makeText(getApplicationContext(), "Failed to Update issue details", Toast.LENGTH_SHORT).show();
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
