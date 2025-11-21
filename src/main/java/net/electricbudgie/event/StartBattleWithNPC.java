package net.electricbudgie.event;

import net.electricbudgie.battle.BasicTrainerBattle;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface StartBattleWithNPC {
    Event<StartBattleWithNPC> EVENT = EventFactory.createArrayBacked(
            StartBattleWithNPC.class,
            (listeners) -> (battleData) -> {
                for (StartBattleWithNPC listener : listeners) {
                    listener.startBattle(battleData);
                }
            }
    );

    void startBattle(BasicTrainerBattle battleData);
}
