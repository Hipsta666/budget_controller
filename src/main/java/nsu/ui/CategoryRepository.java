package nsu.ui;

import java.util.ArrayList;

public interface CategoryRepository {
    Category saveCategory(Category category);

    ArrayList<Category> findCategories();
}
