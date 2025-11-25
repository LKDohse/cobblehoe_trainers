package net.electricbudgie.entity.custom;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.electricbudgie.CobblehoeTrainers;
import net.electricbudgie.entity.variant.NPCVariant;
import net.electricbudgie.networking.DialoguePayload;
import net.electricbudgie.resource.DialogueLoader;
import net.electricbudgie.resource.SpawnLoader;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class NPCEntity extends PassiveEntity {
    protected static final TrackedData<Integer> DATA_TYPE_VARIANT =
            DataTracker.registerData(NPCEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected String variant;
    protected String displayName;
    protected WanderAroundGoal wanderGoal;

    public NPCEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends NPCEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    @Override
    protected void initGoals() {
        this.wanderGoal = new WanderAroundGoal(this, 1.0);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0));
        this.goalSelector.add(2, wanderGoal);
        this.goalSelector.add(3, new LookAtEntityGoal(this, PokemonEntity.class, 6.0F));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        giveDialog(player, getDialogText());
        return ActionResult.SUCCESS;
    }

    protected void giveDialog(PlayerEntity player, String dialogText){
        if (player.getWorld().isClient) return;
        ServerPlayNetworking.send((ServerPlayerEntity)player, new DialoguePayload(dialogText));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isReadyToBreed() {
        return false;
    }

    protected String getDialogText(){
        var variant = getVariant();
        var array = DialogueLoader.loadNPCDialogue(variant.name().toLowerCase());
        return Util.getRandom(array, this.random);
    }

    // VARIANT LOGIC


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_TYPE_VARIANT, 0);
    }

    public int getTypeVariant(){return this.dataTracker.get(DATA_TYPE_VARIANT);}

    public NPCVariant getVariant() {
        return NPCVariant.byId(this.getTypeVariant() & 255); // 255 is acting as a bitwise AND operation to make sure the integer returns correctly
    }

    public void setVariant(String variant){
        this.variant = variant;
        this.dataTracker.set(DATA_TYPE_VARIANT, NPCVariant.valueOf(variant.toUpperCase()).getId());
        this.displayName = this.variant.toUpperCase();
    }

    private String loadRandomVariant(){
        RegistryEntry<Biome> biomeEntry = this.getWorld()
                .getBiome(this.getBlockPos());

        Identifier biomeId = this.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biomeEntry.value());

        String biomeString = biomeId.toString();
        return new SpawnLoader().getVariant(biomeString);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        CobblehoeTrainers.LOGGER.info("initializing entity Trainer because " + spawnReason.name());
        if (!world.isClient()) {
            String variant = loadRandomVariant();
            setVariant(variant);
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_TYPE_VARIANT, nbt.getInt("variant_type"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("variant_type", this.getTypeVariant());
    }

    @Override
    public boolean isBaby() {
       return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

}
