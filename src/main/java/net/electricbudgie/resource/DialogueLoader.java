package net.electricbudgie.resource;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.datagen.configs.NPCDialogConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class DialogueLoader {
    private static final ArrayList<String> oopsText = new ArrayList<>(Arrays.asList("Hello Trainer!"));
    private static final String defaultDialogPath = "npc/dialog/";

    public static ArrayList<String> loadNPCDialogue(String npcVariant) {
        Identifier path = Identifier.of(CobblehoeTrainers.MOD_ID, "npc/dialog/" + npcVariant + ".json");
        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        try {
            var resource = manager.getResource(path);
            if (resource.isEmpty()) return oopsText;
            var json = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8))
                    .getAsJsonArray();

            var result = new ArrayList<String>();
            json.forEach((item) -> result.add(item.getAsString()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return oopsText;
        }
    }

    public static NPCDialogConfig loadDialog(String npcVariant) {
        final NPCDialogConfig oopsConfig = new NPCDialogConfig.Builder()
                .addStandardDialog("Hi Trainer!")
                .addLoseBattle("Aw man!")
                .addNoPokemon("You have no pokemon!")
                .addPlayerTeamFainted("Your poor pokemon! Get them to a pokemon center!")
                .addWinBattle("Haha, take that, nerd!")
                .build();

        Identifier path = Identifier.of(CobblehoeTrainers.MOD_ID, defaultDialogPath + npcVariant + ".json");
        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        try {
            var resource = manager.getResource(path);
            if (resource.isEmpty()) return oopsConfig;
            var json = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8))
                    .getAsJsonObject();

            Gson gson = new Gson();
            return gson.fromJson(json, NPCDialogConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return oopsConfig;
        }
    }
}
