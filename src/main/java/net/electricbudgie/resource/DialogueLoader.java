package net.electricbudgie.resource;

import com.google.gson.JsonParser;
import net.electricbudgie.CobblehoeTrainers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class DialogueLoader {
    private static final ArrayList<String> oopsText = new ArrayList<>(Arrays.asList("Hello Trainer!"));

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
}
