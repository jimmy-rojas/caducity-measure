package com.util.cbba.caducitymeasure.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemRepository {

    private static final String TAG = ItemRepository.class.getSimpleName();
    private IItemDao itemDao;

    public ItemRepository(Application application) {
        CaducityMeasureDatabase db = CaducityMeasureDatabase.getDatabase(application);
        itemDao = db.itemDao();
    }

    public void update(Item item) {
        new UpdateAsyncTask(itemDao).execute(item);
    }

    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllItemsByExpiration() {
        return itemDao.getAllItemsByExpiration();
    }

    public LiveData<List<Item>> getItemsToExpireNow() {
        return itemDao.getItemsToExpireNow();
    }

    public LiveData<List<Item>> getAllItemsByExpirationNext() {
        return itemDao.getAllItemsByExpirationNext();
    }
    public LiveData<Integer> getAllItemsByExpirationNextNDaysPending(Date from, Date to) {
        return itemDao.getAllItemsByExpirationNextNDaysPending(from, to);
    }

    public LiveData<List<Item>> getAllItemsByExpirationNext3Days() {
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        Calendar to = Calendar.getInstance();
        to.add(Calendar.DATE, 3);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        return itemDao.getAllItemsByExpirationNext3Days(from.getTime(), to.getTime());
    }

    public void insert(Item item, MutableLiveData<Long> callback) {
        new InsertAsyncTask(itemDao, callback).execute(item);
    }

    private static class InsertAsyncTask extends AsyncTask<Item, Void, Long> {

        private IItemDao mAsyncTaskDao;
        private MutableLiveData<Long> callback;

        InsertAsyncTask(IItemDao itemDao, MutableLiveData<Long> callback) {
            mAsyncTaskDao = itemDao;
            this.callback = callback;
        }

        @Override
        protected Long doInBackground(final Item... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        protected void onPostExecute(Long result) {
            this.callback.setValue(result);
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Item, Void, Void> {

        private IItemDao mAsyncTaskDao;

        UpdateAsyncTask(IItemDao itemDao) {
            mAsyncTaskDao = itemDao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
