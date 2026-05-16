package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindCategory() {
        Category category = new Category();
        category.setCategoryID(9601);
        category.setCategoryName("Documentation");

        categoryRepository.saveAndFlush(category);

        Category found = categoryRepository.findById(9601).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getCategoryName()).isEqualTo("Documentation");
    }

    @Test
    void findAllShouldContainSavedCategory() {
        Category category = new Category();
        category.setCategoryID(9602);
        category.setCategoryName("Testing");

        categoryRepository.saveAndFlush(category);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories)
                .extracting(Category::getCategoryID)
                .contains(9602);
    }

    @Test
    void updateCategory() {
        Category category = new Category();
        category.setCategoryID(9603);
        category.setCategoryName("Design");
        categoryRepository.saveAndFlush(category);

        Category saved = categoryRepository.findById(9603).orElseThrow();
        saved.setCategoryName("UI Design");
        categoryRepository.saveAndFlush(saved);

        Category updated = categoryRepository.findById(9603).orElseThrow();
        assertThat(updated.getCategoryName()).isEqualTo("UI Design");
    }

    @Test
    void existsByIdShouldReturnTrueForSavedCategory() {
        Category category = new Category();
        category.setCategoryID(9604);
        category.setCategoryName("Marketing");
        categoryRepository.saveAndFlush(category);

        assertThat(categoryRepository.existsById(9604)).isTrue();
    }

    @Test
    void deleteByIdShouldRemoveCategory() {
        Category category = new Category();
        category.setCategoryID(9605);
        category.setCategoryName("Support");
        categoryRepository.saveAndFlush(category);

        categoryRepository.deleteById(9605);

        assertThat(categoryRepository.findById(9605)).isNotPresent();
    }
}