package nsu.ui;

import java.sql.*;
import java.util.*;

public class MySqlRepository implements TransactionRepository, CategoryRepository{
    public static final String url = "jdbc:mysql://localhost:3306/expense_controller";
    public static final String user = "root";
    public static final String pwd = "Qazwsxqwerty123";
    Statement state;
    Connection con;

    public MySqlRepository() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, user, pwd);
    }

    @Override
    public HashMap<String, HashMap<String, ArrayList<Transaction>>> grouping() throws SQLException {
        state = con.createStatement();
        ArrayList<String> dates = getDates("select distinct date from transactions;");
        ArrayList<Transaction> transactions = getTransactions("select * from transactions;");
        HashMap<String, HashMap<String, ArrayList<Transaction>>> datesCategory = new HashMap<String, HashMap<String, ArrayList<Transaction>>>();

        ResultSet uniqueCategory = state.executeQuery("select distinct transactions.category_id, categories.category_name from transactions, categories where categories.id = transactions.category_id;");
        HashMap<Integer, String> id_category = new HashMap<Integer, String>();

        while (uniqueCategory.next()) {
            id_category.put(uniqueCategory.getInt(1), uniqueCategory.getString(2));
        }

        for (String date:dates){
            HashMap<String, ArrayList<Transaction>> categoryTransaction = new HashMap<String, ArrayList<Transaction>>();
            for (int id:id_category.keySet()){
                ArrayList<Transaction> transactionByCategory = new ArrayList<Transaction>();
                String category = "";
                for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                    Transaction transaction = iterator.next();
                    if (id == transaction.getCategory_id() && date.equals(transaction.getDate())){
                        category = id_category.get(id);
                        transactionByCategory.add(transaction);
                        iterator.remove();
                    }
                }
                if (!category.equals("")) categoryTransaction.put(category, transactionByCategory);
            }
            datesCategory.put(date, categoryTransaction);
        }

        return datesCategory;
    }


    @Override
    public ArrayList<Integer> getSums() throws SQLException {
        state = con.createStatement();
        ArrayList<Integer> sums = new ArrayList<Integer>();

        for (String date:getDates("select distinct date from transactions;")){
            ResultSet sum = state.executeQuery("select sum(amount) from transactions where date='" + date + "';");
            while (sum.next()) {
                sums.add(sum.getInt(1));
            }
        }
        return sums;
    }






    @Override
    public Transaction saveTransaction(Transaction transaction) throws SQLException {
        return new Transaction();
    }

    @Override
    public Transaction findTransaction(Long id) {
        return new Transaction();
    }

    @Override
    public Category saveCategory(Category category) {
        return null;
    }





    public ArrayList<Transaction> getTransactions(String query){
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            state = con.createStatement();
            ResultSet rsTransaction = state.executeQuery(query);

            while (rsTransaction.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rsTransaction.getLong(1));
                transaction.setDate(rsTransaction.getString(2));
                transaction.setCategory_id(rsTransaction.getInt(3));
                transaction.setTrans_name(rsTransaction.getString(4));
                transaction.setAmount(rsTransaction.getInt(5));
                transactions.add(transaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return transactions;
    }

    public ArrayList<String> getDates(String query){
        ArrayList<String> dates = new ArrayList<String>();
        try {
            state = con.createStatement();
            ResultSet rsDates = state.executeQuery(query);

            while (rsDates.next()) {
                dates.add(rsDates.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        dates.sort(new Comparator<String>() {
            @Override
            public int compare(String object1, String object2) {
                return object2.compareTo(object1);
            }
        });
        return dates;
    }

}
