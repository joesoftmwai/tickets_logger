package com.joesoft.ticketslogger.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.Comment;
import com.joesoft.ticketslogger.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CommentsRecyclerAdapter";
    private Context mContext;
    private ArrayList<Comment> mComments;
    private ArrayList<User> mUsers;

    public CommentsRecyclerAdapter(Context context, ArrayList<Comment> comments, ArrayList<User> users) {
        mContext = context;
        mComments = comments;
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.mComment.setText(comment.getComment());
        String date = comment.getTime_created().toString();
        holder.mDate.setText(date.substring(0, 16));


        for (int i = 0; i < mUsers.size(); i++) {
            final User user = mUsers.get(i);
            if (comment.getCommented_by().equals(user.getUser_id())) {
                holder.mName.setText(user.getName());
            }
        }




        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_person_24dp)
                .error(R.drawable.ic_person_24dp)
                .centerInside();

        Glide.with(mContext)
                .setDefaultRequestOptions(options)
                .load(R.drawable.ic_person_24dp)
                .into(holder.mProfileImg);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mComment, mDate;
        public CircleImageView mProfileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mComment = itemView.findViewById(R.id.comment);
            mDate = itemView.findViewById(R.id.date);
            mProfileImg = itemView.findViewById(R.id.profile_img);
        }
    }
}
