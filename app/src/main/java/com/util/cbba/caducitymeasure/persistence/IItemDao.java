package com.util.cbba.caducitymeasure.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.Date;
import java.util.List;

@Dao
public interface IItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Query("SELECT COUNT() from item WHERE date(datetime(expiration_date / 1000 , 'unixepoch')) = date('now')")
    LiveData<Integer> isThereItemsToExpireNow();

    @Query("SELECT * from item WHERE date(datetime(expiration_date / 1000 , 'unixepoch')) = date('now')")
    LiveData<List<Item>> getItemsToExpireNow();

    @Query("SELECT * from item WHERE date(datetime(expiration_date / 1000 , 'unixepoch')) > date('now') ORDER BY expiration_date ASC")
    LiveData<List<Item>> getAllItemsByExpirationNext();

}
