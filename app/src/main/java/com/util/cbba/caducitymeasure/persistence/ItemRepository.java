package com.util.cbba.caducitymeasure.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.List;

public class ItemRepository {

    private ItemDao itemDao;
    private LiveData<List<Item>> iteList;

    public ItemRepository(Application application) {
        CaducityMeasureDatabase db = CaducityMeasureDatabase.getDatabase(application);
        itemDao = db.itemDao();
        iteList = itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return iteList;
    }

    public void insert(Item item) {
        new insertAsyncTask(itemDao).execute(item);
    }

    private static class insertAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        insertAsyncTask(ItemDao itemDao) {
            mAsyncTaskDao = itemDao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
