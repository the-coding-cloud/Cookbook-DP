package oana.dp.backend.service;

import oana.dp.backend.domain.Recipe;
import oana.dp.backend.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    private static final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getAll() {
        log.info("getAll - method entered");
        return recipeRepository.findAll();
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        log.info("saveRecipe - method entered");
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        log.info("updateRecipe - method entered");

        Recipe updatedRecipe = recipeRepository.getOne(recipe.getId());

        updatedRecipe.setDifficulty(recipe.getDifficulty());
        updatedRecipe.setIngredients(recipe.getIngredients());
        updatedRecipe.setInstructions(recipe.getInstructions());
        updatedRecipe.setTime(recipe.getTime());

        return recipeRepository.save(updatedRecipe);
    }

    @Override
    public Optional<Recipe> getOne(int id) {
        log.info("getOne - method entered");
        return recipeRepository.findById(id);
    }

    @Override
    public boolean deleteRecipe(int id) {
        log.info("deleteRecipe - method entered");
        recipeRepository.deleteById(id);

        return true;
    }
}
