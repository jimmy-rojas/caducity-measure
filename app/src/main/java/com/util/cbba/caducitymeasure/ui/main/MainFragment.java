package com.util.cbba.caducitymeasure.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.util.cbba.caducitymeasure.MainActivity;
import com.util.cbba.caducitymeasure.NotificationJobService;
import com.util.cbba.caducitymeasure.R;
import com.util.cbba.caducitymeasure.persistence.entity.Item;
import com.util.cbba.caducitymeasure.ui.main.adapter.ItemListAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private MainViewModel mViewModel;
    private ItemListAdapter adapter;

    private MainActivity mainActivity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        view.findViewById(R.id.addNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.navigate(AddEntryFragment.newInstance());
            }
        });
        view.findViewById(R.id.scheduleJob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleJob();
            }
        });
        view.findViewById(R.id.cancelScheduleJob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelJobs();
            }
        });
        adapter = new ItemListAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getAllWords().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> words) {
                adapter.setItems(mViewModel.getAllWords().getValue());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity().getApplicationContext()));
        final int periodicity = (int) TimeUnit.HOURS.toSeconds(1); // Every 1 hour periodicity expressed as seconds
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
        Toast.makeText(getActivity(), R.string.job_scheduled, Toast.LENGTH_SHORT).show();
    }

    /**
     * onClick method for cancelling all existing jobs.
     */
    private void cancelJobs() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity().getApplicationContext()));
        //Cancel all the jobs for this package
        dispatcher.cancelAll();
        // Cancel the job for this tag
        dispatcher.cancel(TAG);
        Toast.makeText(getActivity(), R.string.jobs_canceled, Toast.LENGTH_SHORT).show();
    }

}
