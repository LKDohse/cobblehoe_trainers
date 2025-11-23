package net.electricbudgie.resource;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.datagen.configs.TrainerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TeamLoader {
    private static final TrainerConfig oopsHaveADitto = new TrainerConfig.Builder().setName("youngster").addSpecies("ditto").build();

    public static TrainerConfig loadNPCTeamOptions(String npcVariant) {
        Identifier path = Identifier.of(CobblehoeTrainers.MOD_ID, "npc/teams/" + npcVariant + ".json");
        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        try {
            var resource = manager.getResource(path);
            if (resource.isEmpty()) return oopsHaveADitto;
            var json = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8))
                    .getAsJsonObject();

            Gson gson = new Gson();
            return gson.fromJson(json, TrainerConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return oopsHaveADitto;
        }
    }

}
