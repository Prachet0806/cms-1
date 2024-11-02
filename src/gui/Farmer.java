package gui;

import java.util.ArrayList;
import java.util.List;

public class Farmer {
    private String name;
    private List<Cattle> cattleList;

    public Farmer(String name) {
        this.name = name;
        this.cattleList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    public List<Cattle> getCattleList() {
        return cattleList;
    }
}