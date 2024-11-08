package org.openjfx.demo2;

public class Cattle {
    private String breed,name;
    private int age;
    private double weight;
    private boolean vaccinated;
    private int premium;

    public Cattle(String name, String breed, int age, double weight, boolean vaccinated) {
        this.name=name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccinated = vaccinated;
    }

    public String getName() {
        return name;
    }
    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public int getPremium() {
        return premium;
    }
    public void setPremium(int premium) {
        this.premium = premium;
    }
}
