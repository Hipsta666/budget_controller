package nsu.ui;



import org.javatuples.Pair;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TransactionRepository {

    HashMap<String, HashMap<String, ArrayList<Transaction>>> grouping() throws SQLException, ParseException;

    ArrayList<Integer> getDaySums() throws SQLException;

    HashMap<String, HashMap<String, Integer>> getCategorySums() throws SQLException;

    void saveTransaction(Transaction transaction) throws SQLException;

    Transaction findTransaction(Long id) throws SQLException;

    ArrayList<Category> findCategories();

    ArrayList<String> getDates();

    boolean checkDB(String dbName, String field, String value) throws SQLException;

    void updateTransaction(Transaction transaction) throws SQLException;

    void deleteTransaction(Long id) throws SQLException;
    Pair<Integer, HashMap<String, Integer>> getStatGroupedByDates(String groupBy) throws ParseException, SQLException;

    User findUser(String login);

    void setGlobalUserId(Long user_id);
    boolean checkDBint(String field, Integer value) throws SQLException;

}
