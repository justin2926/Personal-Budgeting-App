package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class ScheduledTransactionsController {

	@FXML
	private TableColumn<ScheduledTransaction, String> descriptionColumn, dueDateColumn, frequencyColumn, nameColumn,
			paymentColumn, typeColumn;

	@FXML
	private TableView<ScheduledTransaction> scheduledTransactionsTableView;

	@FXML
	private TextField scheduledTransactionSearchField;

	private ObservableList<ScheduledTransaction> scheduledTransactions = FXCollections.observableArrayList();

	private final String scheduledTransactionsDataFilePath = "src/application/data/ScheduledTransactions.csv";

	private Stage stage;

	private Scene scene;

	public void initialize() throws IOException {
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("name"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("type"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("description"));
		frequencyColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("frequency"));
		paymentColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("paymentAmount"));
		dueDateColumn.setCellValueFactory(new PropertyValueFactory<ScheduledTransaction, String>("dueDate"));

		loadData();
		initializeSearch();
		initializeRowSelection();
		
		scheduledTransactionsTableView.refresh();
	}
	
	private void initializeSearch() {
		FilteredList<ScheduledTransaction> filteredData = new FilteredList<>(scheduledTransactions, p -> true);

		scheduledTransactionSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(transaction -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				return transaction.getDescription().toLowerCase().contains(lowerCaseFilter);
																								
			});
		});

		SortedList<ScheduledTransaction> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(scheduledTransactionsTableView.comparatorProperty());
		scheduledTransactionsTableView.setItems(sortedData);
	}
	
	private void initializeRowSelection() {
	    scheduledTransactionsTableView.setRowFactory(tv -> {
	        TableRow<ScheduledTransaction> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (event.getClickCount() == 2 && !row.isEmpty()) {
	                ScheduledTransaction selectedTransaction = row.getItem();
	                switchToEditScheduledTransactionPage(selectedTransaction);
	            }
	        });
	        return row;
	    });
	}

	public void addScheduledTransaction(ScheduledTransaction scheduledTransaction) {
		scheduledTransactions.add(scheduledTransaction);
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
			scheduledTransactionsTableView.setItems(scheduledTransactions);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void switchToEditScheduledTransactionPage(ScheduledTransaction scheduledTransaction) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditScheduledTransactionPage.fxml"));
	        Parent root = loader.load();

	        EditScheduledTransactionController editScheduledTransactionController = loader.getController();
	        editScheduledTransactionController.setScheduledTransactionData(scheduledTransaction);
	        editScheduledTransactionController.setScheduledTransactions(scheduledTransactions);

	        stage = (Stage) scheduledTransactionsTableView.getScene().getWindow();
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
	void switchToNewScheduledTransactionPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NewScheduledTransactionPage.fxml"));
		Parent root = loader.load();

		NewScheduledTransactionsController newScheduledTransactionsPageController = loader.getController();
		newScheduledTransactionsPageController.setScheduledTransactionsPageController(this);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
