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

    void updateTransaction(Transaction transaction) throws SQLException;

    void deleteTransaction(Long id) throws SQLException;
    Pair<Integer, HashMap<String, Integer>> getStatGroupedByDates(String groupBy) throws ParseException, SQLException;

}
