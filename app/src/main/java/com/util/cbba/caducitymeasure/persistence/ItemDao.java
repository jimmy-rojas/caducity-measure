package com.util.cbba.caducitymeasure.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Date;
import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item WHERE id = :id")
    void deleteById(long id);

    @Query("DELETE FROM item")
    void deleteAll();

    @Query("SELECT * from item ORDER BY name ASC")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * from item ORDER BY expiration_date ASC")
    LiveData<List<Item>> getAllItemsByExpiration();

    @Query("SELECT * from item WHERE expiration_date > DATETIME('now') ORDER BY expiration_date ASC")
    LiveData<List<Item>> getAllItemsByExpirationNext();

    @Query("SELECT * FROM item WHERE expiration_date = :date")
    LiveData<List<Item>> findItemsExpireAt(Date date);

    @Query("SELECT * FROM item WHERE expiration_date > DATETIME('now') LIMIT 1")
    LiveData<Item> findItemExpireNext();
}
