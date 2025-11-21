package net.electricbudgie.battle;

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import net.electricbudgie.entity.custom.TrainerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class BasicTrainerBattle {
    private final PlayerEntity player;
    private final TrainerEntity trainerNPC;


    public BasicTrainerBattle(PlayerEntity player, TrainerEntity trainerNPC) {
        this.player = player;
        this.trainerNPC = trainerNPC;
    }

    public PlayerEntity getInitiator() {
    return player;
    }

    public void distributeRewards(boolean playerWon) {
        this.trainerNPC.finishBattle(this, playerWon);
    }

    public PlayerEntity getPlayer(){
        return this.player;
    }

    public TrainerEntity getTrainerNpc(){
        return this.trainerNPC;
    }

    public BattleRules getRules(){
        return new BattleRules();
    }

    public BattleFormat getFormat(){
        return BattleFormat.GEN_9_SINGLES;
    }
}
