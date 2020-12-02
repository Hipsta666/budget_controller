package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TransactionRepository {

    HashMap<String, HashMap<String, ArrayList<Transaction>>> grouping() throws SQLException;

    ArrayList<Integer> getDaySums() throws SQLException;

    HashMap<String, HashMap<String, Integer>> getCategorySums() throws SQLException;

    Transaction saveTransaction(Transaction transaction) throws SQLException;

    Transaction findTransaction(Long id);

    ArrayList<String> getCategories() throws SQLException;

    ArrayList<String> getDates();
}
