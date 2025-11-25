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
            .addSpecies("pidgey",6)
            .addSpecies("spearow")
            .addSpecies("bidoof",7)
            .addSpecies("jigglypuff",6)
            .addSpecies("clefairy",6)
            .addSpecies("clefable", 5, 25)
            .addSpecies("wigglytuff", 5, 25)
            .addSpecies("nidoranf",6)
            .addSpecies("nidoqueen", 6, 40)
            .addSpecies("oddish", 4)
            .addSpecies("vileplume", 3, 35)
            .addSpecies("bellsprout", 4)
            .addSpecies("victreebel", 3, 35)
            .addSpecies("meowth",6)
            .addSpecies("paras")
            .addSpecies("psyduck")
            .addSpecies("mareep")
            .addSpecies("hoppip")
            .addSpecies("sunkern")
            .addSpecies("marill",6)
            .addSpecies("lotad")
            .addSpecies("luvdisc")
            .addSpecies("shroomish")
            .addSpecies("skitty")
            .addSpecies("pachirisu")
            .addSpecies("cherubi")
            .addSpecies("budew")
            .addSpecies("roselia", 5,15)
            .addSpecies("roserade", 5, 30)
            .addSpecies("whimsicott", 5, 30)
            .addSpecies("deerling")
            .addSpecies("buneary",6)
            .addSpecies("purrloin")
            .addSpecies("gothita",4)
            .addSpecies("bunnelby")
            .addSpecies("dendenne",6)
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
            .addSpecies("rattata",8)
            .addSpecies("poochyena",6)
            .addSpecies("spearow")
            .addSpecies("zigzagoon",6)
            .addSpecies("starly")
            .addSpecies("caterpie",3)
            .addSpecies("weedle",3)
            .addSpecies("spinerak",4)
            .addSpecies("ladyba",3)
            .addSpecies("nidoranm", 6)
            .addSpecies("nidoking", 6, 45)
            .addSpecies("slowpoke",3)
            .addSpecies("ekans",4)
            .addSpecies("sandshrew")
            .addSpecies("mankey")
            .addSpecies("zubat")
            .addSpecies("crobat", 3,35)
            .addSpecies("growlith",3)
            .addSpecies("seedot",4)
            .addSpecies("shiftry", 5, 25)
            .addSpecies("tranpinch",2)
            .addSpecies("electrike",3)
            .addSpecies("kricketot",3)
            .addSpecies("aipom",3)
            .addSpecies("buizel",3)
            .addSpecies("patrat",6)
            .addSpecies("lillipup")
            .addSpecies("yanma",3)
            .addSpecies("venonat",4)
            .addSpecies("pidove",4)
            .addSpecies("emolga",3)
            .build();

    private final TrainerConfig HIKER = new TrainerConfig.Builder()
            .setName("hiker")
            .setDefaultTeamSize(3)
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
            .addSpecies("geodude", 7)
            .addSpecies("golem", 4, 35)
            .addSpecies("onix", 6)
            .addSpecies("steelix", 4, 35)
            .addSpecies("zubat", 6)
            .addSpecies("diglett", 6)
            .addSpecies("machop")
            .addSpecies("machamp", 3, 40)
            .addSpecies("nosepass",6)
            .addSpecies("sandshrew")
            .addSpecies("numel",3)
            .addSpecies("roggenrola",4)
            .addSpecies("gigalith", 4,35)
            .addSpecies("aron",3)
            .addSpecies("drilbur",4)
            .addSpecies("timburr",3)
            .addSpecies("conkeldurr", 3, 40)
            .addSpecies("gligar")
            .addSpecies("swinub",4)
            .build();


    public void generate(){
        writeTrainerJson(Identifier.of(CobblehoeTrainers.MOD_ID, "lass"), LASS);
        writeTrainerJson(Identifier.of(CobblehoeTrainers.MOD_ID, "youngster"), YOUNGSTER);
        writeTrainerJson(Identifier.of(CobblehoeTrainers.MOD_ID, "hiker"), HIKER);
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
