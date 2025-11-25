package net.electricbudgie.datagen.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class NPCDialogConfig {
    private String name;
    private List<String> standardDialog;
    private List<String> startBattle;
    private List<String> loseBattle;
    private List<String> winBattle;
    private List<String> noPokemon;
    private List<String> playerTeamFainted;

    NPCDialogConfig(String name, List<String> standardDialog, List<String> startBattle, List<String> loseBattle, List<String> winBattle, List<String> noPokemon, List<String> playerTeamFainted){
        this.name = name;
        this.standardDialog = standardDialog;
        this.startBattle = startBattle;
        this.loseBattle = loseBattle;
        this.winBattle = winBattle;
        this.noPokemon = noPokemon;
        this.playerTeamFainted = playerTeamFainted;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStandardDialog() {
        return standardDialog;
    }

    public void setStandardDialog(List<String> standardDialog) {
        this.standardDialog = standardDialog;
    }

    public List<String> getStartBattle() {
        return startBattle;
    }

    public void setStartBattle(List<String> startBattle) {
        this.startBattle = startBattle;
    }

    public List<String> getLoseBattle() {
        return loseBattle;
    }

    public void setLoseBattle(List<String> loseBattle) {
        this.loseBattle = loseBattle;
    }

    public List<String> getWinBattle() {
        return winBattle;
    }

    public void setWinBattle(List<String> winBattle) {
        this.winBattle = winBattle;
    }

    public List<String> getNoPokemon() {
        return noPokemon;
    }

    public void setNoPokemon(List<String> noPokemon) {
        this.noPokemon = noPokemon;
    }

    public List<String> getPlayerTeamFainted() {
        return playerTeamFainted;
    }

    public void setPlayerTeamFainted(List<String> playerTeamFainted) {
        this.playerTeamFainted = playerTeamFainted;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static class Builder {
        private String name;
        private List<String> standardDialog = new ArrayList<>();
        private List<String> startBattle = new ArrayList<>(); ;
        private List<String> loseBattle =new ArrayList<>();
        private List<String> winBattle = new ArrayList<>();
        private List<String> noPokemon = new ArrayList<>();
        private List<String> playerTeamFainted = new ArrayList<>();

        public Builder setStandardDialog(List<String> standardDialog) {
            this.standardDialog = standardDialog;
            return this;
        }

        public Builder addStandardDialog(String dialog){
            this.standardDialog.add(dialog);
            return this;
        }

        public Builder setStartBattle(List<String> startBattle) {
            this.startBattle = startBattle;
            return this;
        }

        public Builder addStartBattle(String dialog){
            this.startBattle.add(dialog);
            return this;
        }

        public Builder setLoseBattle(List<String> loseBattle) {
            this.loseBattle = loseBattle;
            return this;
        }

        public Builder addLoseBattle(String dialog){
            this.loseBattle.add(dialog);
            return this;
        }

        public Builder setWinBattle(List<String> winBattle) {
            this.winBattle = winBattle;
            return this;
        }

        public Builder addWinBattle(String dialog){
            this.winBattle.add(dialog);
            return this;
        }

        public Builder setNoPokemon(List<String> noPokemon) {
            this.noPokemon = noPokemon;
            return this;
        }

        public Builder addNoPokemon(String dialog){
            this.noPokemon.add(dialog);
            return this;
        }

        public Builder setPlayerTeamFainted(List<String> playerTeamFainted) {
            this.playerTeamFainted = playerTeamFainted;
            return this;
        }

        public Builder addPlayerTeamFainted(String dialog){
            this.playerTeamFainted.add(dialog);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public NPCDialogConfig build(){
            return new NPCDialogConfig(this.name, this.standardDialog, this.startBattle, this.loseBattle,this.winBattle, this.noPokemon, this.playerTeamFainted);
        }
    }
}
