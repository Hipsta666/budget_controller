package nsu.ui;

import org.javatuples.Pair;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MySqlRepository implements TransactionRepository, CategoryRepository, UserRepository {
    public static final String url = "jdbc:mysql://localhost:3306/expense_controller?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String user = "root";
    public static final String pwd = "";

    Statement state;
    Connection con;

    public MySqlRepository() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, user, pwd);
    }


    @Override
    public HashMap<String, HashMap<String, ArrayList<Transaction>>> grouping() throws SQLException, ParseException {
        state = con.createStatement();
        ArrayList<String> dates = getDates();
        ArrayList<Transaction> transactions = getTransactions("select transactions.id, transactions.date, transactions.category_id, transactions.trans_name, transactions.amount from transactions, categories where categories.id = transactions.category_id;");

        ArrayList<Category> categories = findCategories();
        HashMap<String, HashMap<String, ArrayList<Transaction>>> datesCategory = new HashMap<String, HashMap<String, ArrayList<Transaction>>>();


        for (String date : dates) {
            HashMap<String, ArrayList<Transaction>> categoryTransaction = new HashMap<String, ArrayList<Transaction>>();
            for (Category category : categories) {
                ArrayList<Transaction> transactionByCategory = new ArrayList<Transaction>();
                for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
                    Transaction transaction = iterator.next();
                    if (category.getId().equals(transaction.getCategory_id()) && date.equals(transaction.getDate())) {

                        transactionByCategory.add(transaction);
                        iterator.remove();
                    }
                }
                if (!transactionByCategory.isEmpty())
                    categoryTransaction.put(category.getCategoryName(), transactionByCategory);
            }
            datesCategory.put(date, categoryTransaction);
        }

        return datesCategory;
    }


    @Override
    public ArrayList<Integer> getDaySums() throws SQLException {
        state = con.createStatement();

        ArrayList<Integer> sums = new ArrayList<Integer>();
        for (String date : getDates()) {
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


        ArrayList<Category> categories = findCategories();
        HashMap<String, HashMap<String, Integer>> categorySums = new HashMap<>();
        for (String date : getDates()) {
            HashMap<String, Integer> sums = new HashMap<>();
            for (Category category : categories) {

                ResultSet sum = state.executeQuery("select sum(transactions.amount) from transactions, categories where categories.id = transactions.category_id and transactions.date = '" + date + "' and categories.category_name='" + category.getCategoryName() + "';");
                while (sum.next()) {
                    sums.put(category.getCategoryName(), sum.getInt(1));
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




    @Override
    public boolean checkDB(String dbName, String field, String value) throws SQLException {
        state = con.createStatement();

        ResultSet rs = state.executeQuery(String.format("SELECT EXISTS(SELECT id FROM `%s` WHERE LOWER(%s) = LOWER('%s'));", dbName, field, value));
        while (rs.next()) {
            if (rs.getString(1).equals("0")) {
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
    public Transaction findTransaction(Long id) throws SQLException {
        Transaction transaction = new Transaction();
        state = con.createStatement();
        ResultSet rs = state.executeQuery("select * from transactions where id=" + id + ";");

        while (rs.next()) {
            transaction.setId(rs.getLong(1));
            transaction.setDate(rs.getString(2));
            transaction.setCategory_id(rs.getLong(3));
            transaction.setTrans_name(rs.getString(4));
            transaction.setAmount(rs.getInt(5));

        }
        return transaction;
    }


    @Override
    public ArrayList<Category> findCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        try {
            state = con.createStatement();
            ResultSet rsCategory = state.executeQuery("select distinct * from categories;");

            while (rsCategory.next()) {
                Category category = new Category();

                category.setCategoryName(rsCategory.getString(2));
                category.setId(rsCategory.getLong(1));

                categories.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return categories;
    }


    public ArrayList<Transaction> getTransactions(String query) {

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            state = con.createStatement();
            ResultSet rsTransaction = state.executeQuery(query);
            while (rsTransaction.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rsTransaction.getLong(1));
                transaction.setDate(rsTransaction.getString(2));
                transaction.setCategory_id(rsTransaction.getLong(3));
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
    public ArrayList<String> getDates() {

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

    @Override
    public void updateTransaction(Transaction transaction) throws SQLException {

        state.executeUpdate("update transactions set date='" + transaction.getDate() + "', category_id='" + transaction.getCategory_id() + "'," +
                " trans_name='" + transaction.getTrans_name() + "', amount=" + transaction.getAmount() + " where id=" + transaction.getId() + ";");
    }

    @Override
    public void deleteTransaction(Long id) throws SQLException {
        state.executeUpdate("delete from transactions where id=" + id + ";");
    }

    @Override
    public void deleteCategory(String category) throws SQLException {
        state.executeUpdate("delete from categories where category_name='" + category + "';");
    }


    public int getWeek(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(date));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public int getMonth(String date) {
        String month = date.split("-")[1];
        return Integer.parseInt(month);
    }

    @Override
    public Pair<Integer, HashMap<String, Integer>> getStatGroupedByDates(String groupBy) throws ParseException, SQLException {
        int sum = 0;
        int categorySum = 0;
        ArrayList<String> group = new ArrayList<>();
        HashMap<String, Integer> categoryValue = new HashMap<>();
//        HashMap<Integer, HashMap<String, Integer>> result = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> dateCategoryValue = getGroupedCategoriesByValue();
        ArrayList<String> dates = getDates();
        if (dates.isEmpty()){
            dates.add("0000-00-00");
        }
        int curr;

        switch (groupBy) {
            case "w":
                curr = getWeek(dates.get(0));
                for (String date : dates) {
                    if (getWeek(date) == curr) {
                        group.add(date);
                    }
                }
                break;
            case "m":
                curr = getMonth(dates.get(0));
                for (String date : dates) {
                    if (getMonth(date) == curr) {
                        group.add(date);
                    }
                }
                break;
            case "d":
                group.add(dates.get(0));
                break;
        }

        dateCategoryValue.keySet().removeIf(date -> !group.contains(date));
//        OR
//        for (Iterator<String> iterator = dateCategoryValue.keySet().iterator();iterator.hasNext();){
//            String date = iterator.next();
//            if (!group.contains(date)){
//                iterator.remove();
//            }
//        }

        for (HashMap<String, Integer> val: dateCategoryValue.values()){
            for (String category: val.keySet()){
                if (!categoryValue.containsKey(category)){
                    sum = val.get(category);
                }
                else {
                    sum = categoryValue.get(category) + val.get(category);
                }
                categoryValue.put(category, sum);
            }
        }
        for (Integer amount: categoryValue.values()){
            categorySum += amount;
        }


        return new Pair<Integer, HashMap<String, Integer>>(categorySum, categoryValue);
    }

    public HashMap<String, HashMap<String, Integer>> getGroupedCategoriesByValue() throws SQLException {
        HashMap<String, HashMap<String, Integer>> dateCategoryValue = new HashMap<>();
        HashMap<String, Integer> categoryValue = new HashMap<>();
        ResultSet rsDates = state.executeQuery("select categories.category_name, sum(transactions.amount), transactions.date from transactions, categories where amount < 0 and transactions.category_id = categories.id group by category_name, date;");
        while (rsDates.next()) {
            if (dateCategoryValue.containsKey(rsDates.getString(3))){
                dateCategoryValue.get(rsDates.getString(3)).put(rsDates.getString(1), rsDates.getInt(2));

            }
            else {
                categoryValue.put(rsDates.getString(1), rsDates.getInt(2));
                dateCategoryValue.put(rsDates.getString(3), categoryValue);
                categoryValue = new HashMap<>();
            }
        }
        return dateCategoryValue;
    }

    @Override
    public User saveUser(User user) throws SQLException {
        String name = user.getUserName();
        String login = user.getUserLogin();
        String password = user.getUserPassword();
        Boolean current = true;

        state = con.createStatement();
        boolean categoryExistence = checkDB("users", "user_login", String.valueOf(name));
        if (!categoryExistence) {
            state.executeUpdate(String.format("INSERT into `users`(user_name, user_login, password, current) VALUES ('%s','%s','%s','%s');", name,login,password,current));
        }
        return user;
    }

    @Override
    public User findUser() {
        User user = new User();
        try {
            state = con.createStatement();
            ResultSet rsUser = state.executeQuery("select distinct * from users;");

            while (rsUser.next()) {
                user.setCurrent(rsUser.getBoolean(5));
                user.setUserPassword(rsUser.getString(4));
                user.setUserLogin(rsUser.getString(3));
                user.setUserName(rsUser.getString(2));
                user.setId(rsUser.getLong(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }
}