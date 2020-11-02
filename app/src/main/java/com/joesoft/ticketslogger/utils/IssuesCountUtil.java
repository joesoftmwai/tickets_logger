package com.joesoft.ticketslogger.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class IssuesCountUtil {

    private static int mSiCount;

    public static String solvedIssuesCount(String userName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = firestore.collection("issues");
        Query query = issuesRef.whereEqualTo("status", "Closed");
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        mSiCount = task.getResult().size();
                    }
                }
            }
        });
        return "(" + mSiCount + ")";
    }
}
