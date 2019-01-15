package com.util.cbba.caducitymeasure.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

public class AddEntryViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    public AddEntryViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public void insert(Item it) { itemRepository.insert(it); }
}
