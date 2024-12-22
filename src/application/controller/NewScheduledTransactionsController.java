package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewScheduledTransactionsController {

	@FXML
	private ChoiceBox<String> accountChoiceBox, frequencyChoiceBox, typeChoiceBox;

	@FXML
	private TextField dueDateField, descriptionField, paymentAmountField;

	@FXML
	private Label messageLabel;

	private ObservableList<String> names = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();

	private String[] frequencies = { "Daily", "Weekly", "Bi-Weekly", "Monthly", "Annually" };

	private final String scheduledTransactionsDataFilePath = "src/application/data/ScheduledTransactions.csv";
	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";

	ScheduledTransactionsController scheduledTransactionsPageController;

	public void initialize() {
		loadAccountNames();
		loadTypes();
		loadFrequencies();
	}

	public void create(ActionEvent event) throws IOException {
		createData();
	}

	public void createData() {
		messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		messageLabel.setText("");

		String accountInput = accountChoiceBox.getValue();
		String typeInput = typeChoiceBox.getValue();
		String frequencyInput = frequencyChoiceBox.getValue();
		String descriptionInput = descriptionField.getText();
		String dueDateInput = dueDateField.getText();
		String paymentInput = paymentAmountField.getText();

		// Check if fields are empty
		if (accountInput == null || typeInput == null || frequencyInput == null || descriptionInput.isEmpty()
				|| dueDateInput.isEmpty() || paymentInput.isEmpty()) {
			messageLabel.setText("Please fill out all fields!");
			return;
		}

		double paymentBalance = Double.parseDouble(paymentAmountField.getText());

		// Check if balance is negative
		if (paymentBalance < 0) {
			messageLabel.setText("Balance cannot be negative!");
			return;
		}
		
		// Check if date is valid
		if (Integer.parseInt(dueDateInput) < 1 || Integer.parseInt(dueDateInput) > 31) {
			messageLabel.setText("Invalid due date!");
			return;
		}

		messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));
		messageLabel.setText("Success!");

		ScheduledTransaction scheduledTransactions = new ScheduledTransaction(accountInput, typeInput, frequencyInput,
				descriptionInput, paymentBalance, dueDateInput);
		scheduledTransactionsPageController.addScheduledTransaction(scheduledTransactions);
		
		saveScheduledTransactions(accountInput, typeInput, descriptionInput, frequencyInput, paymentBalance, dueDateInput);
		
		accountChoiceBox.setValue(null);
		typeChoiceBox.setValue(null);
		frequencyChoiceBox.setValue(null);
		descriptionField.clear();
		dueDateField.clear();
		paymentAmountField.clear();

	}

	private void saveScheduledTransactions(String name, String type, String description, String frequency,
			double paymentAmount, String dueDate) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(scheduledTransactionsDataFilePath, true))) {
			if (new java.io.File(scheduledTransactionsDataFilePath).length() == 0) {
				writer.write("Name,Type,Description,Frequency,Payment Amount, Due Date\n");
			}
			writer.write(name + "," + type + "," + description + "," + frequency + "," + paymentAmount + "," + dueDate
					+ "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAccountNames() {
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

	public void loadTypes() {
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

	public void loadFrequencies() {
		frequencyChoiceBox.getItems().addAll(frequencies);
	}
	
	public void setScheduledTransactionsPageController(ScheduledTransactionsController controller) {
		this.scheduledTransactionsPageController = controller;
	}

	@FXML
	void switchToScheduledTransactionsPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ScheduledTransactionsPage.fxml"));
		Parent root = loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
