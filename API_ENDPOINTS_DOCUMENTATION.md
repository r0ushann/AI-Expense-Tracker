# API Documentation - Smart Expense Categorizer

Complete reference for all REST API endpoints with request/response examples.

---

## Base URL
```
http://localhost:8080
```

---

## Table of Contents
1. [Create Expense (AI Auto-Categorization)](#1-create-expense)
2. [Get All Expenses](#2-get-all-expenses)
3. [Get Expense by ID](#3-get-expense-by-id)
4. [Get Expenses by Category](#4-get-expenses-by-category)
5. [Update Category (Manual Override)](#5-update-category)
6. [Delete Expense](#6-delete-expense)
7. [Get Overall Summary](#7-get-overall-summary)
8. [Get Monthly Summary](#8-get-monthly-summary)
9. [Get Expenses by Date Range](#9-get-expenses-by-date-range)

---

## 1. Create Expense

**Endpoint:** `POST /api/expenses`

**Description:** Creates a new expense with AI-powered automatic categorization

**Headers:**
```
Content-Type: application/json
```

### Request Body

```json
{
  "description": "Starbucks Coffee Grande Latte",
  "amount": 5.50,
  "transactionDate": "2024-02-07T10:30:00"
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| description | String | Yes | Description of the expense |
| amount | Decimal | Yes | Amount spent (must be positive) |
| transactionDate | DateTime | No | Date and time of transaction (defaults to current time if not provided) |

### Response

**Status Code:** `201 Created`

```json
{
  "id": 1,
  "description": "Starbucks Coffee Grande Latte",
  "amount": 5.50,
  "category": "Food & Dining",
  "subCategory": "Coffee Shop",
  "merchantName": "Starbucks",
  "confidenceScore": 0.95,
  "transactionDate": "2024-02-07T10:30:00",
  "createdAt": "2024-02-07T10:31:25",
  "manuallyUpdated": false
}
```

#### Response Fields

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique expense identifier |
| description | String | Expense description |
| amount | Decimal | Expense amount |
| category | String | AI-determined primary category |
| subCategory | String | AI-determined sub-category |
| merchantName | String | Extracted merchant name |
| confidenceScore | Double | AI confidence (0.0 to 1.0) |
| transactionDate | DateTime | Transaction date and time |
| createdAt | DateTime | Record creation timestamp |
| manuallyUpdated | Boolean | Whether category was manually changed |

### Example cURL

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Starbucks Coffee Grande Latte",
    "amount": 5.50,
    "transactionDate": "2024-02-07T10:30:00"
  }'
```

### More Examples

**Example 1: Transportation**
```json
{
  "description": "Uber ride to downtown",
  "amount": 23.45
}
```

**Response:**
```json
{
  "id": 2,
  "description": "Uber ride to downtown",
  "amount": 23.45,
  "category": "Transportation",
  "subCategory": "Ride Sharing",
  "merchantName": "Uber",
  "confidenceScore": 0.92,
  "transactionDate": "2024-02-07T14:30:00",
  "createdAt": "2024-02-07T14:30:15",
  "manuallyUpdated": false
}
```

**Example 2: Shopping**
```json
{
  "description": "Amazon - wireless headphones",
  "amount": 129.99
}
```

**Response:**
```json
{
  "id": 3,
  "description": "Amazon - wireless headphones",
  "amount": 129.99,
  "category": "Shopping",
  "subCategory": "Electronics",
  "merchantName": "Amazon",
  "confidenceScore": 0.89,
  "transactionDate": "2024-02-07T16:00:00",
  "createdAt": "2024-02-07T16:00:20",
  "manuallyUpdated": false
}
```

**Example 3: Entertainment**
```json
{
  "description": "Netflix monthly subscription",
  "amount": 15.99
}
```

**Response:**
```json
{
  "id": 4,
  "description": "Netflix monthly subscription",
  "amount": 15.99,
  "category": "Entertainment",
  "subCategory": "Streaming Services",
  "merchantName": "Netflix",
  "confidenceScore": 0.97,
  "transactionDate": "2024-02-07T18:00:00",
  "createdAt": "2024-02-07T18:00:10",
  "manuallyUpdated": false
}
```

---

## 2. Get All Expenses

**Endpoint:** `GET /api/expenses`

**Description:** Retrieves all expenses in the system

### Request

No request body required.

### Response

**Status Code:** `200 OK`

```json
[
  {
    "id": 1,
    "description": "Starbucks Coffee Grande Latte",
    "amount": 5.50,
    "category": "Food & Dining",
    "subCategory": "Coffee Shop",
    "merchantName": "Starbucks",
    "confidenceScore": 0.95,
    "transactionDate": "2024-02-07T10:30:00",
    "createdAt": "2024-02-07T10:31:25",
    "manuallyUpdated": false
  },
  {
    "id": 2,
    "description": "Uber ride to downtown",
    "amount": 23.45,
    "category": "Transportation",
    "subCategory": "Ride Sharing",
    "merchantName": "Uber",
    "confidenceScore": 0.92,
    "transactionDate": "2024-02-07T14:30:00",
    "createdAt": "2024-02-07T14:30:15",
    "manuallyUpdated": false
  },
  {
    "id": 3,
    "description": "Whole Foods groceries",
    "amount": 87.32,
    "category": "Food & Dining",
    "subCategory": "Groceries",
    "merchantName": "Whole Foods",
    "confidenceScore": 0.94,
    "transactionDate": "2024-02-07T16:00:00",
    "createdAt": "2024-02-07T16:00:20",
    "manuallyUpdated": false
  }
]
```

### Example cURL

```bash
curl -X GET http://localhost:8080/api/expenses
```

---

## 3. Get Expense by ID

**Endpoint:** `GET /api/expenses/{id}`

**Description:** Retrieves a specific expense by its ID

### Request

**Path Parameter:**
- `id` (Long) - Expense ID

### Response

**Status Code:** `200 OK`

```json
{
  "id": 1,
  "description": "Starbucks Coffee Grande Latte",
  "amount": 5.50,
  "category": "Food & Dining",
  "subCategory": "Coffee Shop",
  "merchantName": "Starbucks",
  "confidenceScore": 0.95,
  "transactionDate": "2024-02-07T10:30:00",
  "createdAt": "2024-02-07T10:31:25",
  "manuallyUpdated": false
}
```

### Error Response

**Status Code:** `500 Internal Server Error`

```json
{
  "status": 500,
  "message": "Expense not found with ID: 999",
  "timestamp": "2024-02-07T18:30:00"
}
```

### Example cURL

```bash
curl -X GET http://localhost:8080/api/expenses/1
```

---

## 4. Get Expenses by Category

**Endpoint:** `GET /api/expenses/category/{category}`

**Description:** Retrieves all expenses belonging to a specific category

### Request

**Path Parameter:**
- `category` (String) - Category name (URL encoded if contains spaces)

**Valid Categories:**
- Food & Dining
- Transportation
- Shopping
- Entertainment
- Bills & Utilities
- Healthcare
- Travel
- Education
- Personal Care
- Home & Garden
- Gifts & Donations
- Other

### Response

**Status Code:** `200 OK`

```json
[
  {
    "id": 1,
    "description": "Starbucks Coffee Grande Latte",
    "amount": 5.50,
    "category": "Food & Dining",
    "subCategory": "Coffee Shop",
    "merchantName": "Starbucks",
    "confidenceScore": 0.95,
    "transactionDate": "2024-02-07T10:30:00",
    "createdAt": "2024-02-07T10:31:25",
    "manuallyUpdated": false
  },
  {
    "id": 3,
    "description": "Whole Foods groceries",
    "amount": 87.32,
    "category": "Food & Dining",
    "subCategory": "Groceries",
    "merchantName": "Whole Foods",
    "confidenceScore": 0.94,
    "transactionDate": "2024-02-07T16:00:00",
    "createdAt": "2024-02-07T16:00:20",
    "manuallyUpdated": false
  }
]
```

### Example cURL

```bash
# With URL encoding
curl -X GET "http://localhost:8080/api/expenses/category/Food%20%26%20Dining"

# Simple category
curl -X GET http://localhost:8080/api/expenses/category/Transportation
```

---

## 5. Update Category

**Endpoint:** `PUT /api/expenses/{id}/category`

**Description:** Manually update the category of an expense

**Headers:**
```
Content-Type: application/json
```

### Request

**Path Parameter:**
- `id` (Long) - Expense ID

**Request Body:**
```json
{
  "category": "Entertainment",
  "subCategory": "Coffee with Friends"
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| category | String | Yes | New category name |
| subCategory | String | No | New sub-category name |

### Response

**Status Code:** `200 OK`

```json
{
  "id": 1,
  "description": "Starbucks Coffee Grande Latte",
  "amount": 5.50,
  "category": "Entertainment",
  "subCategory": "Coffee with Friends",
  "merchantName": "Starbucks",
  "confidenceScore": 0.95,
  "transactionDate": "2024-02-07T10:30:00",
  "createdAt": "2024-02-07T10:31:25",
  "manuallyUpdated": true
}
```

**Note:** The `manuallyUpdated` field is now `true`.

### Example cURL

```bash
curl -X PUT http://localhost:8080/api/expenses/1/category \
  -H "Content-Type: application/json" \
  -d '{
    "category": "Entertainment",
    "subCategory": "Coffee with Friends"
  }'
```

---

## 6. Delete Expense

**Endpoint:** `DELETE /api/expenses/{id}`

**Description:** Deletes an expense from the system

### Request

**Path Parameter:**
- `id` (Long) - Expense ID to delete

### Response

**Status Code:** `204 No Content`

No response body.

### Error Response

**Status Code:** `500 Internal Server Error`

```json
{
  "status": 500,
  "message": "Expense not found with ID: 999",
  "timestamp": "2024-02-07T18:30:00"
}
```

### Example cURL

```bash
curl -X DELETE http://localhost:8080/api/expenses/1
```

---

## 7. Get Overall Summary

**Endpoint:** `GET /api/expenses/summary`

**Description:** Retrieves a summary of all expenses with category-wise breakdown

### Request

No request body required.

### Response

**Status Code:** `200 OK`

```json
{
  "totalAmount": 267.26,
  "totalExpenses": 6,
  "categoryBreakdown": [
    {
      "category": "Food & Dining",
      "amount": 98.32,
      "percentage": 36.79
    },
    {
      "category": "Shopping",
      "amount": 129.99,
      "percentage": 48.63
    },
    {
      "category": "Transportation",
      "amount": 23.45,
      "percentage": 8.77
    },
    {
      "category": "Entertainment",
      "amount": 15.99,
      "percentage": 5.98
    }
  ]
}
```

#### Response Fields

| Field | Type | Description |
|-------|------|-------------|
| totalAmount | Decimal | Sum of all expense amounts |
| totalExpenses | Integer | Total number of expenses |
| categoryBreakdown | Array | Category-wise breakdown |

**CategoryBreakdown Object:**

| Field | Type | Description |
|-------|------|-------------|
| category | String | Category name |
| amount | Decimal | Total amount for this category |
| percentage | Double | Percentage of total spending |

### Example cURL

```bash
curl -X GET http://localhost:8080/api/expenses/summary
```

---

## 8. Get Monthly Summary

**Endpoint:** `GET /api/expenses/summary/monthly`

**Description:** Retrieves expense summary for a specific month

### Request

**Query Parameters:**
- `year` (Integer) - Year (e.g., 2024)
- `month` (Integer) - Month (1-12)

### Response

**Status Code:** `200 OK`

```json
{
  "totalAmount": 156.45,
  "totalExpenses": 4,
  "categoryBreakdown": [
    {
      "category": "Food & Dining",
      "amount": 92.82,
      "percentage": 59.33
    },
    {
      "category": "Transportation",
      "amount": 48.00,
      "percentage": 30.68
    },
    {
      "category": "Entertainment",
      "amount": 15.99,
      "percentage": 10.22
    }
  ]
}
```

### Example cURL

```bash
curl -X GET "http://localhost:8080/api/expenses/summary/monthly?year=2024&month=2"
```

### Examples for Different Months

**January 2024:**
```bash
curl -X GET "http://localhost:8080/api/expenses/summary/monthly?year=2024&month=1"
```

**December 2023:**
```bash
curl -X GET "http://localhost:8080/api/expenses/summary/monthly?year=2023&month=12"
```

---

## 9. Get Expenses by Date Range

**Endpoint:** `GET /api/expenses/date-range`

**Description:** Retrieves expenses within a specific date range

### Request

**Query Parameters:**
- `startDate` (DateTime) - Start date in ISO 8601 format
- `endDate` (DateTime) - End date in ISO 8601 format

**Format:** `YYYY-MM-DDTHH:mm:ss`

### Response

**Status Code:** `200 OK`

```json
[
  {
    "id": 1,
    "description": "Starbucks Coffee Grande Latte",
    "amount": 5.50,
    "category": "Food & Dining",
    "subCategory": "Coffee Shop",
    "merchantName": "Starbucks",
    "confidenceScore": 0.95,
    "transactionDate": "2024-02-07T10:30:00",
    "createdAt": "2024-02-07T10:31:25",
    "manuallyUpdated": false
  },
  {
    "id": 2,
    "description": "Uber ride to downtown",
    "amount": 23.45,
    "category": "Transportation",
    "subCategory": "Ride Sharing",
    "merchantName": "Uber",
    "confidenceScore": 0.92,
    "transactionDate": "2024-02-07T14:30:00",
    "createdAt": "2024-02-07T14:30:15",
    "manuallyUpdated": false
  }
]
```

### Example cURL

```bash
# Full month of February 2024
curl -X GET "http://localhost:8080/api/expenses/date-range?startDate=2024-02-01T00:00:00&endDate=2024-02-29T23:59:59"

# Single day
curl -X GET "http://localhost:8080/api/expenses/date-range?startDate=2024-02-07T00:00:00&endDate=2024-02-07T23:59:59"

# Last week
curl -X GET "http://localhost:8080/api/expenses/date-range?startDate=2024-02-01T00:00:00&endDate=2024-02-07T23:59:59"
```

---

## Error Responses

### Validation Error

**Status Code:** `400 Bad Request`

```json
{
  "status": 400,
  "errors": {
    "description": "Description is required",
    "amount": "Amount must be positive"
  },
  "timestamp": "2024-02-07T18:30:00"
}
```

### Not Found Error

**Status Code:** `500 Internal Server Error`

```json
{
  "status": 500,
  "message": "Expense not found with ID: 999",
  "timestamp": "2024-02-07T18:30:00"
}
```

### Internal Server Error

**Status Code:** `500 Internal Server Error`

```json
{
  "status": 500,
  "message": "An unexpected error occurred",
  "timestamp": "2024-02-07T18:30:00"
}
```

---

## HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 OK | Request successful |
| 201 Created | Resource created successfully |
| 204 No Content | Resource deleted successfully |
| 400 Bad Request | Validation error or invalid request |
| 500 Internal Server Error | Server error or resource not found |

---

## Complete Testing Workflow

Here's a complete example workflow to test all endpoints:

### 1. Create Multiple Expenses

```bash
# Coffee
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"description": "Starbucks Coffee", "amount": 5.50}'

# Transportation
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"description": "Uber ride", "amount": 23.45}'

# Shopping
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"description": "Amazon headphones", "amount": 129.99}'

# Groceries
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"description": "Whole Foods groceries", "amount": 87.32}'
```

### 2. View All Expenses

```bash
curl -X GET http://localhost:8080/api/expenses
```

### 3. View Summary

```bash
curl -X GET http://localhost:8080/api/expenses/summary
```

### 4. Filter by Category

```bash
curl -X GET "http://localhost:8080/api/expenses/category/Food%20%26%20Dining"
```

### 5. Update a Category

```bash
curl -X PUT http://localhost:8080/api/expenses/1/category \
  -H "Content-Type: application/json" \
  -d '{"category": "Entertainment", "subCategory": "Social"}'
```

### 6. View Updated Expense

```bash
curl -X GET http://localhost:8080/api/expenses/1
```

### 7. Get Monthly Summary

```bash
curl -X GET "http://localhost:8080/api/expenses/summary/monthly?year=2024&month=2"
```

### 8. Delete an Expense

```bash
curl -X DELETE http://localhost:8080/api/expenses/1
```

---

## Postman Collection

You can import these endpoints into Postman using this structure:

**Collection Name:** Smart Expense Categorizer API

**Folders:**
1. Expenses Management
   - Create Expense
   - Get All Expenses
   - Get Expense by ID
   - Update Category
   - Delete Expense
2. Category Operations
   - Get by Category
3. Analytics & Reports
   - Overall Summary
   - Monthly Summary
   - Date Range Filter

---

## Testing Tips

1. **Test AI Categorization** - Try different expense descriptions to see how the AI categorizes them
2. **Check Confidence Scores** - Lower scores might need manual review
3. **Test Validation** - Try sending invalid data (negative amounts, empty descriptions)
4. **Date Handling** - Test with different date formats and time zones
5. **Edge Cases** - Test with very large amounts, special characters in descriptions

---

## Rate Limiting

Currently, there are no rate limits, but in production we should consider:
- Limiting requests per minute per IP
- API key-based authentication
- Request throttling for AI categorization calls

---

This documentation covers all 9 endpoints with complete request/response examples. We will use it to integrate with frontend applications or for API testing.
