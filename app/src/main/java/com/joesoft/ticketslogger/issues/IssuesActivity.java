package com.joesoft.ticketslogger.issues;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.technicians.NewTechnicianActivity;
import com.joesoft.ticketslogger.technicians.TechniciansActivity;

public class IssuesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

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
        Intent intent = new Intent(IssuesActivity.this, TechniciansActivity.class);
        startActivity(intent);
    }

}
