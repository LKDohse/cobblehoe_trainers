package net.electricbudgie.event;

import net.electricbudgie.entity.custom.TrainerEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface NPCBattleCheck {
    Event<NPCBattleCheck> EVENT = EventFactory.createArrayBacked(
            NPCBattleCheck.class,
            (listeners) -> (PlayerEntity player, TrainerEntity trainer) -> {
                for (NPCBattleCheck listener : listeners) {
                    listener.getBattleCheck(player, trainer);
                }
            }
    );

    void getBattleCheck( PlayerEntity player, TrainerEntity trainer);
}
