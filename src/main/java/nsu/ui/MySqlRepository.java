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
        ArrayList<String> dates = getDates();
        ArrayList<Transaction> transactions = getTransactions();
        ArrayList<String> categories = getCategories();
        HashMap<String, HashMap<String, ArrayList<Transaction>>> datesCategory = new HashMap<String, HashMap<String, ArrayList<Transaction>>>();


        for (String date:dates){
            HashMap<String, ArrayList<Transaction>> categoryTransaction = new HashMap<String, ArrayList<Transaction>>();
            for (String category :categories){
                ArrayList<Transaction> transactionByCategory = new ArrayList<Transaction>();
                for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                    Transaction transaction = iterator.next();
                    if (category.equals(transaction.getCategory()) && date.equals(transaction.getDate())){

                        transactionByCategory.add(transaction);
                        iterator.remove();
                        System.out.println(transaction);
                    }
                }
                if (!transactionByCategory.isEmpty()) categoryTransaction.put(category, transactionByCategory);
            }
            datesCategory.put(date, categoryTransaction);
        }

        return datesCategory;
    }


    @Override
    public ArrayList<Integer> getDaySums() throws SQLException {
        state = con.createStatement();

        ArrayList<Integer> sums = new ArrayList<Integer>();
        for (String date:getDates()){
            ResultSet sum = state.executeQuery("select sum(amount) from transactions where date='" + date + "';");
            while (sum.next()) {
                sums.add(sum.getInt(1));
            }
        }
        return sums;
    }

    @Override
    public HashMap<String, HashMap<String, Integer>> getCategorySums() throws SQLException {
        state = con.createStatement();

        ArrayList<String> categories = getCategories();
        HashMap<String, HashMap<String, Integer>> categorySums = new HashMap<>();
        for (String date:getDates()){
            HashMap<String, Integer> sums = new HashMap<>();
            for (String category:categories){

                ResultSet sum = state.executeQuery("select sum(transactions.amount) from transactions, categories where categories.id = transactions.category_id and transactions.date = '" + date + "' and categories.category_name='" + category + "';");
                while (sum.next()) {
                    if (sum.getInt(1) != 0) sums.put(category, sum.getInt(1));
                }

            }
            categorySums.put(date, sums);

        }
        return categorySums;
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


    public ArrayList<Transaction> getTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            state = con.createStatement();
            ResultSet rsTransaction = state.executeQuery("select transactions.id, transactions.date, categories.category_name, transactions.trans_name, transactions.amount from transactions, categories where categories.id = transactions.category_id;");
            while (rsTransaction.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rsTransaction.getLong(1));
                transaction.setDate(rsTransaction.getString(2));
                transaction.setCategory(rsTransaction.getString(3));
                transaction.setTrans_name(rsTransaction.getString(4));
                transaction.setAmount(rsTransaction.getInt(5));
                transactions.add(transaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return transactions;
    }

    public ArrayList<String> getCategories() throws SQLException {
        state = con.createStatement();

        ArrayList<String> categories = new ArrayList<>();
        ResultSet uniqueCategory = state.executeQuery("select distinct transactions.category_id, categories.category_name from transactions, categories where categories.id = transactions.category_id;");
        while (uniqueCategory.next()) {
            categories.add(uniqueCategory.getString(2));
        }
        

        return categories;
    }

    @Override
    public ArrayList<String> getDates(){
        ArrayList<String> dates = new ArrayList<String>();
        try {
            state = con.createStatement();

            ResultSet rsDates = state.executeQuery("select distinct date from transactions;");
            while (rsDates.next()) {
                dates.add(rsDates.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dates;
    }

}
