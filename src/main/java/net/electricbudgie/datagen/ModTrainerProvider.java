package net.electricbudgie.datagen;

import net.electricbudgie.CobblehoeTrainers;
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

public class ModTrainerProvider implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private final DataOutput output;

    public ModTrainerProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.registryLookupFuture = registryLookupFuture;
    }

    private final TrainerConfig LASS =  new TrainerConfig.Builder()
            .setName("lass")
            .setDefaultTeamSize(2)
            .setSpawnChance(8)
            .addBiome(BiomeKeys.PLAINS)
            .addBiome(BiomeKeys.BEACH)
            .addBiome(BiomeKeys.DESERT)
            .addBiome(BiomeKeys.SUNFLOWER_PLAINS)
            .addBiome(BiomeKeys.FOREST)
            .addBiome(BiomeKeys.FLOWER_FOREST)
            .addBiome(BiomeKeys.BIRCH_FOREST)
            .addBiome(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
            .addBiome(BiomeKeys.WINDSWEPT_FOREST)
            .addBiome(BiomeKeys.SAVANNA)
            .addSpecies("pidgey")
            .addSpecies("spearow")
            .addSpecies("bidoof")
            .addSpecies("jigglypuff")
            .addSpecies("clefairy")
            .addSpecies("clefable", 25)
            .addSpecies("wigglytuff", 25)
            .build();

    private final TrainerConfig YOUNGSTER = new TrainerConfig.Builder()
            .setName("youngster")
            .setDefaultTeamSize(2)
            .setSpawnChance(8)
            .addBiome(BiomeKeys.PLAINS)
            .addBiome(BiomeKeys.BEACH)
            .addBiome(BiomeKeys.DESERT)
            .addBiome(BiomeKeys.SUNFLOWER_PLAINS)
            .addBiome(BiomeKeys.FOREST)
            .addBiome(BiomeKeys.FLOWER_FOREST)
            .addBiome(BiomeKeys.BIRCH_FOREST)
            .addBiome(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
            .addBiome(BiomeKeys.WINDSWEPT_FOREST)
            .addBiome(BiomeKeys.SAVANNA)
            .addSpecies("rattata")
            .addSpecies("poochyena")
            .addSpecies("spearow")
            .addSpecies("zigzagoon")
            .addSpecies("starly")
            .addSpecies("caterpie")
            .addSpecies("weedle")
            .addSpecies("spinerak")
            .addSpecies("ladyba")
            .build();

    public void generate(){
        writeTrainerJson(Identifier.of(CobblehoeTrainers.MOD_ID, "lass"), LASS);
        writeTrainerJson(Identifier.of(CobblehoeTrainers.MOD_ID, "youngster"), YOUNGSTER);
    }

    public void writeTrainerJson(Identifier id, TrainerConfig trainerConfig){
        var json = trainerConfig.toJson();
        Path outputDir = output.getPath().getParent();
        Path trainerPath = outputDir
                .resolve("resources")
                .resolve("assets")
                .resolve(id.getNamespace())
                .resolve("npc")
                .resolve("teams")
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
        return "Trainers";
    }
}
