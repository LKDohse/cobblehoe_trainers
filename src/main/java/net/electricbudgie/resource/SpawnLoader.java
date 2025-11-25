package net.electricbudgie.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.electricbudgie.datagen.configs.NPCSpawnConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpawnLoader {
    private static final NPCSpawnConfig oopsHaveAYoungster = new NPCSpawnConfig.Builder().setName("youngster").build();
    private static final String spawnConfigPath = "npc/spawn";

    public String getVariant(String biomeId) {
        List<NPCSpawnConfig> configs = loadAllConfigs();

        for (NPCSpawnConfig config : configs) {
            System.out.println("Spawn Chance: " + config.spawnChance);
            System.out.println("Preferred Biomes: " + config.preferredBiomes);
        }

        var matchingTrainerTypes = configs.stream().filter(config -> config.preferredBiomes.stream().anyMatch(b -> b.equals(biomeId))).toList();
        if (matchingTrainerTypes.isEmpty()) return oopsHaveAYoungster.name;
        return pickFromAvailableNpcTypes(matchingTrainerTypes).name;
    }

    private NPCSpawnConfig pickFromAvailableNpcTypes(List<NPCSpawnConfig> configs){
        Map<NPCSpawnConfig, Integer> map = configs.stream().collect(Collectors.toMap(
                trainerConfig -> trainerConfig,
                trainerConfig -> trainerConfig.spawnChance
        ));

        var sum = map.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        var random = Math.random() * sum;
        for(Map.Entry<NPCSpawnConfig, Integer> entry: map.entrySet()){
            random -= entry.getValue();
            if (random <= 0) return entry.getKey();
        }

               return oopsHaveAYoungster;
    }

    private List<NPCSpawnConfig> loadAllConfigs() {
        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        List<NPCSpawnConfig> configs = new ArrayList<>();

        Map<Identifier, Resource> found = manager.findResources(
                spawnConfigPath,
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : found.entrySet()) {

            Identifier id = entry.getKey();
            Resource resource = entry.getValue();

            try (Reader reader = resource.getReader()) {
                NPCSpawnConfig config = GSON.fromJson(reader, NPCSpawnConfig.class);
                configs.add(config);

            } catch (Exception e) {
                System.err.println("Failed to load spawn config " + id);
                e.printStackTrace();
            }
        }

        return configs;
    }
}
