package eth22.backroomswanderer.datagen;

import eth22.backroomswanderer.BackroomsWanderer;
import eth22.backroomswanderer.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_WALLPAPER);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_WALLPAPER_BOTTOM);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_TORN_WALLPAPER);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_CARPET_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LEVEL_0_TILE);

        generateLevel0LightBlockState(blockStateModelGenerator);
    }

    private void generateLevel0LightBlockState(BlockStateModelGenerator blockStateModelGenerator) {
        Identifier litModel = blockStateModelGenerator.createSubModel(ModBlocks.LEVEL_0_LIGHT, "_lit", Models.CUBE_ALL, id -> TextureMap.all(Identifier.of(BackroomsWanderer.MOD_ID, "block/level_0_light_lit")));
        Identifier unlitModel = blockStateModelGenerator.createSubModel(ModBlocks.LEVEL_0_LIGHT, "_unlit", Models.CUBE_ALL, id -> TextureMap.all(Identifier.of(BackroomsWanderer.MOD_ID, "block/level_0_light_unlit")));

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.LEVEL_0_LIGHT).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, unlitModel, litModel)));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    @Override
    public String getName() {
        return "BackroomsWanderer Model Provider";
    }
}