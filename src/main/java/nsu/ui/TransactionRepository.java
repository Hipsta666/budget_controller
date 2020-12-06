package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TransactionRepository {

    HashMap<String, HashMap<String, ArrayList<Transaction>>> grouping() throws SQLException;

    ArrayList<Integer> getDaySums() throws SQLException;

    HashMap<String, HashMap<String, Integer>> getCategorySums() throws SQLException;

    void saveTransaction(Transaction transaction) throws SQLException;

    Transaction findTransaction(Long id) throws SQLException;

    ArrayList<Category> findCategories();

    ArrayList<String> getDates();

    void updateTransaction(Transaction transaction) throws SQLException;

    void deleteTransaction(Long id) throws SQLException;

}
