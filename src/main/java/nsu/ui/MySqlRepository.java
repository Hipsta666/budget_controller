package nsu.ui;

import java.sql.*;
import java.util.*;

public class MySqlRepository implements TransactionRepository, CategoryRepository{
    public static final String url = "jdbc:mysql://localhost:3306/expense_controller?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
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

        HashMap<Long, String> categories = getCategories();
        HashMap<String, HashMap<String, ArrayList<Transaction>>> datesCategory = new HashMap<String, HashMap<String, ArrayList<Transaction>>>();


        for (String date:dates){
            HashMap<String, ArrayList<Transaction>> categoryTransaction = new HashMap<String, ArrayList<Transaction>>();
            for (Long id :categories.keySet()){
                ArrayList<Transaction> transactionByCategory = new ArrayList<Transaction>();
                for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                    Transaction transaction = iterator.next();
                    if (id.equals(transaction.getCategory_id()) && date.equals(transaction.getDate())){

                        transactionByCategory.add(transaction);
                        iterator.remove();
                    }
                }
                if (!transactionByCategory.isEmpty()) categoryTransaction.put(categories.get(id), transactionByCategory);
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


        HashMap<Long, String> categories = getCategories();
        HashMap<String, HashMap<String, Integer>> categorySums = new HashMap<>();
        for (String date:getDates()){
            HashMap<String, Integer> sums = new HashMap<>();
            for (Long id:categories.keySet()){

                ResultSet sum = state.executeQuery("select sum(transactions.amount) from transactions, categories where categories.id = transactions.category_id and transactions.date = '" + date + "' and categories.category_name='" + categories.get(id) + "';");
                while (sum.next()) {
                    sums.put(categories.get(id), sum.getInt(1));
                }

            }
            categorySums.put(date, sums);

        }
        return categorySums;
    }
    
    @Override
    public Category saveCategory(Category category) throws SQLException {
        String name = category.getCategoryName();
        state = con.createStatement();
        boolean categoryExistence = checkDB("categories", "category_name", String.valueOf(name));
        if (!categoryExistence) {
            state.executeUpdate(String.format("INSERT into `categories`(category_name) VALUES ('%s');", name));
        }
        return category;
    }

    public boolean checkDB(String dbName, String field, String value) throws SQLException {
        state = con.createStatement();

        ResultSet rs = state.executeQuery(String.format("SELECT EXISTS(SELECT id FROM `%s` WHERE LOWER(%s) = LOWER('%s'));", dbName, field, value));
        while (rs.next()){
            if (rs.getString(1).equals("0")){
                rs.close();
                return false;
            }
        }
        return true;
    }


    @Override
    public void saveTransaction(Transaction transaction) throws SQLException {
        String name = transaction.getTrans_name();
        String date = transaction.getDate();
        int amount = transaction.getAmount();
        Long id_category = transaction.getCategory_id();

        state = con.createStatement();
        state.executeUpdate(String.format("INSERT into transactions (date,category_id,trans_name, amount) VALUES ('%s','%s','%s','%s');", date, id_category, name, amount));
    }

    @Override
    public Transaction findTransaction(Long id) {
        return new Transaction();
    }

    @Override
    public HashMap<Long, String> getCategories() throws SQLException {
        state = con.createStatement();

        HashMap<Long, String> categories = new HashMap<>();
        ResultSet uniqueCategory = state.executeQuery("select distinct transactions.category_id, categories.category_name from transactions, categories where categories.id = transactions.category_id;");
        while (uniqueCategory.next()) {
            categories.put(uniqueCategory.getLong(1), uniqueCategory.getString(2));
        }
        return categories;
    }

    @Override
    public ArrayList<Category> findCategories(){
        ArrayList<Category> transactions = new ArrayList<Category>();
        try {
            state = con.createStatement();
            ResultSet rsCategory = state.executeQuery("select distinct * from categories;");

            while (rsCategory.next()) {
                Category category = new Category();

                category.setCategoryName(rsCategory.getString(2));
                category.setId(rsCategory.getInt(1));

                transactions.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return transactions;
    }


    public ArrayList<Transaction> getTransactions(){

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            state = con.createStatement();
            ResultSet rsTransaction = state.executeQuery("select transactions.id, transactions.date, transactions.category_id, transactions.trans_name, transactions.amount from transactions, categories where categories.id = transactions.category_id;");
            while (rsTransaction.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rsTransaction.getLong(1));
                transaction.setDate(rsTransaction.getString(2));
                transaction.setCategory_id(rsTransaction.getLong(3));
//                transaction.setCategory_id(rsTransaction.getLong(3));

                transaction.setTrans_name(rsTransaction.getString(4));
                transaction.setAmount(rsTransaction.getInt(5));
                transactions.add(transaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return transactions;
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
            dates.sort(new Comparator<String>() {
                @Override
                public int compare(String object1, String object2) {
                    return object2.compareTo(object1);
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dates;
    }

}
