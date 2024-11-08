package org.openjfx.demo2;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InsurancePremiumDisplay extends Application {
    private Farmer loggedInFarmer;
    private TableView<Cattle> premiumTable;

    public InsurancePremiumDisplay() {

    }
    public InsurancePremiumDisplay(Farmer loggedInFarmer) {
        this.loggedInFarmer = loggedInFarmer;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Insurance Premiums");

        premiumTable = new TableView<>();


        TableColumn<Cattle, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Cattle, String> breedColumn = new TableColumn<>("Breed");
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));

        TableColumn<Cattle, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Cattle, Double> weightColumn = new TableColumn<>("Weight");
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Cattle, Boolean> vaccinatedColumn = new TableColumn<>("Vaccinated");
        vaccinatedColumn.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));

        TableColumn<Cattle, Integer> premiumColumn = new TableColumn<>("Premium");
        premiumColumn.setCellValueFactory(new PropertyValueFactory<>("premium"));

        premiumTable.getColumns().addAll(nameColumn, breedColumn, ageColumn, weightColumn, vaccinatedColumn, premiumColumn);


        populatePremiumData();
        loggedInFarmer.getCattleList().addListener((ListChangeListener<Cattle>) change -> populatePremiumData());

        BorderPane layout = new BorderPane();
        layout.setCenter(premiumTable);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void populatePremiumData() {
        premiumTable.getItems().clear();
        for (Cattle cattle : loggedInFarmer.getCattleList()) {
            InsurancePolicy policy = new InsurancePolicy(cattle);
            cattle.setPremium(policy.getPremium());
            premiumTable.getItems().add(cattle);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}