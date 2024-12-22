package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {

	public static Stage stage;

	private final String accountDataFilePath = "src/application/data/Accounts.csv";
	private final String transactionDataFilePath = "src/application/data/Transactions.csv";
	private final String transactionTypeDataFilePath = "src/application/data/TransactionType.csv";
	private final String scheduledTransactionsDataFilePath = "src/application/data/ScheduledTransactions.csv";
	
	@Override
	public void start(Stage primaryStage) {
		Main.stage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));

			Scene homeScene = new Scene(root);
			
			new File(accountDataFilePath).createNewFile();
			new File(transactionDataFilePath).createNewFile();
			new File(transactionTypeDataFilePath).createNewFile();
			new File(scheduledTransactionsDataFilePath).createNewFile();

			List<String> dueTransactions = checkTransactionsDueToday(scheduledTransactionsDataFilePath);
			if (!dueTransactions.isEmpty()) {
				notifyUser(dueTransactions);
			}

			primaryStage.setTitle("Transactify");
			primaryStage.setScene(homeScene);
			primaryStage.show();
			Main.stage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> checkTransactionsDueToday(String csvFile) {
		List<String> dueTransactions = new ArrayList<>();
		int todayDay = java.time.LocalDate.now().getDayOfMonth();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			br.readLine(); // Skip header
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				int transactionDay = Integer.parseInt(fields[5]);
				if (transactionDay == todayDay) {
					dueTransactions.add(fields[2]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dueTransactions;
	}

	private void notifyUser(List<String> transactions) {
		StringBuilder message = new StringBuilder("Transactions due today:\n");
		for (String transaction : transactions) {
			message.append("- ").append(transaction).append("\n");
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Transactions Alert");
		alert.setHeaderText("Reminder!");
		alert.setContentText(message.toString());
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
