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

    // Refined method to calculate insurance rate based on breed, age, and vaccination status
    public double calculateRate(String breed, int age, boolean vaccinated) {
        double baseRate = 150.0; // Updated base rate for more realism

        // Adjust breed multiplier to add more granularity
        double breedMultiplier = 1.0;
        if (breed.equalsIgnoreCase("premium")) {
            breedMultiplier = 1.6; // Higher rate for premium breeds
        } else if (breed.equalsIgnoreCase("standard")) {
            breedMultiplier = 1.2; // Moderate rate for standard breeds
        } else {
            breedMultiplier = 1.0; // Base multiplier for common breeds
        }

        // More realistic age multiplier
        double ageMultiplier;
        if (age < 1) {
            ageMultiplier = 1.4; // Higher rate for calves
        } else if (age <= 5) {
            ageMultiplier = 1.0; // Standard rate for prime age cattle
        } else {
            ageMultiplier = 0.9; // Discounted rate for older cattle
        }

        // Vaccine discount and risk factor
        double vaccineDiscount = vaccinated ? 0.85 : 1.2; // 15% discount for vaccinated, 20% increase if not

        // Calculate the final rate with all factors
        return baseRate * breedMultiplier * ageMultiplier * vaccineDiscount;
    }

    @Override
    public String toString() {
        return policyName + " - " + description;
    }
}
