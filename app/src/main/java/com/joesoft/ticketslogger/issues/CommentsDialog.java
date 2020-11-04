package com.joesoft.ticketslogger.issues;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.adapters.CommentsRecyclerAdapter;
import com.joesoft.ticketslogger.models.Comment;

import java.util.ArrayList;

public class CommentsDialog extends AppCompatDialogFragment {

    private View mView;
    private RecyclerView mCommentsRecyclerView;
    private ImageView mCloseDialog;
    private CommentsRecyclerAdapter mCommentsRecyclerAdapter;
    private IIssuesDetails mIIssuesDetails;
    private ArrayList<Comment> mComments = new ArrayList<>();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.comments_dialog, null);

        mCommentsRecyclerView = mView.findViewById(R.id.rv_comments);
        mCloseDialog = mView.findViewById(R.id.close_dialog);

        mCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        initCommentsRecyclerView();

        builder.setView(mView);
        return builder.create();
    }

    private void initCommentsRecyclerView() {
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentsRecyclerAdapter =
                new CommentsRecyclerAdapter(getActivity(), mIIssuesDetails.getIssueComments(), mIIssuesDetails.getUsers());
        mCommentsRecyclerView.setAdapter(mCommentsRecyclerAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIIssuesDetails.getIssueComments().clear();
        mIIssuesDetails.getUsers().clear();
        mCommentsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mIIssuesDetails = (IIssuesDetails) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
