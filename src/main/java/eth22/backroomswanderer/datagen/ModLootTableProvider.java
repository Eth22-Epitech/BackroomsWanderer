package eth22.backroomswanderer.datagen;

import eth22.backroomswanderer.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.LEVEL_0_WALLPAPER);
        addDrop(ModBlocks.LEVEL_0_WALLPAPER_BOTTOM);
        addDrop(ModBlocks.LEVEL_0_TORN_WALLPAPER);
        addDrop(ModBlocks.LEVEL_0_CARPET_BLOCK);
        addDrop(ModBlocks.LEVEL_0_CARPET);
        addDrop(ModBlocks.LEVEL_0_LIGHT);
        addDrop(ModBlocks.LEVEL_0_TILE);
    }
}