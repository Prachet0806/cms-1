package org.openjfx.demo2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private List<String> logEntries = new ArrayList<>();


    public void addEntry(String actionDescription) {
        String entry = LocalDateTime.now() + ": " + actionDescription;
        logEntries.add(entry);
        saveToFile(entry);
    }


    public List<String> getEntries() {
        List<String> allEntries = new ArrayList<>(logEntries);


        try (BufferedReader reader = new BufferedReader(new FileReader("audit_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allEntries.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error retrieving audit log entries: " + e.getMessage());
        }

        return allEntries;
    }


    private void saveToFile(String entry) {
        try (PrintWriter out = new PrintWriter(new FileWriter("audit_log.txt", true))) {
            out.println(entry);
        } catch (IOException e) {
            System.out.println("Error saving audit log entry: " + e.getMessage());
        }
    }
}