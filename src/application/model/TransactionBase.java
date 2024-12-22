package application;

public abstract class TransactionBase {
	private String name, type;
	private double paymentAmount;
	
	public TransactionBase(String name, String type, double paymentAmount) {
		this.name = name;
		this.type = type;
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

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
}
