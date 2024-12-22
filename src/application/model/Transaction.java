package application;

import java.time.LocalDate;

public class Transaction extends TransactionBase {

	private String description;
	private double depositAmount;
	private LocalDate date;

	public Transaction(String name, String type, LocalDate date, String description, double paymentAmount, double depositAmount) {
		super(name, type, paymentAmount);
		this.date = date;
		this.description = description;
		this.depositAmount = depositAmount;

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
