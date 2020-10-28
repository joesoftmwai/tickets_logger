package com.joesoft.ticketslogger.issues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joesoft.ticketslogger.LoginActivity;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.adapters.IssuesRecyclerAdapter;
import com.joesoft.ticketslogger.models.Issue;
import com.joesoft.ticketslogger.technicians.NewTechnicianActivity;
import com.joesoft.ticketslogger.technicians.UsersActivity;

import java.util.ArrayList;

public class IssuesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "IssuesActivity";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private IssuesRecyclerAdapter mIssuesRecyclerAdapter;
    private ArrayList<Issue> mIssues = new ArrayList<>();
    private RecyclerView mIssuesRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        mIssuesRecyclerView = findViewById(R.id.rv_issues);
        mProgressBar = findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseAuthListener();

       // getIssues();
        initIssuesRecyclerView();
    }

    private void getIssues() {
        mProgressBar.setVisibility(View.VISIBLE);

        if (mIssues != null) {
            if (mIssues.size() > 0) {
                mIssues.clear();
            }
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = firestore.collection(getString(R.string.issues_collection));
        issuesRef.whereEqualTo("status", "Closed");
                issuesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            Issue issue = documentSnapshot.toObject(Issue.class);
                            mIssues.add(issue);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No issues found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "onComplete: Error fetching issues; " + task.getException());
                    Toast.makeText(getApplicationContext(),
                            "Error fetching issues", Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
                mIssuesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initIssuesRecyclerView() {
        int[] issueTypeIcons = {R.drawable.ic_task_24dp, R.drawable.ic_bug_24dp};
        mIssuesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mIssuesRecyclerAdapter = new IssuesRecyclerAdapter(this, mIssues, issueTypeIcons);
        mIssuesRecyclerView.setAdapter(mIssuesRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.issues_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void newIssue(View view) {
        Intent intent = new Intent(IssuesActivity.this, NewIssueActivity.class);
        startActivity(intent);
    }

    public void newTechnician(View view) {
        Intent intent = new Intent(IssuesActivity.this, NewTechnicianActivity.class);
        startActivity(intent);
    }

    public void techniciansList(View view) {
        Intent intent = new Intent(IssuesActivity.this, UsersActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
        onRefresh();
    }

    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.d(TAG, "checkAuthenticationState: User is null, navigating back to login activity");
            Intent intent = new Intent(IssuesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    private void setupFirebaseAuthListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(IssuesActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onRefresh() {
        getIssues();
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

