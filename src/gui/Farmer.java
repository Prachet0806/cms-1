package gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Farmer implements Serializable {
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
