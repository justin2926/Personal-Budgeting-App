package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class NewTransactionController {

	@FXML
	private ChoiceBox<String> accountChoiceBox, typeChoiceBox;

	@FXML
	private DatePicker dateField;

	@FXML
	private TextField descriptionField, paymentField, depositField;

	@FXML
	private Label messageLabel;

	private final String transactionDataFilePath = "src/application/data/Transactions.csv";
	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";

	private ObservableList<String> names = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();

	TransactionsController transactionsPageController;

	public void initialize() {
		loadAccountNames();
		loadTypes();
	}
	
	public void create(ActionEvent event) throws IOException {
		createData();
	}

	public void createData() {
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

		Transaction transaction = new Transaction(accountInput, typeInput, dateField.getValue(), descriptionInput, paymentBalance, depositBalance);
		transactionsPageController.addTransaction(transaction);

		saveTransactions(accountInput, typeInput, dateField.getValue(), descriptionInput, paymentBalance,
				depositBalance);
		
		descriptionField.clear();
		paymentField.clear();
		depositField.clear();
		accountChoiceBox.setValue(null);
		typeChoiceBox.setValue(null);
		dateField.setValue(null);
	}

	private void saveTransactions(String name, String type, LocalDate date, String description, double paymentBalance,
			double depositBalance) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionDataFilePath, true))) {
			if (new java.io.File(transactionDataFilePath).length() == 0) {
				writer.write("Name,Type,Date,Description,Payment Amount, Deposit Amount\n");
			}
			writer.write(name + "," + type + "," + date + "," + description + "," + paymentBalance + ","
					+ depositBalance + "\n");
			writer.flush();
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
	
	public void setTransactionsPageController(TransactionsController controller) {
		this.transactionsPageController = controller;
	}

	@FXML
	void switchToTransactionsPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionsPage.fxml"));
		Parent root = loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
