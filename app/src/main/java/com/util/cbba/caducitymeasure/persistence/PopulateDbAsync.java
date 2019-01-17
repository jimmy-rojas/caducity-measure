package com.util.cbba.caducitymeasure.persistence;

import android.os.AsyncTask;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final IItemDao itemDao;

    public PopulateDbAsync(CaducityMeasureDatabase db) {
        itemDao = db.itemDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        itemDao.deleteAll();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 2; i++) {
            Item item = new Item("Dummy Item", "", cal.getTime(), false);
            itemDao.insert(item);
        }

        cal.add(Calendar.DATE, 1);
        Item item = new Item("Dummy Item, +1", "Any Desc", cal.getTime(), false);
        itemDao.insert(item);
        return null;
    }
}
