package net.electricbudgie.datagen;

import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.datagen.configs.NPCSpawnConfig;
import net.electricbudgie.datagen.configs.TrainerConfig;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

public class ModNPCProvider implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private final DataOutput output;

    public ModNPCProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.registryLookupFuture = registryLookupFuture;
    }

    private final NPCSpawnConfig LASS =  new NPCSpawnConfig.Builder()
            .setName("lass")
            .setSpawnChance(8)
            .addBiome(BiomeKeys.PLAINS)
            .addBiome(BiomeKeys.BEACH)
            .addBiome(BiomeKeys.SUNFLOWER_PLAINS)
            .addBiome(BiomeKeys.FOREST)
            .addBiome(BiomeKeys.FLOWER_FOREST)
            .addBiome(BiomeKeys.BIRCH_FOREST)
            .addBiome(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
            .addBiome(BiomeKeys.WINDSWEPT_FOREST)
            .addBiome(BiomeKeys.SAVANNA)
            .build();

    private final NPCSpawnConfig YOUNGSTER = new NPCSpawnConfig.Builder()
            .setName("youngster")
            .setSpawnChance(8)
            .addBiome(BiomeKeys.PLAINS)
            .addBiome(BiomeKeys.BEACH)
            .addBiome(BiomeKeys.SUNFLOWER_PLAINS)
            .addBiome(BiomeKeys.FOREST)
            .addBiome(BiomeKeys.FLOWER_FOREST)
            .addBiome(BiomeKeys.BIRCH_FOREST)
            .addBiome(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
            .addBiome(BiomeKeys.WINDSWEPT_FOREST)
            .addBiome(BiomeKeys.SAVANNA)
            .build();

    private final NPCSpawnConfig HIKER = new NPCSpawnConfig.Builder()
            .setName("hiker")
            .setSpawnChance(7)
            .addBiome(BiomeKeys.WINDSWEPT_FOREST)
            .addBiome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS)
            .addBiome(BiomeKeys.JAGGED_PEAKS)
            .addBiome(BiomeKeys.STONY_PEAKS)
            .addBiome(BiomeKeys.WINDSWEPT_HILLS)
            .addBiome(BiomeKeys.WINDSWEPT_SAVANNA)
            .addBiome(BiomeKeys.DRIPSTONE_CAVES)
            .addBiome(BiomeKeys.GROVE)
            .addBiome(BiomeKeys.SNOWY_SLOPES)
            .build();

    private final NPCSpawnConfig CURLER = new NPCSpawnConfig.Builder()
            .setName("curler")
            .setSpawnChance(5)
            .addBiome(BiomeKeys.FROZEN_RIVER)
            .addBiome(BiomeKeys.FROZEN_OCEAN)
            .addBiome(BiomeKeys.SNOWY_BEACH)
            .addBiome(BiomeKeys.ICE_SPIKES)
            .addBiome(BiomeKeys.SNOWY_PLAINS)
            .build();

    private final NPCSpawnConfig ESPORTSCOACH = new NPCSpawnConfig.Builder()
            .setName("esportscoach")
            .setSpawnChance(5)
            .addBiome(BiomeKeys.DESERT)
            .build();

    public void generate(){
        writeSpawnJson(Identifier.of(CobblehoeTrainers.MOD_ID, "lass"), LASS);
        writeSpawnJson(Identifier.of(CobblehoeTrainers.MOD_ID, "youngster"), YOUNGSTER);
        writeSpawnJson(Identifier.of(CobblehoeTrainers.MOD_ID, "hiker"), HIKER);
        writeSpawnJson(Identifier.of(CobblehoeTrainers.MOD_ID, "curler"), CURLER);
        writeSpawnJson(Identifier.of(CobblehoeTrainers.MOD_ID, "esportscoach"), ESPORTSCOACH);
    }

    public void writeSpawnJson(Identifier id, NPCSpawnConfig npcSpawnConfig){
        var json = npcSpawnConfig.toJson();
        Path outputDir = output.getPath().getParent();
        Path trainerPath = outputDir
                .resolve("resources")
                .resolve("assets")
                .resolve(id.getNamespace())
                .resolve("npc")
                .resolve("spawn")
                .resolve(id.getPath() + ".json");

        var outputPath = trainerPath.getParent();
        try {
            Files.createDirectories(outputPath);
            Files.writeString(trainerPath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch(Exception ex){

        }
    }

    public final CompletableFuture<?> run(DataWriter writer) {
        generate();
        return this.registryLookupFuture;
    }

    @Override
    public String getName() {
        return "NPCs";
    }
}
