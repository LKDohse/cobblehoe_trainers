package net.electricbudgie.entity.client;

import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.entity.custom.NPCEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NPCRenderer extends BipedEntityRenderer<NPCEntity, PlayerEntityModel<NPCEntity>> {

    public NPCRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public Identifier getTexture(NPCEntity entity) {
        return Identifier.of(CobblehoeTrainers.MOD_ID, "textures/entity/npc/"+ entity.getVariant().name().toLowerCase() + ".png");
    }

    @Override
    public void render(NPCEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
