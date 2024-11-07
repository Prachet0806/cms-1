package gui;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI extends JFrame {
    private static final String ENCRYPTION_KEY = ".uO$P8o}C:Nawc_Y5;rdu&GoD*X]R!iQ";
    private static final String CREDENTIALS_FILE = "C:\\Users\\nilay\\OneDrive\\Documents\\repos\\cms\\src\\gui\\credentials.txt";
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> credentials = new HashMap<>();

    public LoginGUI() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadCredentials();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField(10);
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(10);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginListener());
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
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
            System.err.println("Error loading credentials: " + e.getMessage());
        }
    }

    private void saveCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving credentials: " + e.getMessage());
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

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (credentials.containsKey(username)) {
                String encryptedPassword = credentials.get(username);
                String decryptedPassword = decrypt(encryptedPassword);
                if (password.equals(decryptedPassword)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose(); // Close the login window
                    SwingUtilities.invokeLater(() -> {
                        CattleManagementSystemGUI mainFrame = new CattleManagementSystemGUI();
                        mainFrame.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid password.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Username not found.");
            }
        }
    }

    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
                if (credentials.containsKey(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists.");
                } else {
                    String encryptedPassword = encrypt(password);
                    credentials.put(username, encryptedPassword);
                    saveCredentials();
                    JOptionPane.showMessageDialog(null, "Registration successful! You can now log in.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in both username and password.");
            }
        }
    }
}
