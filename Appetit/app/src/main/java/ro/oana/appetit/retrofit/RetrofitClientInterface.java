package ro.oana.appetit.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ro.oana.appetit.model.Recipe;

public interface RetrofitClientInterface {
    @GET("/recipes")
    Call<List<Recipe>> getAllRecipes();

    @POST("/recipes")
    Call<Recipe> saveRecipe(@Body Recipe recipe);

    @DELETE("/recipes/{id}")
    Call<Recipe> deleteRecipe(@Path("id") int id);

    @PUT("/recipes")
    Call<Recipe> updateRecipe(@Body Recipe recipe);
}
