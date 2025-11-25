package net.electricbudgie.datagen;

import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.datagen.configs.NPCDialogConfig;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

public class ModDialogProvider implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private final DataOutput output;

    public ModDialogProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.registryLookupFuture = registryLookupFuture;
    }

    private final NPCDialogConfig LASS =  new NPCDialogConfig.Builder()
            .setName("lass")
            .addStandardDialog("Fairy types are so cute, don't you think?")
            .addStandardDialog("Doesn't it suck how the patriarchy demonizes and belittles anything that can be attributed to the feminine?")
            .addStandardDialog("I love PIDGEY!")
            .addStandardDialog("BIDOOF is so cute!")
            .addStartBattle("Oh, I suppose I have some time to battle you.")
            .addLoseBattle("Oh, I guess I have some time to lose to you, too. ")
            .addWinBattle("Better luck next time!")
            .addNoPokemon("I'd love to fight you, but don't you think you should get a pokemon first?")
            .addPlayerTeamFainted("Oh no, your poor pokemon! You need to go get them healed at a Pokemon Center!")
            .build();

    private final NPCDialogConfig YOUNGSTER = new NPCDialogConfig.Builder()
            .setName("youngster")
            .addStandardDialog("Do you like shorts?")
            .addStartBattle("I like shorts! They're comfy and easy to wear!")
            .addLoseBattle("Oh no, my mom was right...")
            .addWinBattle("Haha! I won! My mom was wrong!")
            .addNoPokemon("Hey, you don't have any pokemon!")
            .addPlayerTeamFainted("Uh, you should probably go get your pokemon healed first.")
            .build();

    private final NPCDialogConfig HIKER = new NPCDialogConfig.Builder()
            .setName("hiker")
            .addStandardDialog("Nothing beats the great outdoors.")
            .addStartBattle("Hoo hah! I am mighty!")
            .addStartBattle("Nothing beats training in the great outdoors!")
            .addStartBattle("Feel the power of pokemon trained in the mountains!")
            .addLoseBattle("Never mind...")
            .addWinBattle("We are mighty!")
            .addNoPokemon("Pretty dangerous to be out in the wilderness with no pokemon!")
            .addPlayerTeamFainted("Your pokemon can't grow any stronger if they aren't fit to fight!")
            .build();

    public void generate(){
        writeDialogJson(Identifier.of(CobblehoeTrainers.MOD_ID, "lass"), LASS);
        writeDialogJson(Identifier.of(CobblehoeTrainers.MOD_ID, "youngster"), YOUNGSTER);
        writeDialogJson(Identifier.of(CobblehoeTrainers.MOD_ID, "hiker"), HIKER);
    }

    public void writeDialogJson(Identifier id, NPCDialogConfig npcSpawnConfig){
        var json = npcSpawnConfig.toJson();
        Path outputDir = output.getPath().getParent();
        Path trainerPath = outputDir
                .resolve("resources")
                .resolve("assets")
                .resolve(id.getNamespace())
                .resolve("npc")
                .resolve("dialog")
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
        return "NPC Dialog";
    }
}
