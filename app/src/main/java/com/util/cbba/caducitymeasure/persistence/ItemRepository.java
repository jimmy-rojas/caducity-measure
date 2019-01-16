package com.util.cbba.caducitymeasure.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;
import java.util.List;

public class ItemRepository {

    private static final String TAG = ItemRepository.class.getSimpleName();
    private ItemDao itemDao;
    private LiveData<List<Item>> iteList;

    public ItemRepository(Application application) {
        CaducityMeasureDatabase db = CaducityMeasureDatabase.getDatabase(application);
        itemDao = db.itemDao();
        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 1);
        Log.d(TAG, "date in query. " + calendar.getTime());*/
    }

    public LiveData<List<Item>> getAllItems() {
        iteList = itemDao.getAllItems();
        return iteList;
    }

    public void insert(Item item) {
        new InsertAsyncTask(itemDao).execute(item);
    }

    public LiveData<Integer> isThereItemsToExpire() {
        return itemDao.isThereItemsToExpireNow();
    }

    private static class InsertAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao mAsyncTaskDao;

        InsertAsyncTask(ItemDao itemDao) {
            mAsyncTaskDao = itemDao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
