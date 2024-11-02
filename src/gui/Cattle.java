package gui;

public class Cattle {
    private String name;
    private String breed;
    private int age;
    private String healthCondition;
    private boolean vaccinated;

    public Cattle(String name, String breed, int age, String healthCondition, boolean vaccinated) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.healthCondition = healthCondition;
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

    public String getHealthCondition() {
        return healthCondition;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }
}