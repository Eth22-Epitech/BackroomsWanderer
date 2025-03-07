package eth22.backroomswanderer.block;

import eth22.backroomswanderer.BackroomsWanderer;
import eth22.backroomswanderer.block.custom.level_0.BlinkingLightBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block LEVEL_0_WALLPAPER = registerBlock("level_0_wallpaper", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final Block LEVEL_0_TORN_WALLPAPER = registerBlock("level_0_torn_wallpaper", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final Block LEVEL_0_CARPET_BLOCK = registerBlock("level_0_carpet_block", new Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block LEVEL_0_CARPET = registerBlock("level_0_carpet", new Block(AbstractBlock.Settings.copy(Blocks.WHITE_CARPET)));
    public static final Block LEVEL_0_LIGHT = registerBlock("level_0_light", new BlinkingLightBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)));
    public static final Block LEVEL_0_TILE = registerBlock("level_0_tile", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(BackroomsWanderer.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(BackroomsWanderer.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }
    public static void registerModBlocks() {
        BackroomsWanderer.LOGGER.info("Registering Mod Blocks for " + BackroomsWanderer.MOD_ID);
    }
}