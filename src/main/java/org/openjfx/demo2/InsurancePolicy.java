package org.openjfx.demo2;

public class InsurancePolicy {

    private int premium = 1000;

    public InsurancePolicy(Cattle cattle) {
        if (cattle.getAge() <= 5) {
            premium *= 0.8;
        }
        if (cattle.isVaccinated()) {
            premium *= 0.8;
        }
    }

    public int getPremium() {
        return premium;
    }

}