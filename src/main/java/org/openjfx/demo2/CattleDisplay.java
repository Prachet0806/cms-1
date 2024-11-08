package org.openjfx.demo2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CattleDisplay extends Application {

    private Farmer farmer;
    private TableView<Cattle> cattleTable;

    public CattleDisplay(Farmer farmer) {
        this.farmer = farmer;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cattle Management");

        // Create TableView for displaying cattle
        cattleTable = new TableView<>();
        setupTableColumns();

        // Buttons for adding, editing, and removing cattle
        Button addButton = new Button("Add Cattle");
        addButton.setOnAction(e -> showAddCattleDialog());

        Button editButton = new Button("Edit Selected Cattle");
        editButton.setOnAction(e -> showEditCattleDialog());

        Button removeButton = new Button("Remove Selected Cattle");
        removeButton.setOnAction(e -> removeSelectedCattle());

        VBox buttonBox = new VBox(addButton, editButton, removeButton);

        BorderPane layout = new BorderPane();
        layout.setCenter(cattleTable);
        layout.setBottom(buttonBox);

        updateTable();

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTableColumns() {
        TableColumn<Cattle, String> breedCol = new TableColumn<>("Breed");
        breedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBreed()));

        TableColumn<Cattle, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

        TableColumn<Cattle, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getWeight()).asObject());

        TableColumn<Cattle, Boolean> vaccinatedCol = new TableColumn<>("Vaccinated");
        vaccinatedCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isVaccinated()));

        cattleTable.getColumns().addAll(breedCol, ageCol, weightCol, vaccinatedCol);
    }

    private void updateTable() {
        ObservableList<Cattle> data = FXCollections.observableArrayList(farmer.getCattleList());
        cattleTable.setItems(data);
    }

    private void showAddCattleDialog() {
        Dialog<Cattle> dialog = createCattleDialog("Add Cattle", null);

        dialog.showAndWait().ifPresent(cattle -> {
            farmer.addCattle(cattle);
            updateTable();
        });
    }

    private void showEditCattleDialog() {
        Cattle selectedCattle = cattleTable.getSelectionModel().getSelectedItem();

        if (selectedCattle != null) {
            Dialog<Cattle> dialog = createCattleDialog("Edit Cattle", selectedCattle);

            dialog.showAndWait().ifPresent(cattle -> {
                int index = farmer.getCattleList().indexOf(selectedCattle);
                if (index != -1) {
                    farmer.getCattleList().set(index, cattle);
                    updateTable();
                }
            });
        } else {
            showAlert("No Selection", "Please select a cattle to edit.");
        }
    }

    private void removeSelectedCattle() {
        Cattle selectedCattle = cattleTable.getSelectionModel().getSelectedItem();

        if (selectedCattle != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this cattle?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    farmer.removeCattle(selectedCattle);
                    updateTable();
                }
            });
        } else {
            showAlert("No Selection", "Please select a cattle to remove.");
        }
    }

    private Dialog<Cattle> createCattleDialog(String title, Cattle existingCattle) {
        Dialog<Cattle> dialog = new Dialog<>();

        dialog.setTitle(title);

        Label breedLabel = new Label("Breed:");
        TextField breedField = new TextField(existingCattle != null ? existingCattle.getBreed() : "");

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField(existingCattle != null ? String.valueOf(existingCattle.getAge()) : "");

        Label weightLabel = new Label("Weight:");
        TextField weightField = new TextField(existingCattle != null ? String.valueOf(existingCattle.getWeight()) : "");

        Label vaccinatedLabel = new Label("Vaccinated:");
        CheckBox vaccinatedCheckBox = new CheckBox();

        if (existingCattle != null) {
            vaccinatedCheckBox.setSelected(existingCattle.isVaccinated());
        }

        VBox vbox = new VBox(breedLabel, breedField,
                ageLabel, ageField,
                weightLabel, weightField,
                vaccinatedLabel, vaccinatedCheckBox);

        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String name="placeholder";
                    String breed = breedField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    double weight = Double.parseDouble(weightField.getText());
                    boolean vaccinated = vaccinatedCheckBox.isSelected();

                    return new Cattle(name, breed, age, weight, vaccinated);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers for age and weight.");
                }
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}