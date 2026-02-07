package controller;

import dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExpenseController {

    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        log.info("POST /api/expenses - Creating new expense");
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        log.info("GET /api/expenses - Fetching all expenses");
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        log.info("GET /api/expenses/{} - Fetching expense by ID", id);
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(@PathVariable String category) {
        log.info("GET /api/expenses/category/{} - Fetching expenses by category", category);
        List<ExpenseResponse> expenses = expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<ExpenseResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        log.info("PUT /api/expenses/{}/category - Updating category", id);
        ExpenseResponse expense = expenseService.updateCategory(id, request);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.info("DELETE /api/expenses/{} - Deleting expense", id);
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummary> getSummary() {
        log.info("GET /api/expenses/summary - Generating overall summary");
        ExpenseSummary summary = expenseService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<ExpenseSummary> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        log.info("GET /api/expenses/summary/monthly - Generating monthly summary for {}/{}", month, year);
        ExpenseSummary summary = expenseService.getMonthlySummary(year, month);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/expenses/date-range - Fetching expenses between {} and {}", startDate, endDate);
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
}