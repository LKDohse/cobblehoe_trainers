package net.electricbudgie.event;

import net.electricbudgie.entity.custom.TrainerEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegisterNPCTrainer {
    Event<RegisterNPCTrainer> EVENT = EventFactory.createArrayBacked(
            RegisterNPCTrainer.class,
            (listeners) -> (TrainerEntity trainer) -> {
                for (RegisterNPCTrainer listener : listeners) {
                    listener.registerNPCTrainer(trainer);
                }
            }
    );

    void registerNPCTrainer(TrainerEntity trainer);
}
