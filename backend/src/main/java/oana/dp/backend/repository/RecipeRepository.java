package oana.dp.backend.repository;

import oana.dp.backend.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
