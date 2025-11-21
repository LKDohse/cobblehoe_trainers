package net.electricbudgie.entity.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import com.gitlab.srcmc.rctapi.api.battle.BattleFormat;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctapi.api.models.BagItemModel;
import com.gitlab.srcmc.rctapi.api.models.PokemonModel;
import com.gitlab.srcmc.rctapi.api.util.JTO;
import net.electricbudgie.battle.BasicTrainerBattle;
import net.electricbudgie.battle.PokemonModelConverter;
import net.electricbudgie.battle.TrainerBattleData;
import net.electricbudgie.event.NPCBattleCheck;
import net.electricbudgie.event.RegisterNPCTrainer;
import net.electricbudgie.event.StartBattleWithNPC;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TrainerEntity extends NPCEntity {
    protected TrainerBattleData battleData;
    protected PlayerEntity opponent;
    protected String cleanedId;

    public TrainerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);

    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        var data = super.initialize(world, difficulty, spawnReason, entityData);
        String cleanedUuid = this.getUuidAsString().replaceAll("-", "");
        cleanedId = cleanedUuid;
        battleData = new TrainerBattleData(cleanedUuid, gameName, BattleFormat.GEN_9_DOUBLES, new BattleRules(), JTO.of(RCTBattleAI::new), getBag(), getTeam());
        RegisterNPCTrainer.EVENT.invoker().registerNPCTrainer(this);
        return data;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        checkCanBattle(player);
        return ActionResult.SUCCESS;
    }

    public void checkCanBattle(PlayerEntity player) {
        if (isInBattle()) return;
        NPCBattleCheck.EVENT.invoker().getBattleCheck(player, this);
    }

    public void startBattleWith(PlayerEntity player) {
        this.giveDialog(player, "Let's battle!");
        this.goalSelector.remove(this.wanderGoal);
        this.setOpponent(player);
        BasicTrainerBattle battle = new BasicTrainerBattle(player, this);
        StartBattleWithNPC.EVENT.invoker().startBattle(battle);
    }

    public TrainerBattleData getBattleData() {
        return battleData;
    }

    public String getTrainerId() {
        return cleanedId;
    }

    protected void setOpponent(PlayerEntity player) {
        this.opponent = player;
    }

    public PlayerEntity getOpponent() {
        return this.opponent;
    }

    public boolean isInBattle() {
        return this.opponent != null;
    }

    public void playerHasNoPokemon(PlayerEntity player) {
        this.giveDialog(player, "You don't have any pokemon!");
    }

    public void finishBattle(BasicTrainerBattle battle, boolean defeated) {
        this.setOpponent(null);
        if (defeated) {
            this.giveDialog(battle.getInitiator(), "Aw man, you beat me!");
        } else
            this.giveDialog(battle.getInitiator(), "Haha, take that, nerd!");
        this.goalSelector.add(5, this.wanderGoal);
    }

    private ArrayList<BagItemModel> getBag() {
        ArrayList<BagItemModel> bag = new ArrayList<BagItemModel>();
        BagItemModel item = new BagItemModel("oran_berry", 1);
        bag.add(item);
        return bag;
    }

    private ArrayList<PokemonModel> getTeam() {
        ArrayList<PokemonModel> team = new ArrayList<>();
        PokemonProperties properties = new PokemonProperties() {
        };
        properties.setSpecies("rattata");
        properties.setLevel(10);
        Pokemon rattata = properties.create();
        team.add(PokemonModelConverter.getModel(rattata));
        return team;
    }

    public void playerHasNoUseablePokemon(PlayerEntity player) {
        this.giveDialog(player, "Oh no, your poor Pokemon! You need to get them healed first.");
    }
}
