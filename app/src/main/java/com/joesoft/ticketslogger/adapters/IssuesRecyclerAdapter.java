package com.joesoft.ticketslogger.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.issues.IssueDetailsActivity;
import com.joesoft.ticketslogger.models.Issue;

import java.util.ArrayList;

public class IssuesRecyclerAdapter extends RecyclerView.Adapter<IssuesRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Issue> mIssues = new ArrayList<>();
    private int[] mIcons;

    public IssuesRecyclerAdapter(Context context, ArrayList<Issue> issues, int[] icons) {
        mContext = context;
        mIssues = issues;
        mIcons = icons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_issues, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Issue issue = mIssues.get(position);
        holder.mIssueTitle.setText(issue.getTitle());
        holder.mIssueStatus.setText(issue.getStatus());
        holder.mDateReported.setText(issue.getTime_reported().toString());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_camera_24dp)
                .error(R.drawable.ic_camera_24dp)
                .centerInside();

        // set issue type icons
        int issueIcon;
        if (issue.getIssue_type().equals(Issue.TASK)) {
            issueIcon = mIcons[0];
        } else {
            issueIcon = mIcons[1];
        }

        Glide.with(mContext)
                .setDefaultRequestOptions(options)
                .load(issueIcon)
                .into(holder.mIssueTypeImg);

        // set priority icons
        switch (issue.getPriority()) {
            case 1: // low priority
                Glide.with(mContext)
                        .setDefaultRequestOptions(options)
                        .load(R.drawable.ic_priority_low_24dp)
                        .into(holder.mPriorityImg);
                break;
            case 2: // medium priority
                Glide.with(mContext)
                        .setDefaultRequestOptions(options)
                        .load(R.drawable.ic_priority_medium_24dp)
                        .into(holder.mPriorityImg);
                break;
            case 3: // high priority
                Glide.with(mContext)
                        .setDefaultRequestOptions(options)
                        .load(R.drawable.ic_priority_high_24dp)
                        .into(holder.mPriorityImg);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mIssues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mIssueTitle, mIssueStatus, mDateReported;
        public ImageView mIssueTypeImg, mPriorityImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIssueTitle = itemView.findViewById(R.id.title);
            mIssueStatus = itemView.findViewById(R.id.issue_status);
            mDateReported = itemView.findViewById(R.id.date_reported);
            mIssueTypeImg = itemView.findViewById(R.id.issue_type_img);
            mPriorityImg = itemView.findViewById(R.id.priority_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Issue issue = mIssues.get(position);
            Intent intent = new Intent(v.getContext(), IssueDetailsActivity.class);
            intent.putExtra(IssueDetailsActivity.ISSUE_DETAILS, issue);
            v.getContext().startActivity(intent);
        }
    }
}
