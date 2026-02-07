package repo;

import model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByCategory(String category);

    List<Expense> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Expense e WHERE e.transactionDate >= :startDate AND e.transactionDate <= :endDate")
    List<Expense> findExpensesByDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getCategorySummary();

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " +
            "WHERE e.transactionDate >= :startDate AND e.transactionDate <= :endDate " +
            "GROUP BY e.category")
    List<Object[]> getCategorySummaryByDateRange(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
}