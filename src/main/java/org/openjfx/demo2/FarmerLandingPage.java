package org.openjfx.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Objects;

public class FarmerLandingPage extends Application {
    private Farmer loggedInFarmer;
    private AuditLog auditLog;

    public FarmerLandingPage(Farmer loggedInFarmer) {
        this.loggedInFarmer = loggedInFarmer;
        this.auditLog = new AuditLog();
        auditLog.addEntry("Farmer " + loggedInFarmer.getName() + " logged in.");
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cattle Management");

        // ---------------------------- Navbar ----------------------------
        Button homeButton = new Button("Home");
        Button cattleButton = new Button("Cattle");
        Button insuranceButton = new Button("Insurance");
        Button claimsButton = new Button("Claims");
        Button contactButton = new Button("Contact");
        Button logoutButton = new Button("Logout");

        homeButton.getStyleClass().add("navbar-button");
        cattleButton.getStyleClass().add("navbar-button");
        insuranceButton.getStyleClass().add("navbar-button");
        claimsButton.getStyleClass().add("navbar-button");
        contactButton.getStyleClass().add("navbar-button");

        cattleButton.setOnAction(e -> showCattleDetails());
        insuranceButton.setOnAction(e -> showInsurancePremiumTable());


        logoutButton.getStyleClass().add("navbar-button");
        logoutButton.setOnAction(e -> {
            try {
                CMS cms = new CMS();
                cms.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox navbar = new HBox(10);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(10));
        navbar.getChildren().addAll(homeButton, cattleButton, insuranceButton, claimsButton, contactButton, logoutButton);

        homeButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));
        cattleButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));
        insuranceButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));
        claimsButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));
        contactButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));
        logoutButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.12));

        homeButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        cattleButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        insuranceButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        claimsButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        contactButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        logoutButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.07));
        // --------------------------- Main Content ---------------------------
        Button cattleMainButton = new Button("Cattle");
        Button insuranceMainButton = new Button("Insurance Premiums");
        Button claimsMainButton = new Button("Insurance Claims");
        claimsButton.setOnAction(e -> openClaimForm());
        claimsMainButton.setOnAction(e -> openClaimForm());

        cattleMainButton.getStyleClass().addAll("button", "box1");
        insuranceMainButton.getStyleClass().addAll("button", "box2");
        claimsMainButton.getStyleClass().addAll("button", "box5");
        logoutButton.getStyleClass().addAll("navbar-button", "logout");

        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(cattleMainButton, insuranceMainButton, claimsMainButton);

        HBox.setHgrow(cattleMainButton, Priority.ALWAYS);
        HBox.setHgrow(insuranceMainButton, Priority.ALWAYS);
        HBox.setHgrow(claimsMainButton, Priority.ALWAYS);

       cattleMainButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.3));
        insuranceMainButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.3));
        claimsMainButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.3));

        cattleMainButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.7));
        insuranceMainButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.7));
        claimsMainButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.7));

        cattleMainButton.styleProperty().bind(primaryStage.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));
        insuranceMainButton.styleProperty().bind(primaryStage.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));
        claimsMainButton.styleProperty().bind(primaryStage.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));


        cattleMainButton.setOnAction(e -> showCattleDetails());
        insuranceMainButton.setOnAction(e -> showInsurancePremiumTable());

// --------------------------- Layout Setup ---------------------------
        VBox contentContainer = new VBox(hbox);
        contentContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        VBox mainLayout = new VBox(navbar, contentContainer);
        mainLayout.setPadding(new Insets(10, 10, 0, 10));
        mainLayout.setAlignment(Pos.TOP_CENTER);


        Scene scene = new Scene(mainLayout, 800, 600);


        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCattleDetails() {

        CattleDisplayApp cattleDisplayApp = new CattleDisplayApp(loggedInFarmer, auditLog);
        cattleDisplayApp.start(new Stage());
    }
    private void showInsurancePremiumTable() {
        auditLog.addEntry("Viewing insurance premiums.");
        InsurancePremiumDisplay premiumDisplay = new InsurancePremiumDisplay(loggedInFarmer);
        premiumDisplay.start(new Stage());
    }
    private void openClaimForm() {
        InsuranceClaimForm claimForm = new InsuranceClaimForm(loggedInFarmer);
        claimForm.showForm();
    }
    public static void main(String[] args) {
        launch(args);
    }
}