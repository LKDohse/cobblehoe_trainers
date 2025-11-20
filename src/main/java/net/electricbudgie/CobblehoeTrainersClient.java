package net.electricbudgie;

import net.electricbudgie.entity.ModEntities;
import net.electricbudgie.entity.client.ModEntityModelLayers;
import net.electricbudgie.entity.client.NPCRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class CobblehoeTrainersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleHoeTrainersNetwork.registerClientReceivers();

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.NPC, () -> TexturedModelData.of(PlayerEntityModel.getTexturedModelData(Dilation.NONE, false), 64, 64));
        EntityRendererRegistry.register(ModEntities.NPC, NPCRenderer::new);
    }
}
