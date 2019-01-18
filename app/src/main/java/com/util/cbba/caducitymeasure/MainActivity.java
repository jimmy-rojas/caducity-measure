package com.util.cbba.caducitymeasure;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private MenuItem cancelScheduleMeu;
    private MenuItem startScheduleMenu;

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

    private void setupNotificationOptions(boolean isServiceRunning) {
        startScheduleMenu.setVisible(!isServiceRunning);
        cancelScheduleMeu.setVisible(isServiceRunning);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        startScheduleMenu = menu.findItem(R.id.startSchedule);
        cancelScheduleMeu = menu.findItem(R.id.cancelSchedule);
        setupNotificationOptions(isServiceRunning(NotificationJobService.class.getName()));
        /*setupNotificationOptions(isServiceRunning(NotificationJobService.class));
        setupNotificationOptions(isJobServiceOn(NotificationJobService.class));*/
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startSchedule:
                scheduleJob();
                setupNotificationOptions(true);
                return true;
            case R.id.cancelSchedule:
                cancelJobs();
                setupNotificationOptions(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this.getApplicationContext()));
        final int periodicity = (int) TimeUnit.HOURS.toSeconds(1); // Every 1 hour periodicity expressed as seconds
        final int toleranceInterval = (int) TimeUnit.MINUTES.toSeconds(0); // a small(ish) window of time when triggering is OK

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

    private boolean isServiceRunning(String serviceClassName) {
        Log.i(TAG, "Verifying Service running for: " + serviceClassName);
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d(TAG, String.format("Service:%s", service.service.getClassName()));
            if (serviceClassName.equalsIgnoreCase(service.service.getClassName())) {
                Log.i(TAG, "Notification Service Is On");
                return true;
            }
        }
        Log.i(TAG, "Notification Service Is Off");
        return false;
    }

    /*private boolean isServiceRunning(Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            Log.d(TAG, String.format("Service:%s", runningServiceInfo.service.getClassName()));
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                Log.i(TAG, "Notification Service Is On");
                return true;
            }
        }
        Log.i(TAG, "Notification Service Is Off");
        return false;
    }

    private boolean isJobServiceOn(Class<?> serviceClass) {
        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            Log.d(TAG, String.format("Service:%s", jobInfo.getService().getClassName()));
            if ( jobInfo.getService().getClassName().equals(serviceClass.getName()) ) {
                Log.i(TAG, "Notification Service Is On");
                return true;
            }
        }
        Log.i(TAG, "Notification Service Is Off");
        return false ;
    }*/

    public enum StackFlag {
        STACK,
        CLEAN_STACK,
        NONE
    }
}
