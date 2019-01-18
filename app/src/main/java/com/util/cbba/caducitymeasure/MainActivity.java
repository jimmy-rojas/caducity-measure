package com.util.cbba.caducitymeasure;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.util.cbba.caducitymeasure.ui.main.AddEntryFragment;
import com.util.cbba.caducitymeasure.ui.main.MainFragment;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected FragmentManager fragmentManager;
    protected Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentManager.findFragmentById(R.id.container);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentFragment == null) {
            navigate(MainFragment.newInstance(), StackFlag.NONE, MainActivity.TAG);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startSchedule:
                scheduleJob();
                return true;
            case R.id.cancelSchedule:
                cancelJobs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this.getApplicationContext()));
        final int periodicity = (int) TimeUnit.HOURS.toSeconds(0); // Every 1 hour periodicity expressed as seconds
        final int toleranceInterval = (int) TimeUnit.MINUTES.toSeconds(1); // a small(ish) window of time when triggering is OK

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(NotificationJobService.class)
                // uniquely identifies the job
                .setTag(TAG)
                // recurring job
                .setRecurring(true)
                // persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(periodicity, periodicity + toleranceInterval))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(myJob);
        Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_SHORT).show();
    }

    /**
     * onClick method for cancelling all existing jobs.
     */
    private void cancelJobs() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this.getApplicationContext()));
        //Cancel all the jobs for this package
        dispatcher.cancelAll();
        // Cancel the job for this tag
        dispatcher.cancel(TAG);
        Toast.makeText(this, R.string.jobs_canceled, Toast.LENGTH_SHORT).show();
    }

    public void navigate(Fragment fragment, StackFlag flag, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        currentFragment = fragment;
        transaction.replace(R.id.container, currentFragment, tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (flag) {
            case STACK:
                transaction.addToBackStack(null);
                break;
            case CLEAN_STACK:
                cleanFragmentStack();
                break;
            default:
        }
        transaction.commit();
    }
    public void cleanFragmentStack() {
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public void navigateToRoot() {
        navigate(MainFragment.newInstance(), StackFlag.CLEAN_STACK, MainActivity.TAG);
    }

    public void navigateToAdd() {
        navigate(AddEntryFragment.newInstance(), StackFlag.STACK, MainActivity.TAG);
    }

    public enum StackFlag {
        STACK,
        CLEAN_STACK,
        NONE
    }
}
