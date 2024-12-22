package application;

public class ScheduledTransaction extends TransactionBase {
	private String description, frequency, dueDate;
	
	public ScheduledTransaction(String name, String type, String description, String frequency, double paymentAmount, String dueDate) {
		super(name, type, paymentAmount);
		this.description = description;
		this.frequency = frequency;
		this.dueDate = dueDate;
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

}
