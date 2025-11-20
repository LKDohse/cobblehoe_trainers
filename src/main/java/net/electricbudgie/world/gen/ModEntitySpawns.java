package net.electricbudgie.world.gen;

import net.electricbudgie.entity.ModEntities;
import net.electricbudgie.entity.custom.NPCEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.Heightmap;

public class ModEntitySpawns {
    public static void addSpawns(){
        BiomeModifications.addSpawn(BiomeSelectors.all(),
                SpawnGroup.CREATURE,
                ModEntities.NPC,
                50,
                2,
                5);

        SpawnRestriction.register(ModEntities.NPC, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NPCEntity::isValidNaturalSpawn);
    }
}
