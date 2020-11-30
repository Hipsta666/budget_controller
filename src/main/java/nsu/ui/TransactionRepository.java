package nsu.ui;

public interface TransactionRepository {
    /**  */
    Iterable<Transaction> findAll();

    /**  */
    Transaction saveTransaction(Transaction transaction);

    /**  */
    Transaction findTransaction(Long id);
}
