package net.electricbudgie.entity.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.evolution.Evolution;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.evolution.variants.LevelUpEvolution;
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI;
import com.gitlab.srcmc.rctapi.api.battle.BattleFormat;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctapi.api.models.BagItemModel;
import com.gitlab.srcmc.rctapi.api.models.PokemonModel;
import com.gitlab.srcmc.rctapi.api.util.JTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.electricbudgie.battle.BasicTrainerBattle;
import net.electricbudgie.battle.PokemonModelConverter;
import net.electricbudgie.battle.TrainerBattleData;
import net.electricbudgie.datagen.configs.TrainerConfig;
import net.electricbudgie.event.NPCBattleCheck;
import net.electricbudgie.event.RegisterNPCTrainer;
import net.electricbudgie.event.StartBattleWithNPC;
import net.electricbudgie.resource.TeamLoader;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrainerEntity extends NPCEntity {
    protected TrainerBattleData battleData;
    protected PlayerEntity opponent;
    protected String trainerId;
    protected boolean initialSpawn = true;
    protected ArrayList<PokemonModel> team;
    protected ArrayList<BagItemModel> bag;
    protected int trainerLevel;

    public TrainerEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        var data = super.initialize(world, difficulty, spawnReason, entityData);
        this.trainerId = this.getUuidAsString().replaceAll("-", "");
        this.team = generateTeam();
        this.bag = getBag();
        this.setCustomName(Text.literal(this.displayName + ": Level " + this.trainerLevel));
        this.initialSpawn = false;
        return data;
    }

    public void registerTrainerData(){
        this.battleData = new TrainerBattleData(this.trainerId, this.displayName, BattleFormat.GEN_9_DOUBLES, new BattleRules(), JTO.of(RCTBattleAI::new), this.bag, this.team);
        RegisterNPCTrainer.EVENT.invoker().registerNPCTrainer(this);
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
        return trainerId;
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

    protected ArrayList<PokemonModel> generateTeam() {
        var teamOptions = TeamLoader.loadNPCTeamOptions(this.getVariant().name().toLowerCase());
        this.trainerLevel = calculateTrainerLevel();
        teamOptions.speciesList = teamOptions.speciesList.stream().filter(s -> s.getMinimumTrainerLevel() < trainerLevel).toList();

        return populateTeam(teamOptions, this.trainerLevel);
    }

    private ArrayList<PokemonModel> populateTeam(TrainerConfig config, int defaultLevel) {
        int defaultLevelOffset = Math.toIntExact(Math.round(defaultLevel*0.1));
        int speciesListWeightSum = config.speciesList.stream().map(TrainerConfig.SpeciesEntry::getWeight).reduce(0, Integer::sum);
        ArrayList<PokemonModel> team = new ArrayList<>();
        for(int i = 0; i < calculateTeamSize(config.defaultTeamSize); i++){
            int levelOffset = random.nextInt(defaultLevelOffset *2+1) - defaultLevelOffset;
            int level = Math.clamp(defaultLevel + levelOffset,1, 100);
            String species = getRandomWeightedSpecies(speciesListWeightSum, config.speciesList);
            PokemonProperties properties = new PokemonProperties();
            properties.setLevel(level);
            properties.setSpecies(species);
            properties.setSpecies(updateSpeciesIfCanEvolve(properties));
            team.add(PokemonModelConverter.getModel(properties.create()));
        }
        return team;
    }

    private String getRandomWeightedSpecies(int weightSum, List<TrainerConfig.SpeciesEntry> speciesEntryList){
            Map<TrainerConfig.SpeciesEntry, Integer> map = speciesEntryList.stream().collect(Collectors.toMap(
                    trainerConfig -> trainerConfig,
                    TrainerConfig.SpeciesEntry::getWeight
            ));

            var random = Math.random() * weightSum;
            for(Map.Entry<TrainerConfig.SpeciesEntry, Integer> entry: map.entrySet()){
                random -= entry.getValue();
                if (random <= 0) return entry.getKey().getName();
            }

            return "ditto";

    }

    private String updateSpeciesIfCanEvolve(PokemonProperties properties) {
        Pokemon pokemon = properties.create();
        String species = properties.getSpecies();
            for (Evolution evolution : pokemon.getEvolutions()) {
                if (evolution instanceof LevelUpEvolution) {
                    boolean canEvolve = evolution.test(pokemon);
                    if (canEvolve) {
                        properties.setSpecies(evolution.getResult().getSpecies());
                        return updateSpeciesIfCanEvolve(properties);
                    }
                }
            }
        return species;
    }

    private int calculateTeamSize(int defaultTeamSize) {
        int offset = random.nextInt(5) - 2;
        return Math.clamp(defaultTeamSize + offset, 1, 6);
    }

    protected Integer calculateTrainerLevel(){
        var spawn = this.getWorld().getSpawnPos() ;
        var pos = this.getBlockPos();
        var distanceVector = pos.subtract(spawn);
        double dx = distanceVector.getX();
        double dz = distanceVector.getZ();
        var distance = Math.sqrt(dx*dx+dz*dz);
        var level = Math.round(Math.clamp(1.5+Math.pow(Math.log10(0.12*distance), 3.75), 7, 100));
        return Math.toIntExact(level);
    }

    public void playerHasNoUseablePokemon(PlayerEntity player) {
        this.giveDialog(player, "Oh no, your poor Pokemon! You need to get them healed first.");
    }

    // NBT Data
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        Gson gson = new Gson();
        nbt.putBoolean("firstSpawn", this.initialSpawn);
        nbt.putString("trainerId", this.trainerId);
        nbt.putString("trainerName", this.displayName);
        nbt.putString("team", gson.toJson(this.team));
        nbt.putString("bag", gson.toJson(this.bag));
        nbt.putInt("trainerLevel", this.trainerLevel);
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        Gson gson = new Gson();
        this.initialSpawn = nbt.getBoolean("firstSpawn");
        this.trainerId = nbt.getString("trainerId");
        this.displayName = nbt.getString("trainerName");
        this.trainerLevel = nbt.getInt("trainerLevel");
        Type teamListType = new TypeToken<ArrayList<PokemonModel>>(){}.getType();
        Type bagListType = new TypeToken<ArrayList<BagItemModel>>(){}.getType();
        this.team = gson.fromJson(nbt.getString("team"), teamListType);
        this.bag = gson.fromJson(nbt.getString("bag"), bagListType);
    }
}
