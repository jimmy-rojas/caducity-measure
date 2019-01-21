package com.util.cbba.caducitymeasure.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemRepository implements IItemDao {

    private static final String TAG = ItemRepository.class.getSimpleName();
    private IItemDao itemDao;

    public ItemRepository(Application application) {
        CaducityMeasureDatabase db = CaducityMeasureDatabase.getDatabase(application);
        itemDao = db.itemDao();
    }

    @Override
    public void insert(Item item) {
        new InsertAsyncTask(itemDao).execute(item);
    }

    @Override
    public void delete(Item item) {

    }

    @Override
    public void update(Item item) {
        new UpdateAsyncTask(itemDao).execute(item);
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpiration() {
        return itemDao.getAllItemsByExpiration();
    }

    @Override
    public LiveData<List<Item>> getItemsToExpireNow() {
        return itemDao.getItemsToExpireNow();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpirationNext() {
        return itemDao.getAllItemsByExpirationNext();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpirationNext3Days(Date from, Date to) {
        return itemDao.getAllItemsByExpirationNext3Days(from, to);
    }

    @Override
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

    private static class InsertAsyncTask extends AsyncTask<Item, Void, Void> {

        private IItemDao mAsyncTaskDao;

        InsertAsyncTask(IItemDao itemDao) {
            mAsyncTaskDao = itemDao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
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
