package net.electricbudgie.entity.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import com.gitlab.srcmc.rctapi.api.battle.BattleFormat;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctapi.api.models.BagItemModel;
import com.gitlab.srcmc.rctapi.api.models.PokemonModel;
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC;
import com.gitlab.srcmc.rctapi.api.util.JTO;
import net.electricbudgie.battle.BasicTrainerBattle;
import net.electricbudgie.battle.PokemonModelConverter;
import net.electricbudgie.battle.TrainerBattleData;
import net.electricbudgie.event.StartBattleWithNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TrainerEntity extends NPCEntity {
    protected TrainerNPC trainer;
    protected TrainerBattleData battleData;
    protected PlayerEntity opponent;
    protected String cleanedId;


    public TrainerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
        String cleanedUuid = this.getUuidAsString().replaceAll("-","");
        cleanedId = cleanedUuid;
        battleData = new TrainerBattleData(cleanedUuid, getVariant().name(), BattleFormat.GEN_9_DOUBLES, new BattleRules(), JTO.of(RCTBattleAI::new), getBag(), getTeam());
        //trainer = new TrainerNPC("Guy", team, new TrainerBag(), new RandomBattleAI(), this);
    }

    public TrainerBattleData getBattleData(){
        return battleData;
    }

    public String getTrainerId(){
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

    public void startBattleWith(PlayerEntity player) {
        this.giveDialog(player, "Let's battle!");
        this.setOpponent(player);
        BasicTrainerBattle battle = new BasicTrainerBattle(player, this);
        StartBattleWithNPC.EVENT.invoker().startBattle(battle);
    }

    public void finishBattle(BasicTrainerBattle battle, boolean defeated) {
       this.setOpponent(null);
       if(defeated){
           this.giveDialog(battle.getInitiator(), "Aw man, you beat me!");
       }
       else
           this.giveDialog(battle.getInitiator(), "Haha, take that, nerd!");
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

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        startBattleWith(player);
        return ActionResult.SUCCESS;
    }
}
