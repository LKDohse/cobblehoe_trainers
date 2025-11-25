package net.electricbudgie.datagen.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class NPCSpawnConfig {
    public String name;
    public int spawnChance;
    public List<String> preferredBiomes;


    public NPCSpawnConfig(String name, int spawnChance, List<String> preferredBiomes){
        this.name = name;
        this.spawnChance = spawnChance;
        this.preferredBiomes = preferredBiomes;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /** Helper builder for easy creation */
    public static class Builder {
        private int spawnChance;
        private List<String> preferredBiomes = new ArrayList<>();
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

        public NPCSpawnConfig build() {
            return new NPCSpawnConfig(name, spawnChance, preferredBiomes);
        }
    }
}
