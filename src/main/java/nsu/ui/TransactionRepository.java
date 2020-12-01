package nsu.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface TransactionRepository {

    HashMap<String, ArrayList<Transaction>> grouping() throws SQLException;

    ArrayList<Integer> getSums() throws SQLException;

    Transaction saveTransaction(Transaction transaction) throws SQLException;

    Transaction findTransaction(Long id);
}
