package gui;

public class InsurancePolicy {
    private String policyName;
    private String description;

    public InsurancePolicy(String policyName, String description) {
        this.policyName = policyName;
        this.description = description;
    }

    public String getPolicyName() {
        return policyName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return policyName; 
    }
}