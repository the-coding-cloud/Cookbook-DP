package ro.oana.appetit;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText difficultyEditText;
    EditText timeEditText;
    EditText ingredientsEditText;
    EditText instructionsEditText;

    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameEditText = findViewById(R.id.nameAddInput);
        timeEditText = findViewById(R.id.timeAddInput);
        difficultyEditText = findViewById(R.id.difficultyAddInput);
        ingredientsEditText = findViewById(R.id.ingredientsAddInput);
        instructionsEditText = findViewById(R.id.instructionsAddInput);

        addButton = findViewById(R.id.addRecipeBtn);
        addButton.setOnClickListener(addRecipe());
    }

    private View.OnClickListener addRecipe() {
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Recipe recipe = buildRecipe();
                serverCall(recipe);

                finish();
            }
        };
    }

    private void serverCall(Recipe recipe) {
        RetrofitClient.getRetrofitClientInstance().saveRecipe(recipe).enqueue(
                new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        if(response.code() != 200) {
                            Log.d("ADD ACTIVITY", String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Recipe remoteItem = response.body();
                        remoteItem.setSynced(true);
                        RecipeDatabase.getAppDatabase().RecipeDao().insertOne(remoteItem);
                        Toast.makeText(getApplicationContext(), "Recipe added", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("ADD ACTIVITY", "Call failed");
                        Toast.makeText(getApplicationContext(), "You are offline or the server cannot be reached!", Toast.LENGTH_LONG).show();

                        recipe.setSynced(false);
                        RecipeDatabase.getAppDatabase().RecipeDao().insertOne(recipe);


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