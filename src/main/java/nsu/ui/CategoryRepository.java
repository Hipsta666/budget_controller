package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoryRepository {
    Category saveCategory(Category category) throws SQLException;

    ArrayList<Category> findCategories();

    void deleteCategory(Category category) throws SQLException;
}
