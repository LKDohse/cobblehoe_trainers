package net.electricbudgie.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;

public class TrainerEntity extends NPCEntity{
    public TrainerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }
}
