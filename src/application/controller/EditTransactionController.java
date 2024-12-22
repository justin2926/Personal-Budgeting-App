package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTransactionController {

	@FXML
	private ChoiceBox<String> accountChoiceBox, typeChoiceBox;

	@FXML
	private DatePicker dateField;

	@FXML
	private TextField depositField, descriptionField, paymentField;

	@FXML
	private Label messageLabel;

	private Transaction data;

	private final String transactionDataFilePath = "src/application/data/Transactions.csv";
	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";

	private ObservableList<String> names = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();
	private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

	public void initialize() {
		loadData();
		loadAccountNames();
		loadTypes();
	}
	
	public void save(ActionEvent event) throws IOException {
		saveData();
	}

	public void saveData() {
		messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		messageLabel.setText("");

		String descriptionInput = descriptionField.getText();
		String paymentInput = paymentField.getText();
		String depositInput = depositField.getText();
		String accountInput = accountChoiceBox.getValue();
		String typeInput = typeChoiceBox.getValue();

		// Check if fields are empty
		if (descriptionInput.isEmpty() || paymentInput.isEmpty() || depositInput.isEmpty() || typeInput == null
				|| accountInput == null) {
			messageLabel.setText("Please fill out all fields!");
			return;
		}

		double paymentBalance = Double.parseDouble(paymentField.getText());
		double depositBalance = Double.parseDouble(depositField.getText());

		// Check if balance is negative
		if (paymentBalance < 0 || depositBalance < 0) {
			messageLabel.setText("Balance cannot be negative!");
			return;
		}

		messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));
		messageLabel.setText("Success!");

		for (Transaction transaction : transactions) {
			if (transaction.equals(data)) {

				transaction.setName(accountInput);
				transaction.setType(typeInput);
				transaction.setDate(dateField.getValue());
				transaction.setDescription(descriptionInput);
				transaction.setPaymentAmount(paymentBalance);
				transaction.setDepositAmount(depositBalance);

				messageLabel.setText("Transaction updated successfully!");
				messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));

				break;
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionDataFilePath))) {
			if (new File(transactionDataFilePath).length() == 0) {
				writer.write("Name,Type,Date,Description,Payment Amount,Deposit Amount\n");
			}

			for (Transaction transaction : transactions) {
				writer.write(transaction.getName() + "," + transaction.getType() + "," + transaction.getDate() + ","
						+ transaction.getDescription() + "," + transaction.getPaymentAmount() + ","
						+ transaction.getDepositAmount() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			messageLabel.setText("Error saving transactions to file.");
			messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		}

	}

	private void loadData() {

		try (BufferedReader reader = new BufferedReader(new FileReader(transactionDataFilePath))) {
			String line;
			boolean isHeader = true;

			while ((line = reader.readLine()) != null) {
				if (isHeader) {
					isHeader = false;
					continue;
				}
				String[] data = line.split(",");
				if (data.length == 6) {
					String name = data[0];
					String type = data[1];
					LocalDate date = LocalDate.parse(data[2]);
					String description = data[3];
					double paymentAmount = Double.parseDouble(data[4]);
					double depositAmount = Double.parseDouble(data[5]);
					transactions.add(new Transaction(name, type, date, description, paymentAmount, depositAmount));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadAccountNames() {
		try (BufferedReader reader = new BufferedReader(new FileReader(accountDataFilePath))) {
			String line;
			boolean isHeader = true;

			while ((line = reader.readLine()) != null) {
				if (isHeader) {
					isHeader = false;
					continue;
				}
				String[] data = line.split(",");
				if (data.length == 3) {

					String name = data[1];
					names.add(name);
				}
			}
			accountChoiceBox.getItems().addAll(names);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTypes() {
		try (BufferedReader reader = new BufferedReader(new FileReader(transactionTypeDataFilePath))) {
			String line;
			boolean isHeader = true;

			while ((line = reader.readLine()) != null) {
				if (isHeader) {
					isHeader = false;
					continue;
				}
				String[] data = line.split(",");
				if (data.length == 1) {

					String type = data[0];
					types.add(type);
				}
			}
			typeChoiceBox.getItems().addAll(types);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTransactionData(Transaction transaction) {
		this.data = transaction;

		accountChoiceBox.setValue(transaction.getName());
		typeChoiceBox.setValue(transaction.getType());
		dateField.setValue(transaction.getDate());
		descriptionField.setText(transaction.getDescription());
		paymentField.setText(String.valueOf(transaction.getPaymentAmount()));
		depositField.setText(String.valueOf(transaction.getDepositAmount()));
	}
	
	public void setTransactions(ObservableList<Transaction> transactions) {
	    this.transactions = transactions;
	}

	@FXML
	void switchToTransactionsPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("TransactionsPage.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
