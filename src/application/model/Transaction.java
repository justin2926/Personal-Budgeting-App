package application;

import java.time.LocalDate;

public class Transaction {

	private String name, type, description;
	private double paymentAmount, depositAmount;
	private LocalDate date;

	public Transaction(String name, String type, LocalDate date, String description, double paymentAmount,
			double depositAmount) {
		this.name = name;
		this.type = type;
		this.date = date;
		this.description = description;
		this.paymentAmount = paymentAmount;
		this.depositAmount = depositAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
