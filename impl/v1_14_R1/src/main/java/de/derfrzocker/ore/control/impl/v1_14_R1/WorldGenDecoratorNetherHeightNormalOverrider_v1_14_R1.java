package de.derfrzocker.ore.control.impl.v1_14_R1;

import com.mojang.datafixers.Dynamic;
import de.derfrzocker.ore.control.api.Biome;
import de.derfrzocker.ore.control.api.NMSService;
import de.derfrzocker.spigot.utils.ChunkCoordIntPair;
import lombok.NonNull;
import net.minecraft.server.v1_14_R1.*;

import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("Duplicates")
public class WorldGenDecoratorNetherHeightNormalOverrider_v1_14_R1 extends WorldGenDecoratorNetherHeight {

    @NonNull
    private final Biome biome;

    public WorldGenDecoratorNetherHeightNormalOverrider_v1_14_R1(final Function<Dynamic<?>, ? extends WorldGenFeatureChanceDecoratorCountConfiguration> dynamicFunction, final Biome biome) {
        super(dynamicFunction);
        this.biome = biome;
    }

    @Override
    public <C extends WorldGenFeatureConfiguration> boolean a(final GeneratorAccess generatorAccess, final ChunkGenerator<? extends GeneratorSettingsDefault> chunkGenerator, final Random random, final BlockPosition blockPosition, final WorldGenFeatureChanceDecoratorCountConfiguration worldGenFeatureChanceDecoratorCountConfiguration, final WorldGenFeatureConfigured<C> worldGenFeatureConfigured) {
        final NMSService nmsService = NMSUtil_v1_14_R1.getOreControlService().getNMSService();

        return nmsService.generate(generatorAccess.getMinecraftWorld().getWorld(), biome, nmsService.getNMSUtil().getOre(((WorldGenFeatureOreConfiguration) worldGenFeatureConfigured.b).c.getBlock()), new ChunkCoordIntPair(blockPosition.getX() >> 4, blockPosition.getZ() >> 4), worldGenFeatureChanceDecoratorCountConfiguration, worldGenFeatureConfigured,
                null,
                (configuration, featureConfiguration) -> super.a(generatorAccess, chunkGenerator, random, blockPosition, (WorldGenFeatureChanceDecoratorCountConfiguration) configuration, (WorldGenFeatureConfigured<?>) featureConfiguration)
                , random);
    }

}
