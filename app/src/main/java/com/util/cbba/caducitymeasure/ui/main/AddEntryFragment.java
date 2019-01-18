package com.util.cbba.caducitymeasure.ui.main;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.util.cbba.caducitymeasure.MainActivity;
import com.util.cbba.caducitymeasure.R;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;
import java.util.Date;

public class AddEntryFragment extends Fragment {

    private AddEntryViewModel mViewModel;
    private MainActivity mainActivity;

    private TextView expDate;
    private TextInputEditText itemName;
    private TextInputEditText expDesc;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar = Calendar.getInstance();
    private Calendar pickedDate = Calendar.getInstance();;

    public static AddEntryFragment newInstance() {
        return new AddEntryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_entry_fragment, container, false);
        expDate = view.findViewById(R.id.expDate);
        itemName = view.findViewById(R.id.itemName);
        expDesc = view.findViewById(R.id.expDesc);
        view.findViewById(R.id.addNewEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(expDate.getText())) {
                    Toast.makeText(getActivity(), "Seleccionar Fecha", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(itemName.getText())) {
                    itemName.setError("Escribir nombre de Item");
                    Toast.makeText(getActivity(), "Escribir nombre de Item", Toast.LENGTH_LONG).show();
                    return;
                }
                itemName.setError(null);
                Item item = new Item(itemName.getText().toString(), expDesc.getText().toString(), pickedDate.getTime(), false);
                mViewModel.insert(item);
                cleanForm();
                Toast.makeText(getActivity(), "Guardado", Toast.LENGTH_LONG).show();
                mainActivity.navigateToRoot();
            }
        });
        view.findViewById(R.id.btnDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                expDate.setText(day + "/" + (month + 1) + "/" + year);
                                pickedDate.set(year, month, day);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        return view;
    }

    private void cleanForm() {
        itemName.setError(null);
        itemName.setText("");
        expDesc.setText("");
        expDate.setText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddEntryViewModel.class);
        // TODO: Use the ViewModel
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
