package com.util.cbba.caducitymeasure.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

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
    public LiveData<Integer> isThereItemsToExpireNow() {
        return itemDao.isThereItemsToExpireNow();
    }

    @Override
    public LiveData<List<Item>> getItemsToExpireNow() {
        return itemDao.getItemsToExpireNow();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpirationNext() {
        return itemDao.getAllItemsByExpirationNext();
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
}
