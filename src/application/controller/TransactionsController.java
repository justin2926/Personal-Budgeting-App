package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TransactionsController {

	@FXML
	private TableColumn<Person, String> nameColumn, typeColumn, descriptionColumn, paymentColumn, depositColumn;

	@FXML
	private TableColumn<Person, LocalDate> dateColumn;

	@FXML
	private TableView<Transaction> transactionsTableView;

	@FXML
	private TextField transactionSearchField;

	private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

	private final String transactionDataFilePath = "src/application/data/Transactions.csv";
	
	EditTransactionController editTransactionController;

	private Stage stage;

	private Scene scene;

	public void initialize() throws IOException {
		
		initializeTransactionTableView();
		
		loadData();
		initializeSearch();
		initializeRowSelection();
		
		transactionsTableView.refresh();

	}
	
	private void initializeTransactionTableView() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("type"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("date"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("description"));
		paymentColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("paymentAmount"));
		depositColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("depositAmount"));
	}

	private void initializeRowSelection() {
	    transactionsTableView.setRowFactory(tv -> {
	        TableRow<Transaction> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (event.getClickCount() == 2 && !row.isEmpty()) {
	                Transaction selectedTransaction = row.getItem();
	                switchToEditTransactionPage(selectedTransaction);
	            }
	        });
	        return row;
	    });
	}
	
	private void initializeSearch() {
		FilteredList<Transaction> filteredData = new FilteredList<>(transactions, p -> true);

		transactionSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(transaction -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				return transaction.getDescription().toLowerCase().contains(lowerCaseFilter); 
																							
			});
		});

		SortedList<Transaction> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(transactionsTableView.comparatorProperty());
		transactionsTableView.setItems(sortedData);
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
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
			transactionsTableView.setItems(transactions);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void switchToEditTransactionPage(Transaction transaction) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditTransactionPage.fxml"));
	        Parent root = loader.load();

	        EditTransactionController editTransactionController = loader.getController();
	        editTransactionController.setTransactionData(transaction);
	        editTransactionController.setTransactions(transactions); 

	        stage = (Stage) transactionsTableView.getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	void switchToHomePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
		Parent root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void switchToNewTransactionPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTransactionPage.fxml"));
		Parent root = loader.load();

		NewTransactionController newTransactionPageController = loader.getController();
		newTransactionPageController.setTransactionsPageController(this);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void switchToNewTransactionTypePage(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTransactionTypePage.fxml"));
		Parent root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

}
