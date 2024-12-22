package application;

import java.time.LocalDate;

public class Person {
	private String name;
	private double balance;
	private LocalDate date;

	public Person(String name, double balance, LocalDate date) {
		this.name = name;
		this.balance = balance;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		return balance;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
