package com.util.cbba.caducitymeasure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.util.cbba.caducitymeasure.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            navigate(MainFragment.newInstance(), false);
        }
    }

    public void navigate(Fragment fragment) {
        navigate(fragment, true);
    }

    private void navigate(Fragment fragment, boolean putToBack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (putToBack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
