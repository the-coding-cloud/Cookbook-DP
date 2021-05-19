package ro.oana.appetit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.oana.appetit.database.RecipeDatabase;
import ro.oana.appetit.model.Recipe;
import ro.oana.appetit.retrofit.RetrofitClient;

public class EditActivity extends AppCompatActivity {

    Recipe recipe;
    EditText nameEditText;
    EditText difficultyEditText;
    EditText timeEditText;
    EditText ingredientsEditText;
    EditText instructionsEditText;

    Button updateButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("recipe")) {
            recipe = intent.getParcelableExtra("recipe");
        }

        nameEditText = findViewById(R.id.nameEditInput);
        difficultyEditText = findViewById(R.id.difficultyEditInput);
        timeEditText = findViewById(R.id.timeEditInput);
        ingredientsEditText = findViewById(R.id.ingredientsEditInput);
        instructionsEditText = findViewById(R.id.instructionsEditInput);

        nameEditText.setText(recipe.getName());
        difficultyEditText.setText(recipe.getDifficulty());
        timeEditText.setText(String.valueOf(recipe.getTime()));
        ingredientsEditText.setText(recipe.getIngredients());
        instructionsEditText.setText(recipe.getInstructions());

        updateButton = findViewById(R.id.updateRecipeBtn);
        deleteButton = findViewById(R.id.deleteRecipeBtn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Recipe updatedRecipe = buildRecipe();
                editServerCall(updatedRecipe);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteServerCall(recipe.getId());
                finish();
            }
        });

    }

    private void editServerCall(Recipe recipe) {
        RetrofitClient.getRetrofitClientInstance().updateRecipe(recipe).enqueue(
                new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        if(response.code() != 200) {
                            Log.d("EDIT ACTIVITY", String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Recipe remoteItem = response.body();
                        remoteItem.setSynced(true);
                        RecipeDatabase.getAppDatabase().RecipeDao().update(remoteItem);
                        Toast.makeText(getApplicationContext(), "Recipe updated", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("EDIT ACTIVITY", "Call failed");
                        Toast.makeText(getApplicationContext(), "You are offline or the server cannot be reached!", Toast.LENGTH_LONG).show();

                        recipe.setSynced(false);
                        RecipeDatabase.getAppDatabase().RecipeDao().update(recipe);
                    }
                }
        );
    }

    private void deleteServerCall(int id) {
        RetrofitClient.getRetrofitClientInstance().deleteRecipe(id).enqueue(
                new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        if(response.code() != 200) {
                            Log.d("EDIT ACTIVITY", String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        RecipeDatabase.getAppDatabase().RecipeDao().delete(recipe);
                        Toast.makeText(getApplicationContext(), "Recipe deleted", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("EDIT ACTIVITY", "Call failed");
                        Toast.makeText(getApplicationContext(), "You are offline or the server cannot be reached!", Toast.LENGTH_LONG).show();

                    }
                }
        );
    }


    private Recipe buildRecipe() {
        return new Recipe(
                nameEditText.getText().toString(),
                difficultyEditText.getText().toString(),
                Integer.parseInt(timeEditText.getText().toString()),
                ingredientsEditText.getText().toString(),
                instructionsEditText.getText().toString()
        );
    }
}