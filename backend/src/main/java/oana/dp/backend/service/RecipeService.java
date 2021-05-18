package oana.dp.backend.service;

import oana.dp.backend.domain.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> getAll();

    Recipe saveRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe);

    Optional<Recipe> getOne(int id);

    boolean deleteRecipe(int id);
}
