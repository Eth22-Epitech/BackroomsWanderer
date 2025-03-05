package eth22.backroomswanderer.datagen;

import eth22.backroomswanderer.BackroomsWanderer;
import eth22.backroomswanderer.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_WALLPAPER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    @Override
    public String getName() {
        return "BackroomsWanderer Model Provider";
    }
}