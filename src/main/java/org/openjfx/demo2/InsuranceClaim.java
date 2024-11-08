package org.openjfx.demo2;

public class InsuranceClaim {
    private String cattleName;
    private int cattleAge;
    private String cattleBreed;
    private String reason;
    private String status;

    public InsuranceClaim(String cattleName, int cattleAge, String cattleBreed, String reason) {
        this.cattleName = cattleName;
        this.cattleAge = cattleAge;
        this.cattleBreed = cattleBreed;
        this.reason = reason;
        this.status = "Pending";
    }


    public String getCattleName() {
        return cattleName;
    }

    public int getCattleAge() {
        return cattleAge;
    }

    public String getCattleBreed() {
        return cattleBreed;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Claim for " + cattleName + " (Age: " + cattleAge + ", Breed: " + cattleBreed + ") - Reason: " + reason + " - Status: " + status;
    }

    public String toFileString() {
        return cattleName + "," + cattleAge + "," + cattleBreed + "," + reason + "," + status;
    }
}