package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserRepository {
    User saveUser(User user) throws SQLException;

    boolean checkDB(String dbName, String field, String value) throws SQLException;

    User findUser();

    // void deleteUsers(String user) throws SQLException;
}
