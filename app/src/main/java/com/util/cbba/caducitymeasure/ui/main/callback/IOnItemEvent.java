package com.util.cbba.caducitymeasure.ui.main.callback;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

public interface IOnItemEvent {
    void onItemResolve(Item item);
}
