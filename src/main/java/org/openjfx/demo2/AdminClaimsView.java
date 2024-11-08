// AdminClaimsView.java
package org.openjfx.demo2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminClaimsView extends Application {

    private TableView<InsuranceClaim> claimsTable;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Submitted Insurance Claims");

        claimsTable = new TableView<>();

        TableColumn<InsuranceClaim, String> cattleNameColumn = new TableColumn<>("Cattle Name");
        cattleNameColumn.setCellValueFactory(new PropertyValueFactory<>("cattleName"));

        TableColumn<InsuranceClaim, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<InsuranceClaim, String> breedColumn = new TableColumn<>("Breed");
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));

        TableColumn<InsuranceClaim, String> reasonColumn = new TableColumn<>("Reason for Claim");
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        claimsTable.getColumns().addAll(cattleNameColumn, ageColumn, breedColumn, reasonColumn);

        ObservableList<InsuranceClaim> claimsList = loadClaimsFromFile();
        claimsTable.setItems(claimsList);

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));
        layout.setCenter(claimsTable);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<InsuranceClaim> loadClaimsFromFile() {
        ObservableList<InsuranceClaim> claims = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("claims.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String cattleName = details[0].trim();
                    int age = Integer.parseInt(details[1].trim());
                    String breed = details[2].trim();
                    String reason = details[3].trim();

                    claims.add(new InsuranceClaim(cattleName, age, breed, reason));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return claims;
    }
}