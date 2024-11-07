package gui;

import java.util.ArrayList;
import java.util.List;

public class ManageClaimsController {
    private List<InsuranceClaim> claimsList;
    private List<InsurancePolicy> policies;
    private List<Farmer> farmers; // List to hold farmer information

    public ManageClaimsController() {
        this.claimsList = new ArrayList<>();
        this.policies = new ArrayList<>();
        this.farmers = new ArrayList<>();

        // Initialize sample policies
        policies.add(new InsurancePolicy("Basic Coverage", "Covers common illnesses and accidents."));
        policies.add(new InsurancePolicy("Comprehensive Coverage", "Includes major illnesses, accidents, and check-ups."));
        policies.add(new InsurancePolicy("High-Risk Coverage", "Specialized coverage for high-risk breeds or older cattle."));

        // Sample farmers and cattle
        Farmer farmer1 = new Farmer("phiroz");
        farmer1.addCattle(new Cattle("x", "x", 9, "Healthy", true));



        // Add farmers to the list
        farmers.add(farmer1);


        // Example claims for testing
        fileClaim(farmer1.getCattleList().get(0), farmer1, policies.get(0));

    }

    // Method to return the list of insurance policies
    public List<InsurancePolicy> getPolicies() {
        return policies;
    }

    // Method to get a list of all farmers
    public List<Farmer> getFarmers() {
        return farmers;
    }

    // Method to get cattle associated with a specific farmer
    public List<Cattle> getCattleForFarmer(Farmer farmer) {
        return farmer.getCattleList();
    }

    public void fileClaim(Cattle cattle, Farmer farmer, InsurancePolicy policy) {
        InsuranceClaim claim = new InsuranceClaim(cattle, farmer, policy);
        claimsList.add(claim);
        System.out.println("Claim filed: " + claim);
    }

    public double calculateInsuranceRate(Cattle cattle, InsurancePolicy policy) {
        return policy.calculateRate(cattle.getBreed(), cattle.getAge(), cattle.isVaccinated());
    }

    public void approveClaim(InsuranceClaim claim) {
        claim.approveClaim();
        System.out.println("Claim approved: " + claim);
    }

    public void rejectClaim(InsuranceClaim claim) {
        claim.rejectClaim();
        System.out.println("Claim rejected: " + claim);
    }

    public List<InsuranceClaim> getPendingClaims() {
        List<InsuranceClaim> pendingClaims = new ArrayList<>();
        for (InsuranceClaim claim : claimsList) {
            if (claim.getStatus().equals("Pending")) {
                pendingClaims.add(claim);
            }
        }
        return pendingClaims;
    }

    public void displayAllClaims() {
        for (InsuranceClaim claim : claimsList) {
            System.out.println(claim);
        }
    }
}
