package net.electricbudgie;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.RCTApi;
import com.gitlab.srcmc.rctapi.api.battle.BattleState;
import com.gitlab.srcmc.rctapi.api.events.EventListener;
import com.gitlab.srcmc.rctapi.api.events.Events;
import com.gitlab.srcmc.rctapi.api.trainer.Trainer;
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC;
import com.google.gson.Gson;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.electricbudgie.battle.BasicTrainerBattle;
import net.electricbudgie.entity.ModEntities;
import net.electricbudgie.entity.custom.NPCEntity;
import net.electricbudgie.entity.custom.TrainerEntity;
import net.electricbudgie.event.NPCBattleCheck;
import net.electricbudgie.event.RegisterNPCTrainer;
import net.electricbudgie.event.StartBattleWithNPC;
import net.electricbudgie.networking.DialoguePayload;
import net.electricbudgie.world.gen.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CobblehoeTrainers implements ModInitializer {
    public static final String MOD_ID = "cobblehoetrainers";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Our own instance of the service.
    public static final RCTApi RCT = RCTApi.initInstance(MOD_ID);

    // Using a gson builder provided by the api is necessary.
    private static final Gson GSON = RCT.gsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();


    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");
        PayloadTypeRegistry.playS2C().register(DialoguePayload.ID, DialoguePayload.CODEC);
        ModEntities.registerModEntities();
        ModEntitySpawns.addSpawns();

        FabricDefaultAttributeRegistry.register(ModEntities.NPC, NPCEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.TRAINER, NPCEntity.createAttributes());

        registerEvents();
    }

    static void registerEvents() {
        LifecycleEvent.SERVER_STARTING.register(CobblehoeTrainers::onServerStarting);

        PlayerEvent.PLAYER_JOIN.register(CobblehoeTrainers::onPlayerJoin);
        PlayerEvent.PLAYER_QUIT.register(CobblehoeTrainers::onPlayerQuit);
        RegisterNPCTrainer.EVENT.register(CobblehoeTrainers::registerSpawnedTrainers);
        ServerEntityEvents.ENTITY_UNLOAD.register(CobblehoeTrainers::unregisterSpawnedTrainers);
        NPCBattleCheck.EVENT.register(CobblehoeTrainers::getBattleCheck);
        StartBattleWithNPC.EVENT.register(CobblehoeTrainers::startBattle);
    }

    static void onServerStarting(MinecraftServer server) {
        var trainerRegistry = RCT.getTrainerRegistry();
        trainerRegistry.init(server);
    }

    static void onPlayerJoin(ServerPlayerEntity player) {
        RCT.getTrainerRegistry().registerPlayer(player.getName().getString(), player);
    }

    static void onPlayerQuit(PlayerEntity player) {
        RCT.getTrainerRegistry().unregisterById(player.getName().getString());
    }

    static void registerSpawnedTrainers(Entity entity) {
        if (!(entity instanceof TrainerEntity trainer)) return;
        RCT.getTrainerRegistry().registerNPC(trainer.getTrainerId(), trainer.getBattleData());
        RCT.getTrainerRegistry().getById(trainer.getTrainerId(), TrainerNPC.class).setEntity(trainer);
    }

    static void unregisterSpawnedTrainers(Entity entity, World world) {
        if (!(entity instanceof TrainerEntity trainer)) return;
        RCT.getTrainerRegistry().unregisterById(trainer.getTrainerId());
    }

    static void getBattleCheck(PlayerEntity player, TrainerEntity trainer){
        var playerTrainer = CobblehoeTrainers.RCT.getTrainerRegistry().getById(player.getName().getString());
        if (playerTrainer.getTeam().length < 1) {
            trainer.playerHasNoPokemon(player);
            return;
        }
        if (Arrays.stream(playerTrainer.getTeam()).allMatch(Pokemon::isFainted)) {
            trainer.playerHasNoUseablePokemon(player);
            return;
        }
        trainer.startBattleWith(player);
    }

    static void startBattle(BasicTrainerBattle battleData) {
         var player = RCT.getTrainerRegistry().getById(battleData.getPlayer().getName().getString());
         var trainerNpc = RCT.getTrainerRegistry().getById(battleData.getTrainerNpc().getTrainerId());
         if (trainerNpc == null || player == null) return;
         var battleId = RCT.getBattleManager().startBattle(new ArrayList<>(List.of(player)), new ArrayList<>(List.of(trainerNpc)), battleData.getFormat(), battleData.getRules());
         registerBattleListeners(battleId, battleData);
    }

    static void registerBattleListeners(UUID battleId, BasicTrainerBattle battleData) {
        if (battleId != null) {
            EventListener<?>[] onEnd = createEndBattleEventListener(battleId, battleData);

            RCT.getEventContext().register(Events.BATTLE_ENDED, (EventListener<BattleState>) onEnd[0]);
        }

    }

    private static EventListener<?> @NotNull [] createEndBattleEventListener(UUID battleId, BasicTrainerBattle battleData) {
        EventListener<?>[] onEnd = new EventListener[1];

        onEnd[0] = e -> {
            if (e.getValue() instanceof BattleState bs && bs.getBattle().getBattleId().equals(battleId)) {
                if (!bs.isEndForced()) {
                        var winnersFirst = Stream
                                .concat(bs.getWinners().stream(), bs.getLosers().stream())
                                .map(Trainer::getEntity).toArray(LivingEntity[]::new);

                        var winningEntity = Arrays.stream(winnersFirst).findFirst();
                        if (winningEntity.isPresent()){
                            if (winningEntity.get() == battleData.getPlayer()){
                                battleData.getTrainerNpc().finishBattle(battleData, true);
                            }
                            else {
                                battleData.getTrainerNpc().finishBattle(battleData, false);
                            }
                        }
                    }
                }

                RCT.getEventContext().unregister(Events.BATTLE_ENDED, (EventListener<BattleState>) onEnd[0]);

        };

        return onEnd;
    }


}