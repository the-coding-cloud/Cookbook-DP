package ro.oana.appetit.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.oana.appetit.EditActivity;
import ro.oana.appetit.R;
import ro.oana.appetit.database.RecipeDatabase;
import ro.oana.appetit.model.Recipe;
import ro.oana.appetit.retrofit.RetrofitClient;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final Context context;
    private List<Recipe> items;
    private final LinearProgressIndicator linearProgressIndicator;

    public RecipeAdapter(Context context, LinearProgressIndicator linearProgressIndicator) {
        this.context = context;
        this.items = new ArrayList<>();
        this.linearProgressIndicator = linearProgressIndicator;

        serverCall(context);
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe item = items.get(position);

        holder.textViewName.setText(String.valueOf(item.getName()));
        holder.textViewDifficulty.setText(String.valueOf(item.getDifficulty()));
        holder.textViewTime.setText(String.valueOf(item.getTime()));
        holder.textViewIngredients.setText(String.valueOf(item.getIngredients()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("item", item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Recipe> items) {
        this.items = items;
    }

    public List<Recipe> getItems() {
        return items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDifficulty;
        private final TextView textViewTime;
        private final TextView textViewIngredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView parent = itemView.findViewById(R.id.parent);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDifficulty = itemView.findViewById(R.id.text_view_difficulty);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewIngredients = itemView.findViewById(R.id.text_view_ingredients);
        }
    }

    private void serverCall(Context context) {
        RetrofitClient.getRetrofitClientInstance().getAllRecipes().enqueue(
                new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        if(response.code() != 200){
                            Log.d("RECIPE ADAPTER", String.valueOf(response.body()));
                            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        RecipeDatabase.getAppDatabase().RecipeDao().nukeTable();

                        for(Recipe item: response.body()){
                            item.setSynced(true);
                            items.add(item);
                            RecipeDatabase.getAppDatabase().RecipeDao().insertOne(item);
                        }

                        linearProgressIndicator.hide();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Log.d("RECIPE ADAPTER", "server call failed");
                        Toast.makeText(context, "You are offline or the server is not reachable", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
