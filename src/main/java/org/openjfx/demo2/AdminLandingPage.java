package org.openjfx.demo2;
import javafx.scene.control.TextArea;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class AdminLandingPage extends Application {
    private List<InsuranceClaim> allClaims = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cattle Management");

        // ---------------------------- Navbar ----------------------------
        Button homeButton = new Button("Home");
        Button claimsButton = new Button("Claims");
        Button logsButton = new Button("Logs");
        Button contactButton = new Button("Contact");
        Button logoutButton = new Button("Logout");  // New Logout Button

        homeButton.getStyleClass().add("navbar-button");
        claimsButton.getStyleClass().add("navbar-button");
        logsButton.getStyleClass().add("navbar-button");
        claimsButton.getStyleClass().add("navbar-button");
        contactButton.getStyleClass().add("navbar-button");


        logoutButton.getStyleClass().add("navbar-button"); // Add style for logout
        logoutButton.setOnAction(e -> {
            try {
                CMS cms = new CMS();
                cms.start(primaryStage); // Return to CMS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        claimsButton.setOnAction(e -> openClaimsWindow());
        logsButton.setOnAction(e -> displayLogs());


        HBox navbar = new HBox(10); // Spacing between navbar buttons
        navbar.setAlignment(Pos.CENTER); // Align center
        navbar.setPadding(new Insets(10)); // Padding around the navbar
        navbar.getChildren().addAll(homeButton, logsButton, claimsButton, contactButton, logoutButton); // Add Logout

        homeButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        logsButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        claimsButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        contactButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));
        logoutButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.15));

        homeButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        logsButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        claimsButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        contactButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        logoutButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        // --------------------------- Main Content ---------------------------
        Button logsMainButton = new Button("Logs");
        Button claimsMainButton = new Button("Insurance Claims");

        logsMainButton.setOnAction(e -> displayLogs());
        claimsMainButton.setOnAction(e -> openClaimsWindow());

        logsMainButton.getStyleClass().addAll("button", "box2");
        claimsMainButton.getStyleClass().addAll("button", "box5");
        logoutButton.getStyleClass().addAll("navbar-button", "logout");

        HBox hbox = new HBox(20); // Spacing between buttons
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(logsMainButton, claimsMainButton);

        HBox.setHgrow(logsMainButton, Priority.ALWAYS);
        HBox.setHgrow(claimsMainButton, Priority.ALWAYS);

        logsMainButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.4));
        claimsMainButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.4));

        logsMainButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.7));
        claimsMainButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.7));

        logsMainButton.styleProperty().bind(primaryStage.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));
        claimsMainButton.styleProperty().bind(primaryStage.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));

        // --------------------------- Layout Setup ---------------------------
        VBox contentContainer = new VBox(hbox);
        contentContainer.setAlignment(Pos.CENTER); // Center HBox horizontally and vertically
        VBox.setVgrow(contentContainer, Priority.ALWAYS); // Allow the VBox to grow and fill space

        VBox mainLayout = new VBox(navbar, contentContainer);
        mainLayout.setPadding(new Insets(10, 10, 0, 10)); // Set 10px padding on all sides except bottom
        mainLayout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(mainLayout, 800, 600);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void displayLogs() {
        AuditLog auditLog = new AuditLog();
        List<String> logs = auditLog.getEntries();

        if (logs.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No logs found.");
            alert.showAndWait();
            return;
        }

        Stage logStage = new Stage();
        logStage.setTitle("Audit Logs");

        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        logs.forEach(log -> logArea.appendText(log + "\n"));

        VBox layout = new VBox(logArea);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(logArea, Priority.ALWAYS); // Allow TextArea to grow with VBox

        Scene scene = new Scene(layout, 400, 400);

        layout.prefWidthProperty().bind(scene.widthProperty());
        layout.prefHeightProperty().bind(scene.heightProperty());

        logArea.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        logArea.setMinSize(0, 0);

        logStage.setScene(scene);
        logStage.show();
    }

    private void openClaimsWindow() {
        Stage claimsStage = new Stage();
        claimsStage.setTitle("Insurance Claims");

        loadClaimsFromFile(); // Load claims from the claims file

        ListView<String> claimListView = new ListView<>();
        for (InsuranceClaim claim : allClaims) {
            claimListView.getItems().add(claim.toString());
        }

        Button approveButton = new Button("Approve");
        Button denyButton = new Button("Deny");

        approveButton.setOnAction(e -> handleClaimApproval(claimListView.getSelectionModel().getSelectedItem(), "Approved"));
        denyButton.setOnAction(e -> handleClaimApproval(claimListView.getSelectionModel().getSelectedItem(), "Denied"));

        VBox layout = new VBox(10, claimListView, approveButton, denyButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 300);
        claimsStage.setScene(scene);
        claimsStage.show();
    }

    private void loadClaimsFromFile() {
        allClaims.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("claims.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] claimData = line.split(",");
                String cattleName = claimData[0];
                int cattleAge = Integer.parseInt(claimData[1]);
                String cattleBreed = claimData[2];
                String reason = claimData[3];
                String status = claimData[4];

                InsuranceClaim claim = new InsuranceClaim(cattleName, cattleAge, cattleBreed, reason);
                claim.setStatus(status);
                allClaims.add(claim);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClaimApproval(String selectedClaimStr, String status) {
        if (selectedClaimStr == null) {
            showAlert("Please select a claim first.");
            return;
        }

        for (InsuranceClaim claim : allClaims) {
            if (claim.toString().equals(selectedClaimStr)) {
                claim.setStatus(status);
                updateClaimInFile(claim);
                showAlert("Claim " + status);
                return;
            }
        }
    }

    private void updateClaimInFile(InsuranceClaim updatedClaim) {
        List<InsuranceClaim> updatedClaims = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("claims.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] claimData = line.split(",");
                String cattleName = claimData[0];
                int cattleAge = Integer.parseInt(claimData[1]);
                String cattleBreed = claimData[2];
                String reason = claimData[3];
                String status = claimData[4];

                InsuranceClaim claim = new InsuranceClaim(cattleName, cattleAge, cattleBreed, reason);
                claim.setStatus(status);
                updatedClaims.add(claim);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("claims.txt"))) {
            for (InsuranceClaim claim : updatedClaims) {
                writer.write(claim.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}