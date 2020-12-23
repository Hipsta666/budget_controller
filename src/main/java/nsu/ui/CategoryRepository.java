package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoryRepository {
    Category saveCategory(Category category) throws SQLException;

    boolean checkDB(String dbName, String field, String value) throws SQLException;

    ArrayList<Category> findCategories();

    void deleteCategory(String category) throws SQLException;

    User findUser(String login);

    void setGlobalUserId(Long user_id);

    boolean checkDBint(String field, Integer value) throws SQLException;
}
