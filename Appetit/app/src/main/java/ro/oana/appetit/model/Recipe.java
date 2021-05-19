package ro.oana.appetit.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Recipe implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "difficulty")
    private String difficulty;

    @ColumnInfo(name = "time")
    private int time;

    @ColumnInfo(name = "ingredients")
    private String ingredients;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "synced", defaultValue = "false")
    private boolean synced;

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        difficulty = in.readString();
        time = in.readInt();
        ingredients = in.readString();
        instructions = in.readString();
        synced = in.readByte() != 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(int id, String name, String difficulty, int time, String ingredients, String instructions) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe(String name, String difficulty, int time, String ingredients, String instructions) {
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(difficulty);
        dest.writeInt(time);
        dest.writeString(ingredients);
        dest.writeString(instructions);
        dest.writeByte((byte) (synced ? 1 : 0));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, difficulty, time, ingredients, instructions, synced);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", time=" + time +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }
}
