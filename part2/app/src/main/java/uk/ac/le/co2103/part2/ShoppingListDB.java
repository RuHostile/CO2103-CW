package uk.ac.le.co2103.part2;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;

@Database(entities = {ShoppingList.class, Product.class}, version = 4)
public abstract class ShoppingListDB extends RoomDatabase {

    public abstract ShoppingListDao shoppingListDao();

    private static ShoppingListDB INSTANCE;

    public static ShoppingListDB getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShoppingListDB.class, "ShoppingListDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
