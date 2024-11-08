package org.openjfx.demo2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InsuranceClaimForm {
    private Farmer farmer;

    public InsuranceClaimForm(Farmer farmer) {
        this.farmer = farmer;
    }

    public void showForm() {
        Stage stage = new Stage();
        stage.setTitle("Submit Insurance Claim");

        Label cattleNameLabel = new Label("Cattle Name:");
        TextField cattleNameField = new TextField();

        Label cattleAgeLabel = new Label("Cattle Age:");
        TextField cattleAgeField = new TextField();

        Label cattleBreedLabel = new Label("Cattle Breed:");
        TextField cattleBreedField = new TextField();

        Label reasonLabel = new Label("Reason for Claim:");
        TextArea reasonField = new TextArea();

        Button submitButton = new Button("Submit Claim");
        submitButton.setOnAction(e -> {
            try {
                int cattleAge = Integer.parseInt(cattleAgeField.getText().trim());
                submitClaim(cattleNameField.getText().trim(), cattleAge, cattleBreedField.getText().trim(), reasonField.getText().trim(), stage);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid integer for cattle age.");
            }
        });

        VBox layout = new VBox(10, cattleNameLabel, cattleNameField, cattleAgeLabel, cattleAgeField, cattleBreedLabel, cattleBreedField, reasonLabel, reasonField, submitButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 300, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void submitClaim(String cattleName, int cattleAge, String cattleBreed, String reason, Stage stage) {
        InsuranceClaim claim = new InsuranceClaim(cattleName, cattleAge, cattleBreed, reason);
        farmer.addClaim(claim);

        if (writeClaimToFile(claim)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Claim submitted successfully!");
            stage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit claim. Please try again.");
        }
    }

    private boolean writeClaimToFile(InsuranceClaim claim) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("claims.txt", true))) {
            writer.write(claim.toFileString());
            writer.newLine();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}