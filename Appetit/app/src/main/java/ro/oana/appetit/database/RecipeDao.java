package ro.oana.appetit.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ro.oana.appetit.model.Recipe;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Insert
    void insertAll(List<Recipe> recipes);

    @Query("DELETE FROM recipe")
    void nukeTable();

    @Insert
    void insertOne(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Update
    void update(Recipe recipe);
}
