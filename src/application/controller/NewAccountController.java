package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewAccountController {

	@FXML
	private TextField nameField;

	@FXML
	private TextField balanceField;

	@FXML
	private DatePicker dateField;

	@FXML
	private Button createButton;

	@FXML
	private Label messageLabel;

	AccountsController accountsPageController;
	
	private final String accountDataFilePath = "src/application/data/Accounts.csv";

	public void create(ActionEvent event) {
		createAccount();
	}

	public void createAccount() {
		messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		messageLabel.setText("");

		String nameInput = nameField.getText();
		String balanceInput = balanceField.getText();

		// Check if fields are empty
		if (nameInput.isEmpty() || dateField.getValue() == null || balanceInput.isEmpty()) {
			messageLabel.setText("Please fill out all fields!");
			return;
		}

		double balance = Double.parseDouble(balanceField.getText());

		// Check if balance is negative
		if (balance < 0) {
			messageLabel.setText("Balance cannot be negative!");
			return;
		}

		messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));
		messageLabel.setText("Success!");

		Person person = new Person(nameInput, balance, dateField.getValue());
		accountsPageController.addPerson(person);

		saveNewAccountData(nameInput, dateField.getValue(), balance);

		nameField.setText("");
		balanceField.setText("");
		dateField.setValue(null);
	}

	private void saveNewAccountData(String name, LocalDate date, double balance) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountDataFilePath, true))) {
			if (new java.io.File(accountDataFilePath).length() == 0) {
				writer.write("Date,Name,Balance\n");
			}
			writer.write(date + "," + name + "," + balance + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAccountsPageController(AccountsController controller) {
		this.accountsPageController = controller;
	}
	
	@FXML
	void switchToAccountsPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsPage.fxml"));
		Parent root = loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
