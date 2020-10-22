package com.joesoft.ticketslogger.issues;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.joesoft.ticketslogger.R;

public class IssuesActivity extends AppCompatActivity {
    public static final int ISSUES_FRAGMENT = 0;
    public static final int TECHNICIANS_FRAGMENT = 1;
    private IssuesFragment mIssuesFragment;
    private TechniciansFragment mTechniciansFragment;
    private IssuesPageAdapter mIssuesPageAdapter;

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        mViewPager = findViewById(R.id.view_pager);

        setUpViewPager();
    }

    private void setUpViewPager() {
        mIssuesFragment = new IssuesFragment();
        mTechniciansFragment = new TechniciansFragment();

        mIssuesPageAdapter = new IssuesPageAdapter(getSupportFragmentManager());
        mIssuesPageAdapter.addFragments(mIssuesFragment);
        mIssuesPageAdapter.addFragments(mTechniciansFragment);

        mViewPager.setAdapter(mIssuesPageAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(ISSUES_FRAGMENT).setText(getString(R.string.issues_fragment));
        tabLayout.getTabAt(TECHNICIANS_FRAGMENT).setText(getString(R.string.technicians_fragment));

    }
}
