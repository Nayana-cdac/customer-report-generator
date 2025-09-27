# Customer Report Generator

## Overview
This Spring Boot application processes a retail transaction dataset and generates reports for a given CustomerID.

## Features
- Total number of visits
- Average spend per visit
- Top ProductCategory by total spend
- Total discount received
- Most preferred payment method
- Payment count and gross amount by payment mode
- Top store by revenue

---

## Running the Application

### Option 1: Run as Spring Boot App
From Eclipse or IntelliJ:
1. Right-click the project → **Run As → Spring Boot App**  
2. The app will start on port `8080` (default).  
3. Access the report in browser or via curl:
   http://localhost:8080/report/{customerId}
	
### Option 2: Run as JAR
1. Build the JAR:
   mvn clean package	
2. Run the JAR   
   java -jar target/customer-report-generator-1.0.0.jar
3. Access the report in browser or via curl:
   http://localhost:8080/report/{customerId} 
   
## Input Data
Dataset is located in:
   src/main/resources/Retail_Transaction_Dataset.csv
   
## Example Response
When you call http://localhost:8080/report/CUST123, the API returns a JSON report:

{
  "customerId": "CUST123",
  "totalVisits": 12,
  "averageSpend": 245.67,
  "topCategory": "Electronics",
  "totalDiscount": 150.0,
  "preferredPayment": "Credit Card",
  "paymentStats": {
    "Cash": {"count": 4, "grossAmount": 800.0},
    "Credit Card": {"count": 8, "grossAmount": 2100.0}
  },
  "topStore": "New York Downtown Store"
}   
     
   

