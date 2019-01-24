package com.util.cbba.caducitymeasure.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    public MainViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }
    public void update(Item item) {
        itemRepository.update(item);
    }

    public LiveData<List<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getAllItemsByExpiration() {
        return itemRepository.getAllItemsByExpiration();
    }

    public LiveData<List<Item>> getItemsToExpireNow() {
        return itemRepository.getItemsToExpireNow();
    }

    public LiveData<List<Item>> getAllItemsByExpirationNext() {
        return itemRepository.getAllItemsByExpirationNext();
    }

    public LiveData<List<Item>> getAllItemsByExpirationNext3Days() {
        return itemRepository.getAllItemsByExpirationNext3Days();
    }
}
