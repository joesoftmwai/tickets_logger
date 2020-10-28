package com.joesoft.ticketslogger.utils;

import com.joesoft.ticketslogger.R;
import com.joesoft.ticketslogger.models.Issue;

public class SpinnerResources {
    public static final String[] issue_priorities_spinner = {
            Issue.HIGH,
            Issue.MEDIUM,
            Issue.LOW
    };
    public static final int [] issue_priority_images_spinner = {
            R.drawable.ic_priority_high_24dp,
            R.drawable.ic_priority_medium_24dp,
            R.drawable.ic_priority_low_24dp
    };

    public static final String[] issue_status_spinner = {
            Issue.NEW,
            Issue.IN_PROGRESS,
            Issue.TESTING,
            Issue.CLOSED
    };

    public static final String[] issue_types_spinner = {
            Issue.TASK,
            Issue.BUG,
    };
    public static final int[] issue_type_images_spinner = {
            R.drawable.ic_task_24dp,
            R.drawable.ic_bug_24dp,
    };
}
