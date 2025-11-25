package net.electricbudgie;

import net.electricbudgie.datagen.ModNPCProvider;
import net.electricbudgie.datagen.ModTrainerProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CobblehoeTrainersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
			FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModTrainerProvider::new);
		pack.addProvider(ModNPCProvider::new);
	}
}
