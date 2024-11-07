package gui;

import logging.AuditLog;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CattleManagementSystemGUI extends JFrame {
    private static final String FARMER_FILE = "farmers.txt";
    private List<Farmer> farmers = new ArrayList<>();
    private JPanel actionPanel;
    private List<InsurancePolicy> policies = new ArrayList<>();
    private List<InsuranceClaim> claims = new ArrayList<>();
    private AuditLog auditLog = new AuditLog();
    private JTextArea displayArea;
    private JTextField farmerNameField;
    private JTextField cattleNameField;
    private JTextField breedField;
    private JTextField ageField;
    private JTextField healthField;
    private JCheckBox vaccinatedCheckBox;
    private static final String ENCRYPTION_KEY = ".uO$P8o}C:Nawc_Y5;rdu&GoD*X]R!iQ";
    private static final String CREDENTIALS_FILE = "C:\\Users\\nilay\\OneDrive\\Documents\\repos\\cms\\src\\gui\\credentials.txt";
    private JComboBox<String> farmerComboBox;
    private JButton editFarmerButton;
    public CattleManagementSystemGUI() {
        loadFarmers();
        setTitle("Cattle Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        farmerNameField = new JTextField(10);
        cattleNameField = new JTextField(10);
        breedField = new JTextField(10);
        ageField = new JTextField(5);
        healthField = new JTextField(10);
        vaccinatedCheckBox = new JCheckBox("Vaccinated");



        JButton addFarmerButton = new JButton("Add Farmer");
        addFarmerButton.addActionListener(new AddFarmerListener());

        JButton addCattleButton = new JButton("Add Cattle");
        addCattleButton.addActionListener(new AddCattleListener());

        JButton displayFarmersButton = new JButton("Display Farmers");
        displayFarmersButton.addActionListener(new DisplayFarmersListener());

        JButton showInsuranceButton = new JButton("Show Insurance Policies");
        showInsuranceButton.addActionListener(new ShowInsuranceListener());

        JButton viewAuditLogButton = new JButton("View Audit Log");
        viewAuditLogButton.addActionListener(new ViewAuditLogListener());

        JButton manageClaimsButton = new JButton("Manage Claims");
        manageClaimsButton.addActionListener(new ManageClaimsListener());

        JButton displayCattleButton = new JButton("Display Cattle");
        displayCattleButton.addActionListener(new DisplayCattleListener());

        // Initialize actionPanel here as the class-level variable
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS)); // Set desired layout
        actionPanel.add(displayFarmersButton);
        actionPanel.add(showInsuranceButton);
        actionPanel.add(viewAuditLogButton);
        actionPanel.add(manageClaimsButton);
        actionPanel.add(displayCattleButton);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Farmer Name:"));
        inputPanel.add(farmerNameField);
        inputPanel.add(addFarmerButton);

        JPanel cattlePanel = new JPanel();
        cattlePanel.add(new JLabel("Cattle Name:"));
        cattlePanel.add(cattleNameField);
        cattlePanel.add(new JLabel("Breed:"));
        cattlePanel.add(breedField);
        cattlePanel.add(new JLabel("Age:"));
        cattlePanel.add(ageField);
        cattlePanel.add(new JLabel("Health:"));
        cattlePanel.add(healthField);
        cattlePanel.add(vaccinatedCheckBox);
        cattlePanel.add(addCattleButton);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(cattlePanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.WEST);

        policies.add(new InsurancePolicy("Basic Coverage", "Covers common illnesses and accidents"));
        policies.add(new InsurancePolicy("Comprehensive Coverage", "Includes coverage for major illnesses and accidents, plus routine check-ups"));
        policies.add(new InsurancePolicy("High-Risk Coverage", "Specialized coverage for high-risk breeds or older cattle"));

        addSampleData();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveFarmers();  // Save farmers on application exit
            }
        });
    }
    private void saveFarmers() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FARMER_FILE);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(farmers);
            String encodedFarmers = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            fileOutputStream.write(encodedFarmers.getBytes());
            System.out.println("Farmers saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load farmers list from a Base64-encoded file
    private void loadFarmers() {
        File file = new File(FARMER_FILE);
        if (!file.exists()) {
            return;  // No file to load from, start with an empty list
        }

        try (FileInputStream fileInputStream = new FileInputStream(FARMER_FILE);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            int read;
            while ((read = fileInputStream.read()) != -1) {
                byteArrayOutputStream.write(read);
            }
            byte[] data = Base64.getDecoder().decode(byteArrayOutputStream.toByteArray());

            try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
                farmers = (List<Farmer>) objectInputStream.readObject();
                System.out.println("Farmers loaded successfully.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSampleData() {
        Farmer farmer = new Farmer("Jeff");
        farmers.add(farmer);

        Cattle cattle = new Cattle("PLACEHOLDER", "PLACEHOLDER", 5, "Healthy", true);
        farmer.addCattle(cattle);

        InsurancePolicy policy = policies.get(0);
        InsuranceClaim claim = new InsuranceClaim(cattle, farmer, policy);
        claims.add(claim);
    }

    private class AddFarmerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String farmerName = farmerNameField.getText().trim();
            if (!farmerName.isEmpty()) {
                Farmer farmer = new Farmer(farmerName);
                farmers.add(farmer);
                displayArea.append("Farmer added: " + farmerName + "\n");
                auditLog.addEntry("Added Farmer: " + farmerName);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a farmer name.");
            }
        }
    }

    private class AddCattleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String farmerName = farmerNameField.getText().trim();
            String cattleName = cattleNameField.getText().trim();
            String breed = breedField.getText().trim();
            String health = healthField.getText().trim();
            boolean vaccinated = vaccinatedCheckBox.isSelected();

            int age;
            try {
                age = Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid age. Please enter a valid age.");
                return;
            }

            if (!farmerName.isEmpty() && !cattleName.isEmpty() && !breed.isEmpty() && !health.isEmpty()) {
                Cattle cattle = new Cattle(cattleName, breed, age, health, vaccinated);
                for (Farmer farmer : farmers) {
                    if (farmer.getName().equals(farmerName)) {
                        farmer.addCattle(cattle);
                        displayArea.append("Cattle added: " + cattleName + " to farmer " + farmerName + "\n");
                        auditLog.addEntry("Added Cattle: " + cattleName + " to farmer " + farmerName);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all cattle details.");
            }
        }
    }

    private class DisplayFarmersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder farmerList = new StringBuilder("Farmers:\n");
            for (Farmer farmer : farmers) {
                farmerList.append(farmer.getName()).append("\n");
            }
            displayArea.setText(farmerList.toString());
        }
    }

    private class ShowInsuranceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder policyList = new StringBuilder("Insurance Policies:\n");
            for (InsurancePolicy policy : policies) {
                policyList.append(policy.getPolicyName()).append(" - ").append(policy.getDescription()).append("\n");
            }
            displayArea.setText(policyList.toString());
        }
    }

    private class ViewAuditLogListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder logEntries = new StringBuilder("Audit Log:\n");
            for (String entry : auditLog.getEntries()) {
                logEntries.append(entry).append("\n");
            }
            displayArea.setText(logEntries.toString());
        }
    }
    private class DisplayCattleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder cattleList = new StringBuilder("Cattle Details:\n\n");
            for (Farmer farmer : farmers) {
                cattleList.append("Farmer: ").append(farmer.getName()).append("\n");
                List<Cattle> cattleListForFarmer = farmer.getCattleList(); // Ensure Farmer has getCattleList()
                if (cattleListForFarmer.isEmpty()) {
                    cattleList.append("  No cattle registered.\n\n");
                    continue;
                }
                for (Cattle cattle : cattleListForFarmer) {
                    cattleList.append("  Name: ").append(cattle.getName()).append("\n");
                    cattleList.append("  Breed: ").append(cattle.getBreed()).append("\n");
                    cattleList.append("  Age: ").append(cattle.getAge()).append("\n");
                    cattleList.append("  Health Condition: ").append(cattle.getHealthCondition()).append("\n");
                    cattleList.append("  Vaccinated: ").append(cattle.isVaccinated() ? "Yes" : "No").append("\n\n");
                }
            }
            displayArea.setText(cattleList.toString());
            auditLog.addEntry("Displayed Cattle Details");
        }
    }
    private class ManageClaimsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ManageClaimsGUI manageClaimsGUI = new ManageClaimsGUI();
            manageClaimsGUI.setVisible(true);
        }
    }

    private class ManageClaimsGUI extends JFrame {
        private JTextArea displayArea;
        private JButton approveButton;
        private JButton rejectButton;
        private JComboBox<InsuranceClaim> claimComboBox;

        public ManageClaimsGUI() {
            setTitle("Manage Insurance Claims");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            displayArea = new JTextArea();
            displayArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(displayArea);

            claimComboBox = new JComboBox<>();
            populateClaimsComboBox();

            approveButton = new JButton("Approve Claim");
            approveButton.addActionListener(new ApproveClaimListener());

            rejectButton = new JButton("Reject Claim");
            rejectButton.addActionListener(new RejectClaimListener());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(new JLabel("Select Claim:"));
            buttonPanel.add(claimComboBox);
            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);

            add(scrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            updateClaimDisplay();
        }

        private void populateClaimsComboBox() {
            claimComboBox.removeAllItems();
            for (InsuranceClaim claim : claims) {
                claimComboBox.addItem(claim);
            }
        }

        private void updateClaimDisplay() {
            StringBuilder claimList = new StringBuilder("Insurance Claims:\n");
            if (claims.isEmpty()) {
                claimList.append("No claims available.");
            } else {
                for (InsuranceClaim claim : claims) {
                    claimList.append(claim.toString()).append("\n");
                }
            }
            displayArea.setText(claimList.toString());
        }

        private class ApproveClaimListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsuranceClaim selectedClaim = (InsuranceClaim) claimComboBox.getSelectedItem();
                if (selectedClaim != null) {
                    claims.remove(selectedClaim);
                    auditLog.addEntry("Claim approved: " + selectedClaim);
                    updateClaimDisplay();
                    populateClaimsComboBox();
                    JOptionPane.showMessageDialog(null, "Claim approved!");
                }
            }
        }

        private class RejectClaimListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsuranceClaim selectedClaim = (InsuranceClaim) claimComboBox.getSelectedItem();
                if (selectedClaim != null) {
                    claims.remove(selectedClaim);
                    auditLog.addEntry("Claim rejected: " + selectedClaim);
                    updateClaimDisplay();
                    populateClaimsComboBox();
                    JOptionPane.showMessageDialog(null, "Claim rejected.");
                }
            }
        }
    }

    // Login functionality
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
