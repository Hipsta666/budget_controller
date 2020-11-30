package nsu.ui;

public class MySqlRepository implements TransactionRepository, CategoryRepository{
    @Override
    public Iterable<Transaction> findAll() {
        return null;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction findTransaction(Long id) {
        return null;
    }

    @Override
    public Category saveCategory(Category category) {
        return null;
    }
}
