package com.util.cbba.caducitymeasure.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.util.cbba.caducitymeasure.persistence.entity.Item;

@Database(entities = {Item.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CaducityMeasureDatabase extends RoomDatabase {

    private static final String TAG = CaducityMeasureDatabase.class.getSimpleName();
    private static volatile CaducityMeasureDatabase INSTANCE;

    public abstract IItemDao itemDao();

    static CaducityMeasureDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CaducityMeasureDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CaducityMeasureDatabase.class, "caducity_measure_database.db")
                        .addCallback(sRoomDatabaseCallback)
                        .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    Log.d(TAG, "DDBB ready for use");
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
}
