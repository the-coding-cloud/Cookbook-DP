package oana.dp.backend.controllers;

import oana.dp.backend.domain.Recipe;
import oana.dp.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);


    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping()
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @PostMapping()
    public Recipe addRecipe(@RequestBody @Valid Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @DeleteMapping(path = "/{id}")
    public boolean deleteRecipe(@PathVariable int id) {
        return recipeService.deleteRecipe(id);
    }

    @PutMapping()
    public Recipe updateRecipe(@RequestBody @Valid Recipe recipe) {
        return recipeService.updateRecipe(recipe);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
