package eth22.backroomswanderer.dimension;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModBiomes {
    public static final RegistryKey<Biome> LEVEL0_BIOME = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("backroomswanderer", "level_0"));

    public static RegistryEntry<Biome> getLevel0Biome(RegistryEntryLookup<Biome> biomeRegistry) {
        return biomeRegistry.getOrThrow(LEVEL0_BIOME);
    }
}
