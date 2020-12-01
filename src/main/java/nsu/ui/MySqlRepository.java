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
    public HashMap<String, ArrayList<Transaction>> grouping() throws SQLException {
        state = con.createStatement();
        ArrayList<String> dates = getDates("select distinct date from transactions;");
        ArrayList<Transaction> transactions = getTransactions("select transactions.id, transactions.date, categories.category_name, transactions.trans_name, transactions.amount from transactions, categories where categories.id = transactions.category_id;");
        HashMap<String, ArrayList<Transaction>> datesCategory = new HashMap<>();
        HashMap<String, ArrayList<Transaction>> dateCategory = new HashMap<>();

        ResultSet uniqueCategory = state.executeQuery("select distinct transactions.category_id, categories.category_name from transactions, categories where categories.id = transactions.category_id;");
        ArrayList<String> id_category = new ArrayList<>();

        while (uniqueCategory.next()) {
            id_category.add(uniqueCategory.getString(2));
        }

        for (String date:dates){
            ArrayList<Transaction> categoryTransaction = new ArrayList<>();
            for (String category:id_category){
                for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                    Transaction transaction = iterator.next();
                    if (category.equals(transaction.getCategory()) && date.equals(transaction.getDate())){
                        categoryTransaction.add(transaction);
                        iterator.remove();
                    }
                }
            }
            dateCategory.put(date, categoryTransaction);

        }
        System.out.println(dateCategory);
        for (String date:dateCategory.keySet()){
            System.out.println(date);
            for (Transaction tr:dateCategory.get(date)){
                System.out.println(tr.getCategory());
                System.out.println(tr.getTrans_name());
            }
        }
        return dateCategory;
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
