package ro.oana.appetit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ro.oana.appetit.model.Recipe;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase INSTANCE;

    public abstract RecipeDao RecipeDao();

    public static RecipeDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, "recipe-database")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static RecipeDatabase getAppDatabase() {
        if (INSTANCE == null) {
            return null;
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
