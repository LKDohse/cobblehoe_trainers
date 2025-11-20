package net.electricbudgie;

import com.gitlab.srcmc.rctapi.api.RCTApi;
import com.google.gson.Gson;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.electricbudgie.entity.ModEntities;
import net.electricbudgie.entity.custom.NPCEntity;
import net.electricbudgie.networking.DialoguePayload;
import net.electricbudgie.world.gen.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobblehoeTrainers implements ModInitializer {
	public static final String MOD_ID = "cobblehoetrainers";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Our own instance of the service.
	private static final RCTApi RCT = RCTApi.initInstance(MOD_ID);

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

		FabricDefaultAttributeRegistry.register(ModEntities.NPC, NPCEntity.createMobAttributes());

		registerEvents();
	}

	static void registerEvents(){
		LifecycleEvent.SERVER_STARTING.register(CobblehoeTrainers::onServerStarting);

		PlayerEvent.PLAYER_JOIN.register(CobblehoeTrainers::onPlayerJoin);
		PlayerEvent.PLAYER_QUIT.register(CobblehoeTrainers::onPlayerQuit);
	}

	static void onServerStarting(MinecraftServer server){
		var trainerRegistry = RCT.getTrainerRegistry();
		trainerRegistry.init(server);
	}

	static void onPlayerJoin(ServerPlayerEntity player) {
		RCT.getTrainerRegistry().registerPlayer(player.getName().getString(), player);
	}

	static void onPlayerQuit(PlayerEntity player) {
		RCT.getTrainerRegistry().unregisterById(player.getName().getString());
	}


}