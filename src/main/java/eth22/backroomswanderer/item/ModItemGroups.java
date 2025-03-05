package eth22.backroomswanderer.item;

import eth22.backroomswanderer.BackroomsWanderer;
import eth22.backroomswanderer.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup BACKROOMSWANDERER_BLOCKS = Registry.register(Registries.ITEM_GROUP,
        Identifier.of(BackroomsWanderer.MOD_ID, "blocks"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.backroomswanderer.blocks"))
            .icon(() -> new ItemStack(ModBlocks.LEVEL_0_WALLPAPER))
            .entries(((displayContext, entries) -> {
                entries.add(ModBlocks.LEVEL_0_WALLPAPER);
            }))
            .build());

    public static void registerItemGroups() {
        BackroomsWanderer.LOGGER.info("Registering Item Groups for " + BackroomsWanderer.MOD_ID);
    }
}
