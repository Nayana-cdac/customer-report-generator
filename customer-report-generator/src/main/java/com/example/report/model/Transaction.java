package com.example.report.model;

public class Transaction {
	private String customerID;
	private String productID;
	private int quantity;
	private double price;
	private String transactionDate;
	private String paymentMethod;
	private String storeLocation;
	private String productCategory;
	private double discountApplied;
	private double totalAmount;

	public Transaction(String customerID, String productID, int quantity, double price, String transactionDate,
			String paymentMethod, String storeLocation, String productCategory, double discountApplied,
			double totalAmount) {
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		this.price = price;
		this.transactionDate = transactionDate;
		this.paymentMethod = paymentMethod;
		this.storeLocation = storeLocation;
		this.productCategory = productCategory;
		this.discountApplied = discountApplied;
		this.totalAmount = totalAmount;
	}

	// Getters
	public String getCustomerID() {
		return customerID;
	}

	public String getProductID() {
		return productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public double getDiscountApplied() {
		return discountApplied;
	}

	public double getTotalAmount() {
		return totalAmount;
	}
}
