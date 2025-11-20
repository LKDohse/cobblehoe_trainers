package net.electricbudgie.entity;

import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.entity.custom.NPCEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<NPCEntity> NPC = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(CobblehoeTrainers.MOD_ID, "npc"),
            EntityType.Builder.create(NPCEntity::new, SpawnGroup.CREATURE).dimensions(1f, 1.5f).build());

    public static void registerModEntities() {

    }
}
