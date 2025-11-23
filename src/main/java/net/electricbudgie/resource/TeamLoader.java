package net.electricbudgie.resource;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.electricbudgie.CobblehoeTrainers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TeamLoader {
    private static final teamOptions oopsHaveADitto = new teamOptions(List.of("ditto"), 1);

    public static teamOptions loadNPCTeamOptions(String npcVariant) {
        Identifier path = Identifier.of(CobblehoeTrainers.MOD_ID, "teams/" + npcVariant + ".json");
        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        try {
            var resource = manager.getResource(path);
            if (resource.isEmpty()) return oopsHaveADitto;
            var json = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8))
                    .getAsJsonObject();

            Gson gson = new Gson();
            return gson.fromJson(json, teamOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
            return oopsHaveADitto;
        }
    }

    public record teamOptions(List<String> speciesList, int defaultTeamSize){}
}
