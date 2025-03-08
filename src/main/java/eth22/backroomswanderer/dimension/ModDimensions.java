package eth22.backroomswanderer.dimension;

import eth22.backroomswanderer.BackroomsWanderer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    public static final RegistryKey<DimensionType> LEVEL_0_DIMENSION_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of("backroomswanderer", "level_0"));
    public static final RegistryKey<DimensionOptions> LEVEL_0_DIMENSION = RegistryKey.of(RegistryKeys.DIMENSION, Identifier.of("backroomswanderer", "level_0"));

    public static void registerDimension() {
        BackroomsWanderer.LOGGER.info("Registering Dimensions for " + BackroomsWanderer.MOD_ID);
    }
}
