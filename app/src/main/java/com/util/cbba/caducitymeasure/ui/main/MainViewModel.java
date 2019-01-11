package com.util.cbba.caducitymeasure.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    private LiveData<List<Item>> allItems;

    public MainViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        allItems = itemRepository.getAllItems();
    }

    LiveData<List<Item>> getAllWords() { return allItems; }

    public void insert(Item it) { itemRepository.insert(it); }
}
