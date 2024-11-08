package org.openjfx.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CMS extends Application {
    private static final String ENCRYPTION_KEY = ".uO$P8o}C:Nawc_Y5;rdu&GoD*X]R!iQ";
    private static final String CREDENTIALS_FILE = "C:\\Users\\prach\\Documents\\2nd Year\\OOPS\\Mini-project\\demo2\\src\\main\\java\\org\\openjfx\\demo2\\credentials.txt";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final AuditLog auditLog = new AuditLog();

    private VBox signupForm;
    private VBox loginForm;
    private VBox adminForm;
    private Map<String, String> credentials = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CMS");

        loadCredentials(); // Load credentials on startup

        HBox mainContainer = new HBox();
        Scene scene = new Scene(mainContainer, 800, 600);

        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setStyle("-fx-background-color: linear-gradient(to right, #6ec1e4, #9b64e4);");
        leftSection.prefWidthProperty().bind(scene.widthProperty().multiply(0.3));
        Label brandingTitle = new Label("Cattle Management System");
        Label brandingSubtext = new Label("Your Partner in Cattle Management");

        brandingTitle.styleProperty().bind(primaryStage.widthProperty().add(1).multiply(0.03).asString("-fx-font-size: %.2fpx; -fx-text-fill: white;"));
        brandingSubtext.styleProperty().bind(primaryStage.widthProperty().add(1).multiply(0.02).asString("-fx-font-size: %.2fpx; -fx-text-fill: white;"));
        leftSection.getChildren().addAll(brandingTitle, brandingSubtext);

        VBox rightSection = new VBox(20);
        rightSection.setAlignment(Pos.CENTER);

        HBox formTabs = new HBox(10);
        formTabs.setAlignment(Pos.CENTER);
        Button signupTab = createStyledButton("Farmer Sign Up");
        Button loginTab = createStyledButton("Sign In");
        Button adminTab = createStyledButton("Admin Login");

        signupTab.setOnAction(e -> showForm("signup"));
        loginTab.setOnAction(e -> showForm("login"));
        adminTab.setOnAction(e -> showForm("admin"));

        formTabs.getChildren().addAll(signupTab, loginTab, adminTab);

        StackPane formStackPane = new StackPane();
        signupForm = createSignUpForm(scene);
        loginForm = createLoginForm(scene);
        adminForm = createAdminForm(scene);
        formStackPane.getChildren().addAll(signupForm, loginForm, adminForm);
        showForm("signup");

        rightSection.getChildren().addAll(formTabs, formStackPane);
        mainContainer.getChildren().addAll(leftSection, rightSection);

        HBox.setHgrow(rightSection, Priority.ALWAYS);
        HBox.setHgrow(leftSection, Priority.ALWAYS);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void validateEmail(String email) throws InvalidEmailException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new InvalidEmailException("Invalid email format.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new InvalidPhoneNumberException("Phone number must be exactly 10 digits.");
        }
    }

    private VBox createSignUpForm(Scene scene) {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);
        form.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        TextField farmerAddress = new TextField();
        farmerAddress.setPromptText("Address");
        farmerAddress.setPrefHeight(40);
        farmerAddress.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        TextField farmerEmail = new TextField();
        farmerEmail.setPromptText("Email ID");
        farmerEmail.setPrefHeight(40);
        farmerEmail.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        TextField farmerNumber = new TextField();
        farmerNumber.setPromptText("Phone Number");
        farmerNumber.setPrefHeight(40);
        farmerNumber.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        Button signUpButton = createStyledButton("Sign Up");
        signUpButton.prefWidthProperty().bind(scene.widthProperty().multiply(0.3));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));

        signUpButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String email = farmerEmail.getText().trim();
            String phoneNumber = farmerNumber.getText().trim();

            try {
                // Validate email and phone number
                validateEmail(email);
                validatePhoneNumber(phoneNumber);

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (credentials.containsKey(username)) {
                        showAlert("Error", "Username already exists.");
                    } else {
                        String encryptedPassword = encrypt(password);
                        credentials.put(username, encryptedPassword);
                        saveCredentials();
                        showAlert("Success", "Registration successful! You can now log in.");
                        usernameField.clear();
                        passwordField.clear();
                        farmerAddress.clear();
                        farmerEmail.clear();
                        farmerNumber.clear();
                    }
                } else {
                    showAlert("Error", "Please fill in both username and password.");
                }
            } catch (InvalidEmailException ex) {
                showAlert("Error", ex.getMessage());
            } catch (InvalidPhoneNumberException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.getChildren().addAll(usernameField, farmerAddress, farmerEmail, farmerNumber, passwordField, signUpButton);
        return form;
    }
    private VBox createLoginForm(Scene scene) {
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);
        form.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Farmer ID");
        usernameField.setPrefHeight(40);
        usernameField.prefWidthProperty().bind(scene.widthProperty().multiply(0.5)); // Bind to 60% of stage width
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.prefWidthProperty().bind(scene.widthProperty().multiply(0.5)); // Bind to 60% of stage width
        Button loginButton = createStyledButton("Login");
        loginButton.prefWidthProperty().bind(scene.widthProperty().multiply(0.3)); // Bind to 30% of stage width
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            Farmer fr=new Farmer(username);
            if (credentials.containsKey(username)) {
                String encryptedPassword = credentials.get(username);
                String decryptedPassword = decrypt(encryptedPassword);

                if (password.equals(decryptedPassword)) {
                    showAlert("Success", "Login successful!");
                    auditLog.addEntry("User " + username + " successfully logged in.");
                    // Redirect to the FarmerLandingPage
                    try {
                        FarmerLandingPage landingPage = new FarmerLandingPage(fr); // Assume FarmerLandingPage exists
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        landingPage.start(stage); // Start FarmerLandingPage in the same window
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    showAlert("Error", "Invalid password.");
                    auditLog.addEntry("Failed login attempt for user: " + username);
                }
            } else {
                showAlert("Error", "Username not found.");
                auditLog.addEntry("Failed login attempt for user: " + username);
            }
        });

        form.getChildren().addAll(usernameField, passwordField, loginButton);
        return form;
    }

    private VBox createAdminForm(Scene scene) {
    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);
    form.setVisible(false);
    form.setPadding(new Insets(20));

    TextField adminName = new TextField();
    adminName.setPromptText("Admin Username");
    adminName.setPrefHeight(40);
    adminName.prefWidthProperty().bind(scene.widthProperty().multiply(0.5)); // Bind to 60% of stage width
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));

    Button adminLoginButton = createStyledButton("Admin Login");
    adminLoginButton.setOnAction(e -> {
        String username = adminName.getText().trim();
        String password = passwordField.getText();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            showAlert("Success", "Admin login successful!");

            // Redirect to the admin dashboard or perform admin-specific tasks
            try {
                AdminLandingPage landingPage = new AdminLandingPage();
                Stage stage = (Stage) adminLoginButton.getScene().getWindow();
                landingPage.start(stage); // Start FarmerLandingPage in the same window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            showAlert("Error", "Invalid admin credentials.");
        }
    });

    form.getChildren().addAll(adminName, passwordField, adminLoginButton);
    return form;
}

    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0];
                    String encryptedPassword = parts[1];
                    credentials.put(username, encryptedPassword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encrypt(String password) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decrypt(String encryptedPassword) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #6ec1e4; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 5px; -fx-border-radius: 5px;");
        return button;
    }

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
}