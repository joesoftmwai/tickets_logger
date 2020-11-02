package com.joesoft.ticketslogger.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.adapters.UsersRecyclerAdapter;
import com.joesoft.ticketslogger.models.Issue;
import com.joesoft.ticketslogger.models.User;
import com.joesoft.ticketslogger.utils.IssuesCountUtil;

import java.util.ArrayList;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "UsersActivity";
    public static final int ROWS = 2;
    private ArrayList<User> mUsers = new ArrayList<>();
    private ArrayList<Issue> mIssuesCount = new ArrayList<>();
    private UsersRecyclerAdapter mUsersRecyclerAdapter;
    private RecyclerView mUsersRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUsersRecyclerView = findViewById(R.id.rv_users);
        mProgressBar = findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        initIssuesRecyclerView();

    }



    private void initIssuesRecyclerView() {
        mUsersRecyclerView.setLayoutManager(new GridLayoutManager(this, ROWS));
        mUsersRecyclerAdapter = new UsersRecyclerAdapter(this, mUsers);
        mUsersRecyclerView.setAdapter(mUsersRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void getUsers() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mUsers != null) {
            if (mUsers.size() > 0) {
                mUsers.clear();
            }
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.users_collection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                mUsers.add(user);
                            }
                        } else {
                            Log.d(TAG, "onComplete: Error fetching users, " + task.getException());
                            Toast.makeText(getApplicationContext(), "Error fetching users", Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mUsersRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.technicians_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getUsers();
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
