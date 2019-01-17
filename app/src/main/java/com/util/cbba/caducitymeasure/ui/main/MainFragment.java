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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.util.cbba.caducitymeasure.MainActivity;
import com.util.cbba.caducitymeasure.R;
import com.util.cbba.caducitymeasure.persistence.entity.Item;
import com.util.cbba.caducitymeasure.ui.main.adapter.ItemListAdapter;
import com.util.cbba.caducitymeasure.ui.main.enums.Period;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
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

        adapter = new ItemListAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        setupSpinner(view);
        return view;
    }

    private void setupSpinner(View view) {
        List<Period> periods = new ArrayList<>();
        periods.add(Period.EXPIRE_TODAY);
        periods.add(Period.EXPIRE_SOON);
        periods.add(Period.EXPIRE_ALL);
        periods.add(Period.EXPIRE_ALL_BY_DATE);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerList);

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_period, periods);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadItemsByPeriod((Period)adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        loadItemsByPeriod(Period.EXPIRE_TODAY);
    }

    private void loadItemsByPeriod(Period period) {
        Observer<List<Item>> obsChanges = new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> itemList) {
                adapter.setItems(itemList);
            }
        };
        switch (period) {
            case EXPIRE_TODAY:
                mViewModel.getItemsToExpireNow().observe(this, obsChanges);
                break;
            case EXPIRE_SOON:
                mViewModel.getAllItemsByExpirationNext().observe(this, obsChanges);
                break;
            case EXPIRE_ALL:
                mViewModel.getAllItems().observe(this, obsChanges);
                break;
            case EXPIRE_ALL_BY_DATE:
                mViewModel.getAllItemsByExpiration().observe(this, obsChanges);
                break;
        }

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

}
