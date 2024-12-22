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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditScheduledTransactionController {

    @FXML
    private ChoiceBox<String> accountChoiceBox, typeChoiceBox, frequencyChoiceBox;

    @FXML
    private TextField descriptionField, dueDateField, paymentAmountField;

    @FXML
    private Label messageLabel;
    
	private ScheduledTransaction data;

	private final String scheduledTransactionsDataFilePath = "src/application/data/ScheduledTransactions.csv";
	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";
	
	private String[] frequencies = { "Daily", "Weekly", "Bi-Weekly", "Monthly", "Annually" };

	private ObservableList<String> names = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();
	private ObservableList<ScheduledTransaction> scheduledTransactions = FXCollections.observableArrayList();
    
    public void initialize() {
		loadData();
		loadAccountNames();
		loadTypes();
		loadFrequencies();
    }
    
	public void save(ActionEvent event) throws IOException {
		saveData();
	}
    
	public void saveData() {
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
		
		if (Integer.parseInt(dueDateInput) < 1 || Integer.parseInt(dueDateInput) > 31) {
			messageLabel.setText("Invalid due date!");
			return;
		}

		messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));
		messageLabel.setText("Success!");

		for (ScheduledTransaction scheduledTransaction : scheduledTransactions) {
			if (scheduledTransaction.equals(data)) {

				scheduledTransaction.setName(accountInput);
				scheduledTransaction.setType(typeInput);
				scheduledTransaction.setDueDate(dueDateInput);
				scheduledTransaction.setDescription(descriptionInput);
				scheduledTransaction.setPaymentAmount(paymentBalance);
				scheduledTransaction.setFrequency(frequencyInput);

				messageLabel.setText("Transaction updated successfully!");
				messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));

				break;
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(scheduledTransactionsDataFilePath))) {
			if (new File(scheduledTransactionsDataFilePath).length() == 0) {
				writer.write("Name,Type,Description,Frequency,Payment Amount, Due Date\n");
			}

			for (ScheduledTransaction scheduledTransaction : scheduledTransactions) {
				writer.write(scheduledTransaction.getName() + "," + scheduledTransaction.getType() + ","
						+ scheduledTransaction.getDescription() + "," + scheduledTransaction.getFrequency() + "," + scheduledTransaction.getPaymentAmount() + ","
						+ scheduledTransaction.getDueDate() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			messageLabel.setText("Error saving transactions to file.");
			messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		}

	}

	private void loadData() {

		try (BufferedReader reader = new BufferedReader(new FileReader(scheduledTransactionsDataFilePath))) {
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
					String description = data[2];
					String frequency = data[3];
					double paymentAmount = Double.parseDouble(data[4]);
					String dueDate = data[5];
					scheduledTransactions.add(new ScheduledTransaction(name, type, description, frequency, paymentAmount, dueDate));
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
	
	public void loadFrequencies() {
		frequencyChoiceBox.getItems().addAll(frequencies);
	}
	
	public void setScheduledTransactionData(ScheduledTransaction scheduledTransaction) {
		this.data = scheduledTransaction;

		accountChoiceBox.setValue(scheduledTransaction.getName());
		typeChoiceBox.setValue(scheduledTransaction.getType());
		dueDateField.setText(scheduledTransaction.getDueDate());
		descriptionField.setText(scheduledTransaction.getDescription());
		frequencyChoiceBox.setValue(scheduledTransaction.getFrequency());
		paymentAmountField.setText(String.valueOf(scheduledTransaction.getPaymentAmount()));
	}
	
	public void setScheduledTransactions(ObservableList<ScheduledTransaction> scheduledTransactions) {
	    this.scheduledTransactions = scheduledTransactions;
	}

    @FXML
    void switchToScheduledTransactionsPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ScheduledTransactionsPage.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }

}
