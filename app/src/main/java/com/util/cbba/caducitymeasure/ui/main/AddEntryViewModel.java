package com.util.cbba.caducitymeasure.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.util.cbba.caducitymeasure.persistence.ItemRepository;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

public class AddEntryViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    private MutableLiveData<Long> newItemId;

    public AddEntryViewModel(Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public MutableLiveData<Long> insert(Item it) {
        newItemId = new MutableLiveData<>();
        itemRepository.insert(it, newItemId);
        return newItemId;
    }

}
