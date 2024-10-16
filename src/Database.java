import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    public static ArrayList<Farmer> farmers = new ArrayList<>();
    public static ArrayList<InsuranceClaim> claims = new ArrayList<>();
    public static HashMap<String, ArrayList<Cattle>> cattleByFarmer = new HashMap<>();
    public static ArrayList<String> auditLogs = new ArrayList<>();

    public static void addFarmer(Farmer farmer) {
        farmers.add(farmer);
        cattleByFarmer.put(farmer.getUsername(), new ArrayList<>());
    }

    public static void addCattle(Farmer farmer, Cattle cattle) {
        cattleByFarmer.get(farmer.getUsername()).add(cattle);
        auditLogs.add("Cattle registered: " + cattle.getUID());
    }

    public static void addClaim(InsuranceClaim claim) {
        claims.add(claim);
        auditLogs.add("Claim filed: " + claim.getCattle().getUID());
    }

    public static ArrayList<String> getAuditLogs() {
        return auditLogs;
    }
}
