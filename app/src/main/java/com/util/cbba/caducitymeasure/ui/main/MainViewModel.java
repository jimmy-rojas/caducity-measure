package com.util.cbba.caducitymeasure.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.util.cbba.caducitymeasure.persistence.IItemDao;
import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.List;

public class MainViewModel extends AndroidViewModel implements IItemDao {

    private ItemRepository itemRepository;

    public MainViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    @Override
    public void insert(Item item) {

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
        return itemRepository.getAllItems();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpiration() {
        return itemRepository.getAllItemsByExpiration();
    }

    @Override
    public LiveData<Integer> isThereItemsToExpireNow() {
        return itemRepository.isThereItemsToExpireNow();
    }

    @Override
    public LiveData<List<Item>> getItemsToExpireNow() {
        return itemRepository.getItemsToExpireNow();
    }

    @Override
    public LiveData<List<Item>> getAllItemsByExpirationNext() {
        return itemRepository.getAllItemsByExpirationNext();
    }
}
