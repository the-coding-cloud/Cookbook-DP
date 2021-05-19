package ro.oana.appetit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.oana.appetit.adapters.RecipeAdapter;
import ro.oana.appetit.database.RecipeDatabase;
import ro.oana.appetit.model.Recipe;
import ro.oana.appetit.retrofit.RetrofitClient;
import ro.oana.appetit.utils.NetworkStateReceiver;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkStateReceiver.NetworkStateReceiverListener {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private NetworkStateReceiver networkStateReceiver;

    private FloatingActionButton addRecipe;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearProgressIndicator linearProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearProgressIndicator = findViewById(R.id.linearIndicator);
        linearProgressIndicator.show();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        RecipeDatabase.getAppDatabase(this);

        addRecipe = findViewById(R.id.floating_action_button);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        syncWithRemote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setItems(RecipeDatabase.getAppDatabase().RecipeDao().getAll());
        adapter.notifyDataSetChanged();
        //syncWithRemote();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
        linearProgressIndicator.show();

        syncWithRemote();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    @Override
    public void networkAvailable() {
        List<Recipe> items = RecipeDatabase.getAppDatabase().RecipeDao().getAll();
        Toast.makeText(getApplicationContext(), "Refreshing...", Toast.LENGTH_LONG).show();

        for(Recipe item: items){
            if (!item.isSynced()){
                RetrofitClient.getRetrofitClientInstance().saveRecipe(item).enqueue(
                        new Callback<Recipe>() {
                            @Override
                            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                                if(response.code() != 200) {
                                    Log.d("REFRESH", String.valueOf(response.body()));
                                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    RecipeDatabase.getAppDatabase().RecipeDao().delete(item);
                                    return;
                                }

                                RecipeDatabase.getAppDatabase().RecipeDao().delete(item);
                                RecipeDatabase.getAppDatabase().RecipeDao().insertOne(response.body());
                                Toast.makeText(getApplicationContext(), "Recipe added", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.d("REFRESH", "Refresh failed");
                                Toast.makeText(getApplicationContext(), "You are offline or the server cannot be reached!", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        }
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this, "Lost connection", Toast.LENGTH_SHORT).show();
    }

    private void syncWithRemote() {
        adapter = new RecipeAdapter(this, linearProgressIndicator);
        recyclerView.setAdapter(adapter);
    }
}