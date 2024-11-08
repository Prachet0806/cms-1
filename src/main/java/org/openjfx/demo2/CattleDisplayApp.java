package org.openjfx.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CattleDisplayApp extends Application {
    private Farmer loggedInFarmer;
    private TableView<Cattle> cattleTable;
    private AuditLog auditLog;

    public CattleDisplayApp() {
    }
    public CattleDisplayApp(Farmer loggedInFarmer, AuditLog auditLog) {
        this.loggedInFarmer = loggedInFarmer;
        this.auditLog = auditLog;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Cattle Management - Cattle Details");


        cattleTable = new TableView<>();


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

        cattleTable.getColumns().addAll(nameColumn, breedColumn, ageColumn, weightColumn, vaccinatedColumn);


        cattleTable.getItems().addAll(loggedInFarmer.getCattleList());


        Button addButton = new Button("Add Cattle");
        addButton.setStyle("-fx-background-color: blue; -fx-text-fill: white");
        Button editButton = new Button("Edit Cattle");
        editButton.setStyle("-fx-background-color: green; -fx-text-fill: white");
        Button removeButton = new Button("Remove Cattle");
        removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white");

        addButton.setOnAction(e -> showAddCattleDialog());
        editButton.setOnAction(e -> showEditCattleDialog());
        removeButton.setOnAction(e -> removeSelectedCattle());

        HBox buttonBox = new HBox(10, addButton, editButton, removeButton);
        buttonBox.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setCenter(cattleTable);
        layout.setBottom(buttonBox);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddCattleDialog() {
        Dialog<Cattle> dialog = createCattleDialog("Add New Cattle");
        dialog.showAndWait().ifPresent(cattle -> {
            loggedInFarmer.addCattle(cattle);
            auditLog.addEntry("Added new cattle: " + cattle.getName());
            cattleTable.getItems().add(cattle);
        });
    }

    private void showEditCattleDialog() {
        Cattle selectedCattle = cattleTable.getSelectionModel().getSelectedItem();
        if (selectedCattle != null) {
            Dialog<Cattle> dialog = createCattleDialog("Edit Cattle Details", selectedCattle);
            dialog.showAndWait().ifPresent(updatedCattle -> {
                int index = cattleTable.getItems().indexOf(selectedCattle);
                loggedInFarmer.getCattleList().set(index, updatedCattle);
                auditLog.addEntry("Edited cattle: " + updatedCattle.getName());
                cattleTable.getItems().set(index, updatedCattle);
            });
        } else {
            showAlert("No Cattle Selected", "Please select a cattle to edit.");
        }
    }

    private void removeSelectedCattle() {
        Cattle selectedCattle = cattleTable.getSelectionModel().getSelectedItem();
        if (selectedCattle != null) {
            loggedInFarmer.removeCattle(selectedCattle);
            auditLog.addEntry("Removed cattle: " + selectedCattle.getName());
            cattleTable.getItems().remove(selectedCattle);
        } else {
            showAlert("No Cattle Selected", "Please select a cattle to remove.");
        }
    }

    private Dialog<Cattle> createCattleDialog(String title) {
        return createCattleDialog(title, null);
    }

    private Dialog<Cattle> createCattleDialog(String title, Cattle existingCattle) {
        Dialog<Cattle> dialog = new Dialog<>();
        dialog.setTitle(title);


        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField breedField = new TextField();
        breedField.setPromptText("Breed");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField weightField = new TextField();
        weightField.setPromptText("Weight");
        CheckBox vaccinatedCheckBox = new CheckBox("Vaccinated");

        if (existingCattle != null) {
            nameField.setText(existingCattle.getName());
            breedField.setText(existingCattle.getBreed());
            ageField.setText(String.valueOf(existingCattle.getAge()));
            weightField.setText(String.valueOf(existingCattle.getWeight()));
            vaccinatedCheckBox.setSelected(existingCattle.isVaccinated());
        }

        VBox form = new VBox(10, new Label("Name:"), nameField,
                new Label("Breed:"), breedField,
                new Label("Age:"), ageField,
                new Label("Weight:"), weightField,
                vaccinatedCheckBox);
        form.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    String breed = breedField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    double weight = Double.parseDouble(weightField.getText());
                    boolean vaccinated = vaccinatedCheckBox.isSelected();

                    return new Cattle(name, breed, age, weight, vaccinated);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid age and weight.");
                }
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}