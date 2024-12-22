package application;

public class ScheduledTransaction {
	private String name, type, description, frequency, dueDate;
	private double paymentAmount;
	
	public ScheduledTransaction(String name, String type, String description, String frequency, double paymentAmount, String dueDate) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.frequency = frequency;
		this.dueDate = dueDate;
		this.paymentAmount = paymentAmount;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
}
