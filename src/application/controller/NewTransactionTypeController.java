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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NewTransactionTypeController {

	private String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";

	@FXML
	private TableView<TransactionType> transactionTypeTableView;

	@FXML
	private TextField typeField;

	@FXML
	private Label messageLabel;

	@FXML
	TableColumn<TransactionType, String> typeColumn;
	
	ObservableList<TransactionType> types = FXCollections.observableArrayList();

	private Stage stage;

	private Scene scene;

	public void initialize() {
		typeColumn.setCellValueFactory(new PropertyValueFactory<TransactionType, String>("type"));
		loadData();
	}

	public void createType(ActionEvent event) {
		verifyTextField();
	}

	public void verifyTextField() {
		messageLabel.setTextFill(javafx.scene.paint.Color.RED);
		messageLabel.setText("");

		String typeInput = typeField.getText();

		// Check if textfield is empty
		if (typeInput.isEmpty()) {
			messageLabel.setText("Please fill out all fields!");
			return;
		}

		messageLabel.setTextFill(javafx.scene.paint.Color.web("#33ff5c"));
		messageLabel.setText("Success!");

		TransactionType type = new TransactionType(typeInput);
		transactionTypeTableView.getItems().add(type);

		saveNewAccountData(typeInput);

		typeField.setText("");

	}

	private void loadData() {

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
					types.add(new TransactionType(type));
				}
			}
			transactionTypeTableView.setItems(types);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveNewAccountData(String type) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionTypeDataFilePath, true))) {
			if (new java.io.File(transactionTypeDataFilePath).length() == 0) {
				writer.write("Type\n");
			}
			writer.write(type + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void switchToTransactionsPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionsPage.fxml"));
		Parent root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}