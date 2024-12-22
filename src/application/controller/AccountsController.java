package application;

import java.io.BufferedReader;
import java.io.File;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AccountsController {

	private Stage stage;
	private Scene scene;

	@FXML
	private TableView<Person> accountTableView;

	@FXML
	private TableColumn<Person, String> nameColumn, balanceColumn;

	@FXML
	private TableColumn<Person, LocalDate> dateColumn;

	private ObservableList<Person> people = FXCollections.observableArrayList();

	private final String accountDataFilePath = "src/application/data/Accounts.csv";

	public void initialize() throws IOException {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		balanceColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("balance"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("date"));

		loadData();

	}

	public void addPerson(Person person) {
		people.add(person);
	}

	private void loadData() {

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
					LocalDate date = LocalDate.parse(data[0]);
					String name = data[1];
					double balance = Double.parseDouble(data[2]);
					people.add(new Person(name, balance, date));
				}
			}
			accountTableView.setItems(people);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void switchToHomePage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void switchToNewAccountPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NewAccountPage.fxml"));
		Parent root = loader.load();

		NewAccountController newAccountController = loader.getController();
		newAccountController.setAccountsPageController(this);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
