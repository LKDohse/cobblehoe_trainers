package net.electricbudgie.entity.client;

import net.electricbudgie.CobblehoeTrainers;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {
    public static final EntityModelLayer NPC =
            new EntityModelLayer(Identifier.of(CobblehoeTrainers.MOD_ID, "npc"), "main");

}
