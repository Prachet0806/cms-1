package org.openjfx.demo3;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Objects;

public class CMS extends Application {
    private VBox signupForm;
    private VBox loginForm;
    private VBox adminForm;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CMS");

        // Left Section with Branding
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setStyle("-fx-background-color: linear-gradient(to right, #6ec1e4, #9b64e4);");
        leftSection.setPrefWidth(300); // Default width
        leftSection.setMinWidth(250); // Minimum width to ensure it doesn't shrink too much
        leftSection.setMaxWidth(500); // Maximum width to limit expansion

        Label brandingTitle = new Label("Cattle Management System");
        brandingTitle.setStyle("-fx-font-size: 24px; -fx-font-family: 'Pacifico'; -fx-text-fill: white;");
        Label brandingSubtext = new Label("Your Partner in Cattle Management");
        brandingSubtext.setStyle("-fx-text-fill: white;");

        leftSection.getChildren().addAll(brandingTitle, brandingSubtext);

        // Right Section with Tabs and Forms
        VBox rightSection = new VBox(20);
        rightSection.setAlignment(Pos.CENTER);

        // Create Tabs
        HBox formTabs = new HBox(10);
        formTabs.setAlignment(Pos.CENTER);

        Button signupTab = createStyledButton("Farmer Sign Up");
        Button loginTab = createStyledButton("Sign In");
        Button adminTab = createStyledButton("Admin Registration");

        signupTab.setOnAction(e -> showForm("signup"));
        loginTab.setOnAction(e -> showForm("login"));
        adminTab.setOnAction(e -> showForm("admin"));

        formTabs.getChildren().addAll(signupTab, loginTab, adminTab);

        // Forms
        signupForm = createSignUpForm();
        loginForm = createLoginForm();
        adminForm = createAdminForm();

        // Initially, show the SignUp form
        rightSection.getChildren().addAll(formTabs, signupForm, loginForm, adminForm);
        showForm("signup");

        // Main Container
        HBox mainContainer = new HBox();
        mainContainer.getChildren().addAll(leftSection, rightSection);
        HBox.setHgrow(rightSection, Priority.ALWAYS); // Allow rightSection to grow
        HBox.setHgrow(leftSection, Priority.ALWAYS); // Allow leftSection to grow

        // Set Scene and Stage
        Scene scene = new Scene(mainContainer, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to switch between forms
    private void showForm(String formType) {
        signupForm.setVisible(false);
        loginForm.setVisible(false);
        adminForm.setVisible(false);

        switch (formType) {
            case "signup":
                signupForm.setVisible(true);
                break;
            case "login":
                loginForm.setVisible(true);
                break;
            case "admin":
                adminForm.setVisible(true);
                break;
        }
    }

    // Create SignUp Form
    private VBox createSignUpForm() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);
        form.setPadding(new Insets(20));

        TextField farmerName = new TextField();
        farmerName.setPromptText("Farmer Name");
        farmerName.setPrefHeight(40); // Set preferred height
        farmerName.setMaxWidth(400); // Set maximum width

        TextField farmerAddress = new TextField();
        farmerAddress.setPromptText("Address");
        farmerAddress.setPrefHeight(40); // Set preferred height
        farmerAddress.setMaxWidth(400); // Set maximum width

        TextField farmerEmail = new TextField();
        farmerEmail.setPromptText("Email ID");
        farmerEmail.setPrefHeight(40); // Set preferred height
        farmerEmail.setMaxWidth(400); // Set maximum width

        Button signUpButton = createStyledButton("Sign Up");

        form.getChildren().addAll(farmerName, farmerAddress, farmerEmail, signUpButton);
        return form;
    }

    // Create Login Form
    private VBox createLoginForm() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);
        form.setPadding(new Insets(20));

        TextField farmerId = new TextField();
        farmerId.setPromptText("Farmer ID");
        farmerId.setPrefHeight(40); // Set preferred height
        farmerId.setMaxWidth(400); // Set maximum width

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40); // Set preferred height
        passwordField.setMaxWidth(400); // Set maximum width

        Button loginButton = createStyledButton("Login");

        form.getChildren().addAll(farmerId, passwordField, loginButton);
        return form;
    }

    // Create Admin Registration Form
    private VBox createAdminForm() {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);
        form.setPadding(new Insets(20));

        TextField adminName = new TextField();
        adminName.setPromptText("Admin Name");
        adminName.setPrefHeight(40); // Set preferred height
        adminName.setMaxWidth(400); // Set maximum width

        TextField email = new TextField();
        email.setPromptText("Email");
        email.setPrefHeight(40); // Set preferred height
        email.setMaxWidth(400); // Set maximum width

        Button registerButton = createStyledButton("Register");

        form.getChildren().addAll(adminName, email, registerButton);
        return form;
    }

    // Method to create styled buttons
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #6ec1e4; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 1);");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #9b64e4; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0, 0, 1);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #6ec1e4; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 1);");
        });

        return button;
    }
}
