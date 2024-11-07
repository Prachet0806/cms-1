package gui;

import java.io.Serializable;

public class Cattle implements Serializable {
    private static final long serialVersionUID = 1L;  // Added serialVersionUID for versioning of the class
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

    @Override
    public String toString() {
        return "Cattle{" +
                "name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", healthCondition='" + healthCondition + '\'' +
                ", vaccinated=" + vaccinated +
                '}';
    }
}
