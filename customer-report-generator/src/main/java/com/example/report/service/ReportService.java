package com.example.report.service;

import com.example.report.model.Transaction;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

	private List<Transaction> transactions;

	public ReportService() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Retail_Transaction_Dataset.csv")) {
            if (is == null) {
                throw new RuntimeException("Dataset not found!");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            // Configure CSV parser for quotes + multi-line fields
            com.opencsv.CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .withQuoteChar('"')
                    .withIgnoreQuotations(false)
                    .build();

            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .build();

            transactions = new ArrayList<>();
            try {
				String[] header = csvReader.readNext();
			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // skip header
            String[] row;

            try {
				while ((row = csvReader.readNext()) != null) {
				    try {
				        Transaction txn = new Transaction(
				                row[0],                  // CustomerID
				                row[1],                  // ProductID
				                Integer.parseInt(row[2]),// Quantity
				                Double.parseDouble(row[3]), // Price
				                row[4],                  // TransactionDate
				                row[5],                  // PaymentMethod
				                row[6].replaceAll("[\\r\\n]+", " "),                  // StoreLocation (may be multi-line, but parsed correctly)
				                row[7],                  // ProductCategory
				                Double.parseDouble(row[8]), // DiscountApplied
				                Double.parseDouble(row[9])  // TotalAmount
				        );
				        transactions.add(txn);
				    } catch (Exception e) {
				        System.err.println("Skipping malformed row: " + Arrays.toString(row));
				    }
				}
			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dataset", e);
        }
    }

	public Map<String, Object> generateReport(String customerId) {
		List<Transaction> customerTxns = transactions.stream().filter(t -> t.getCustomerID().equals(customerId))
				.collect(Collectors.toList());

		if (customerTxns.isEmpty())
			return Map.of("error", "Customer not found");

		long totalVisits = customerTxns.size();
		double totalSpend = customerTxns.stream().mapToDouble(Transaction::getTotalAmount).sum();
		double avgSpend = totalSpend / totalVisits;

		String topCategory = customerTxns.stream()
				.collect(Collectors.groupingBy(Transaction::getProductCategory,
						Collectors.summingDouble(Transaction::getTotalAmount)))
				.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

		double totalDiscount = customerTxns.stream()
				.mapToDouble(t -> t.getPrice() * t.getQuantity() * t.getDiscountApplied() / 100.0).sum();

		String preferredPayment = customerTxns.stream()
				.collect(Collectors.groupingBy(Transaction::getPaymentMethod, Collectors.counting())).entrySet()
				.stream().max(Map.Entry.comparingByValue()).get().getKey();

		Map<String, Map<String, Object>> paymentStats = new HashMap<>();
		customerTxns.stream().collect(Collectors.groupingBy(Transaction::getPaymentMethod)).forEach((method, list) -> {
			paymentStats.put(method, Map.of("count", list.size(), "grossAmount",
					list.stream().mapToDouble(Transaction::getTotalAmount).sum()));
		});

		String topStore = customerTxns.stream()
				.collect(Collectors.groupingBy(Transaction::getStoreLocation,
						Collectors.summingDouble(Transaction::getTotalAmount)))
				.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

		return Map.of("customerId", customerId, "totalVisits", totalVisits, "averageSpendPerVisit", avgSpend,
				"topCategoryBySpend", topCategory, "totalDiscountReceived", totalDiscount, "mostPreferredPaymentMethod",
				preferredPayment, "paymentStats", paymentStats, "topStoreByRevenue", topStore);
	}
}
