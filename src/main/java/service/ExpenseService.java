package service;

import dto.*;
import model.Expense;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repo.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseRepository expenseRepository;
    private final ClaudeAiService claudeAiService;

    public ExpenseService(ExpenseRepository expenseRepository, ClaudeAiService claudeAiService) {
        this.expenseRepository = expenseRepository;
        this.claudeAiService = claudeAiService;
    }

    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        log.info("Creating expense: {}", request.getDescription());

        // Use AI to categorize the expense
        AiCategorizationResult aiResult = claudeAiService.categorizeExpense(
                request.getDescription(),
                request.getAmount().toString()
        );

        // Create expense entity
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(aiResult.getCategory());
        expense.setSubCategory(aiResult.getSubCategory());
        expense.setMerchantName(aiResult.getMerchantName());
        expense.setConfidenceScore(aiResult.getConfidenceScore());
        expense.setTransactionDate(request.getTransactionDate() != null ?
                request.getTransactionDate() : LocalDateTime.now());
        expense.setManuallyUpdated(false);

        // Save to database
        Expense savedExpense = expenseRepository.save(expense);

        log.info("Expense created with ID: {} and category: {}",
                savedExpense.getId(), savedExpense.getCategory());

        return mapToResponse(savedExpense);
    }

    public List<ExpenseResponse> getAllExpenses() {
        log.info("Fetching all expenses");
        return expenseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id) {
        log.info("Fetching expense with ID: {}", id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));
        return mapToResponse(expense);
    }

    public List<ExpenseResponse> getExpensesByCategory(String category) {
        log.info("Fetching expenses by category: {}", category);
        return expenseRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponse updateCategory(Long id, CategoryUpdateRequest request) {
        log.info("Updating category for expense ID: {}", id);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));

        expense.setCategory(request.getCategory());
        expense.setSubCategory(request.getSubCategory());
        expense.setManuallyUpdated(true);

        Expense updatedExpense = expenseRepository.save(expense);

        log.info("Category updated to: {}", updatedExpense.getCategory());

        return mapToResponse(updatedExpense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        log.info("Deleting expense with ID: {}", id);

        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with ID: " + id);
        }

        expenseRepository.deleteById(id);
        log.info("Expense deleted successfully");
    }

    public ExpenseSummary getSummary() {
        log.info("Generating expense summary");

        List<Expense> allExpenses = expenseRepository.findAll();

        return generateSummary(allExpenses);
    }

    public ExpenseSummary getMonthlySummary(int year, int month) {
        log.info("Generating monthly summary for {}/{}", month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Expense> monthlyExpenses = expenseRepository.findByTransactionDateBetween(
                startDate, endDate
        );

        return generateSummary(monthlyExpenses);
    }

    public List<ExpenseResponse> getExpensesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching expenses between {} and {}", startDate, endDate);

        return expenseRepository.findByTransactionDateBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ExpenseSummary generateSummary(List<Expense> expenses) {
        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group by category and calculate totals
        List<ExpenseSummary.CategorySummary> categoryBreakdown = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ))
                .entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    double percentage = totalAmount.compareTo(BigDecimal.ZERO) > 0
                            ? amount.divide(totalAmount, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue()
                            : 0.0;

                    return new ExpenseSummary.CategorySummary(category, amount, percentage);
                })
                .sorted((a, b) -> b.getAmount().compareTo(a.getAmount()))
                .collect(Collectors.toList());

        ExpenseSummary summary = new ExpenseSummary();
        summary.setTotalAmount(totalAmount);
        summary.setTotalExpenses(expenses.size());
        summary.setCategoryBreakdown(categoryBreakdown);

        return summary;
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setSubCategory(expense.getSubCategory());
        response.setMerchantName(expense.getMerchantName());
        response.setConfidenceScore(expense.getConfidenceScore());
        response.setTransactionDate(expense.getTransactionDate());
        response.setCreatedAt(expense.getCreatedAt());
        response.setManuallyUpdated(expense.isManuallyUpdated());
        return response;
    }
}