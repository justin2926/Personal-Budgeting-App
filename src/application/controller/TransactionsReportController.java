package application;

import java.io.BufferedReader;
import java.io.FileReader;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TransactionsReportController {

	@FXML
	private ComboBox<String> accountComboBox, typeComboBox;

	@FXML
	private TableColumn<Person, LocalDate> dateAccountColumn, dateTypeColumn;

	@FXML
	private TableColumn<Person, String> depositAccountColumn, depositTypeColumn;

	@FXML
	private TableColumn<Person, String> descriptionAccountColumn, descriptionTypeColumn;

	@FXML
	private TableColumn<Person, String> nameAccountColumn, nameTypeColumn;

	@FXML
	private TableColumn<Person, String> paymentAccountColumn, paymentTypeColumn;

	@FXML
	private TableColumn<Person, String> typeAccountColumn, typeTypeColumn;

	@FXML
	private TableView<Transaction> transactionsReportAccountTableView, transactionsReportTypeTableView;

	private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	private ObservableList<String> names = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();

	private final String transactionDataFilePath = "src/application/data/Transactions.csv";
	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";

	public void initialize() {

		initializeAccountTableView();
		initializeTypeTableView();

		loadData();
		loadAccountNames();
		loadTypes();
	}

	public void initializeAccountTableView() {
		dateAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("date"));
		depositAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("depositAmount"));
		descriptionAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("description"));
		nameAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		paymentAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("paymentAmount"));
		typeAccountColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("type"));
	}

	public void initializeTypeTableView() {
		dateTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("date"));
		depositTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("depositAmount"));
		descriptionTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("description"));
		nameTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("paymentAmount"));
		typeTypeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("type"));
	}
	
	@FXML
	private void filterTransactionsByAccount() {
		String selectedAccount = accountComboBox.getValue();

		if (selectedAccount != null) {
			ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

			for (Transaction transaction : transactions) {
				if (transaction.getName().equals(selectedAccount)) {
					filteredTransactions.add(transaction);
				}
			}

			transactionsReportAccountTableView.setItems(filteredTransactions);
		}
	}

	@FXML
	private void filterTransactionsByType() {
		String selectedType = typeComboBox.getValue();

		if (selectedType != null) {
			ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

			for (Transaction transaction : transactions) {
				if (transaction.getType().equals(selectedType)) {
					filteredTransactions.add(transaction);
				}
			}

			transactionsReportTypeTableView.setItems(filteredTransactions);
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
			transactionsReportAccountTableView.setItems(transactions);
			transactionsReportTypeTableView.setItems(transactions);

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
			accountComboBox.getItems().addAll(names);

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
			typeComboBox.getItems().addAll(types);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void switchToHomePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
		Parent root = loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
