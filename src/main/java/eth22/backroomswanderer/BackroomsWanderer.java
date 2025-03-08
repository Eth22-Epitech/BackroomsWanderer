package eth22.backroomswanderer;

import eth22.backroomswanderer.block.ModBlocks;
import eth22.backroomswanderer.dimension.ModChunkGenerators;
import eth22.backroomswanderer.dimension.ModDimensions;
import eth22.backroomswanderer.item.ModItemGroups;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsWanderer implements ModInitializer {
	public static final String MOD_ID = "backroomswanderer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemGroups();
		ModChunkGenerators.registerChunkGenerators();
		ModDimensions.registerDimension();
	}
}