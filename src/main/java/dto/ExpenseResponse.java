package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

    private Long id;
    private String description;
    private BigDecimal amount;
    private String category;
    private String subCategory;
    private String merchantName;
    private Double confidenceScore;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private boolean manuallyUpdated;

    public void setCategory(String category) {
    }

    public void setId(Long id) {
    }

    public void setDescription(String description) {
    }

    public void setAmount(BigDecimal amount) {
    }

    public void setSubCategory(String subCategory) {
    }

    public void setMerchantName(String merchantName) {
    }

    public void setConfidenceScore(Double confidenceScore) {
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    }

    public void setManuallyUpdated(boolean manuallyUpdated) {
    }
}