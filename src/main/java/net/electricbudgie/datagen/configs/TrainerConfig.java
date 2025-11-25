package net.electricbudgie.datagen.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class TrainerConfig {
    public String name;
    public int spawnChance;
    public List<String> preferredBiomes;
    public int defaultTeamSize;
    public List<SpeciesEntry> speciesList;

    public TrainerConfig(String name, int spawnChance, List<String> preferredBiomes, int defaultTeamSize, List<SpeciesEntry> speciesList){
        this.name = name;
        this.spawnChance = spawnChance;
        this.preferredBiomes = preferredBiomes;
        this.defaultTeamSize = defaultTeamSize;
        this.speciesList = speciesList;
    }

    public static class SpeciesEntry {
        private String name;

        @SerializedName("minimum_trainer_level_required")
        private int minimumTrainerLevel;

        private int weight = 5;

        public SpeciesEntry(String name, int minimumTrainerLevel){
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
        }

        public SpeciesEntry(String name, int minimumTrainerLevel, int weight) {
            this.name = name;
            this.minimumTrainerLevel = minimumTrainerLevel;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public int getMinimumTrainerLevel() {
            return minimumTrainerLevel;
        }

        public int getWeight(){
            return weight;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /** Helper builder for easy creation */
    public static class Builder {
        private int spawnChance = 10;
        private List<String> preferredBiomes = new ArrayList<>();
        private int defaultTeamSize;
        private List<SpeciesEntry> speciesList = new ArrayList<>();
        private String name;

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setSpawnChance(int chance) {
            this.spawnChance = chance;
            return this;
        }

        public Builder addBiome(RegistryKey<Biome> biome) {
            this.preferredBiomes.add(biome.getValue().toString());
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

        public Builder addSpecies(String name, int weight, int minLevel){
            this.speciesList.add(new SpeciesEntry(name, minLevel, weight));
            return this;
        }

        public TrainerConfig build() {
            return new TrainerConfig(name, spawnChance, preferredBiomes, defaultTeamSize, speciesList);
        }
    }
}
