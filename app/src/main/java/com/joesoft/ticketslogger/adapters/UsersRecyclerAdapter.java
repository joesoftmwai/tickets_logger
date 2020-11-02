package com.joesoft.ticketslogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.User;
import com.joesoft.ticketslogger.utils.IssuesCountUtil;

import java.util.ArrayList;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> mUsers = new ArrayList<>();

    public UsersRecyclerAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mName.setText(user.getName());
        holder.mEmail.setText(user.getEmail());
        holder.mAssignedIssues.setText("( 3 )");
        holder.mSolvedIssues.setText(IssuesCountUtil.solvedIssuesCount(user.getName()));
        String lastLogin = "no last login";
        if (!user.getLast_login().equals(user.getDate_created())) {
            lastLogin = user.getLast_login().toString();
        }
        holder.mLastLogin.setText(lastLogin);
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mProfileImg;
        public TextView mName, mEmail, mSolvedIssues, mAssignedIssues, mLastLogin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImg = itemView.findViewById(R.id.profile_img);
            mName = itemView.findViewById(R.id.name);
            mEmail = itemView.findViewById(R.id.email);
            mSolvedIssues = itemView.findViewById(R.id.solved_issues);
            mAssignedIssues = itemView.findViewById(R.id.assigned_issues);
            mLastLogin = itemView.findViewById(R.id.last_login);
        }
    }
}
