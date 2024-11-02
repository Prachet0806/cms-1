package gui;

public class InsuranceClaim {
    private Cattle cattle;
    private Farmer farmer;
    private InsurancePolicy policy;
    private String status;

    public InsuranceClaim(Cattle cattle, Farmer farmer, InsurancePolicy policy) {
        this.cattle = cattle;
        this.farmer = farmer;
        this.policy = policy;
        this.status = "Pending"; // Default status
    }

    public String getDescription() {
        return "Claim for " + cattle.getBreed() + " owned by " + farmer.getName();
    }

    public InsurancePolicy getPolicy() {
        return policy;
    }

    public String getStatus() {
        return status;
    }

    public void approveClaim() {
        this.status = "Approved";
    }

    public void rejectClaim() {
        this.status = "Rejected";
    }

    @Override
    public String toString() {
        return getDescription() + " | Status: " + status + " | Policy: " + policy.getPolicyName();
    }
}
