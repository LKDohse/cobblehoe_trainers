package net.electricbudgie.entity.client;

import com.google.common.collect.Maps;
import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.entity.custom.NPCEntity;
import net.electricbudgie.entity.variant.NPCVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class NPCRenderer extends BipedEntityRenderer<NPCEntity, PlayerEntityModel<NPCEntity>> {
    private static final Map<NPCVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(NPCVariant.class), map -> {
                map.put(NPCVariant.LASS, Identifier.of(CobblehoeTrainers.MOD_ID, "textures/entity/npc/lass.png"));
                map.put(NPCVariant.YOUNGSTER, Identifier.of(CobblehoeTrainers.MOD_ID, "textures/entity/npc/youngster.png"));
            });

    public NPCRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }



    @Override
    public Identifier getTexture(NPCEntity entity) {
        return LOCATION_BY_VARIANT.get(entity.getVariant());
    }

    @Override
    public void render(NPCEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
