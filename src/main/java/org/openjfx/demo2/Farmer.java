package org.openjfx.demo2;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Farmer {
    private String name;
    private ObservableList<Cattle> cattleList;
    private List<InsuranceClaim> claimsList;

    public Farmer(String name) {
        this.name = name;
        this.cattleList = FXCollections.observableArrayList();
        this.claimsList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    public ObservableList<Cattle> getCattleList() {
        return cattleList;
    }

    public void removeCattle(Cattle cattle) {
        cattleList.remove(cattle);
    }
    public void addClaim(InsuranceClaim claim) {
        claimsList.add(claim);
    }
    public List<InsuranceClaim> getClaimsList() {
        return claimsList;
    }
}