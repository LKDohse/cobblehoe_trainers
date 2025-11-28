package net.electricbudgie.datagen.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrainerConfig {
    public String name;
    public int defaultTeamSize;
    public List<SpeciesEntry> speciesList;

    public TrainerConfig(String name, int defaultTeamSize, List<SpeciesEntry> speciesList) {
        this.name = name;
        this.defaultTeamSize = defaultTeamSize;
        this.speciesList = speciesList;
    }

    public static class SpeciesEntry {
        private String name;

        @SerializedName("minimum_trainer_level_required")
        private int minimumTrainerLevel;

        private int weight = 5;

        private boolean skipEvolution = false;

        public SpeciesEntry(String name, int minimumTrainerLevel) {
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
        }

        public SpeciesEntry(String name, int minimumTrainerLevel, boolean skipEvolution) {
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
            this.skipEvolution = skipEvolution;
        }

        public SpeciesEntry(String name, int minimumTrainerLevel, int weight) {
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
            this.weight = weight;
        }

        public SpeciesEntry(String name, int minimumTrainerLevel, int weight, boolean skipEvolution) {
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
            this.weight = weight;
            this.skipEvolution = skipEvolution;
        }

        public String getName() {
            return name;
        }

        public int getMinimumTrainerLevel() {
            return minimumTrainerLevel;
        }

        public int getWeight() {
            return weight;
        }

        public boolean skipEvolution() {
            return skipEvolution;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * Helper builder for easy creation
     */
    public static class Builder {
        private int defaultTeamSize;
        private List<SpeciesEntry> speciesList = new ArrayList<>();
        private String name;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }


        public Builder setDefaultTeamSize(int size) {
            this.defaultTeamSize = size;
            return this;
        }

        public Builder addSpecies(String name, int weight) {
            this.speciesList.add(new SpeciesEntry(name, 1, weight));
            return this;
        }

        public Builder addSpecies(String name) {
            this.speciesList.add(new SpeciesEntry(name, 1));
            return this;
        }

        public Builder addSpecies(String name, boolean skipEvolution) {
            this.speciesList.add(new SpeciesEntry(name, 1, skipEvolution));
            return this;
        }

        public Builder addSpecies(String name, int weight, int minLevel) {
            this.speciesList.add(new SpeciesEntry(name, minLevel, weight));
            return this;
        }

        public TrainerConfig build() {
            return new TrainerConfig(name, defaultTeamSize, speciesList);
        }
    }
}
