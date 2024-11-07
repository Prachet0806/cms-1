package gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Farmer implements Serializable {
    private static final long serialVersionUID = -8495115447310258104L;

    private String name;
    private List<Cattle> cattleList;

    public Farmer(String name) {
        this.name = name;
        this.cattleList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    public void removeCattle(Cattle cattle) {
        cattleList.remove(cattle);
    }

    public List<Cattle> getCattleList() {
        return cattleList;
    }

    @Override
    public String toString() {
        return name;
    }
}
