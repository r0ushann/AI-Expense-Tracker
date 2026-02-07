package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSummary {

    private BigDecimal totalAmount;
    private int totalExpenses;
    private List<CategorySummary> categoryBreakdown;

    public void setTotalAmount(BigDecimal totalAmount) {
    }

    public void setTotalExpenses(int size) {
    }

    public void setCategoryBreakdown(List<CategorySummary> categoryBreakdown) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummary {
        private String category;
        private BigDecimal amount;
        private double percentage;

        public CategorySummary(String category, BigDecimal amount, double percentage) {
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }
}