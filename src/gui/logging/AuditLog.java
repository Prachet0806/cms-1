package logging;

import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private List<String> logEntries;

    public AuditLog() {
        logEntries = new ArrayList<>();
    }

    public void addEntry(String actionDescription) {
        String entry = "Timestamp: " + System.currentTimeMillis() + " - Action: " + actionDescription;
        logEntries.add(entry);
    }

    public List<String> getEntries() {
        return logEntries;
    }
}
