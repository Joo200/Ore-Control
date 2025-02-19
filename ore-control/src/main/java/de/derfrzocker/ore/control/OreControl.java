/*
 * MIT License
 *
 * Copyright (c) 2019 - 2022 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.ore.control;

import com.google.common.base.Charsets;
import com.mojang.serialization.Codec;
import de.derfrzocker.feature.common.feature.placement.ActivationModifier;
import de.derfrzocker.feature.common.ruletest.AlwaysTrueRuleTestType;
import de.derfrzocker.feature.common.ruletest.BlockMatchRuleTestType;
import de.derfrzocker.feature.common.ruletest.BlockStateMatchRuleTestType;
import de.derfrzocker.feature.common.ruletest.RandomBlockMatchRuleTestType;
import de.derfrzocker.feature.common.ruletest.RandomBlockStateMatchRuleTestType;
import de.derfrzocker.feature.common.ruletest.TagMatchRuleTestType;
import de.derfrzocker.feature.common.value.bool.BooleanType;
import de.derfrzocker.feature.common.value.bool.FixedBooleanType;
import de.derfrzocker.feature.common.value.number.FixedFloatType;
import de.derfrzocker.feature.common.value.number.FloatType;
import de.derfrzocker.feature.common.value.number.IntegerType;
import de.derfrzocker.feature.common.value.number.integer.Exp4jIntegerType;
import de.derfrzocker.feature.common.value.number.integer.FixedDoubleToIntegerType;
import de.derfrzocker.feature.common.value.number.integer.FixedIntegerType;
import de.derfrzocker.feature.common.value.number.integer.biased.BiasedToBottomIntegerType;
import de.derfrzocker.feature.common.value.number.integer.clamped.ClampedIntegerType;
import de.derfrzocker.feature.common.value.number.integer.clamped.ClampedNormalIntegerType;
import de.derfrzocker.feature.common.value.number.integer.trapezoid.TrapezoidIntegerType;
import de.derfrzocker.feature.common.value.number.integer.uniform.UniformIntegerType;
import de.derfrzocker.feature.common.value.number.integer.weighted.WeightedListIntegerType;
import de.derfrzocker.feature.common.value.target.FixedTargetListType;
import de.derfrzocker.feature.common.value.target.TargetListType;
import de.derfrzocker.ore.control.api.NMSReplacer;
import de.derfrzocker.ore.control.api.OreControlManager;
import de.derfrzocker.ore.control.api.OreControlRegistries;
import de.derfrzocker.ore.control.api.config.Config;
import de.derfrzocker.ore.control.api.config.ConfigInfo;
import de.derfrzocker.ore.control.api.config.ConfigManager;
import de.derfrzocker.ore.control.api.config.ConfigType;
import de.derfrzocker.ore.control.api.config.dao.ConfigDao;
import de.derfrzocker.ore.control.api.config.dao.ConfigInfoDao;
import de.derfrzocker.ore.control.api.config.dao.ExtraValueDao;
import de.derfrzocker.ore.control.cache.config.ConfigCache;
import de.derfrzocker.ore.control.cache.extra.ExtraValueCache;
import de.derfrzocker.ore.control.cache.info.ConfigInfoCache;
import de.derfrzocker.ore.control.gui.OreControlGuiManager;
import de.derfrzocker.ore.control.impl.BasicConfigManager;
import de.derfrzocker.ore.control.impl.v1_18_R1.NMSReplacer_v1_18_R1;
import de.derfrzocker.ore.control.impl.v1_18_R2.NMSReplacer_v1_18_R2;
import de.derfrzocker.ore.control.impl.v1_19_R1.NMSReplacer_v1_19_R1;
import de.derfrzocker.ore.control.impl.v1_19_R2.NMSReplacer_v1_19_R2;
import de.derfrzocker.ore.control.impl.v1_19_R3.NMSReplacer_v1_19_R3;
import de.derfrzocker.ore.control.impl.v1_20_R1.NMSReplacer_v1_20_R1;
import de.derfrzocker.ore.control.interactions.BlockInteractionManager;
import de.derfrzocker.spigot.utils.Version;
import de.derfrzocker.spigot.utils.language.LanguageManager;
import de.derfrzocker.spigot.utils.language.loader.FileLanguageLoader;
import de.derfrzocker.spigot.utils.language.loader.MergeLanguageLoader;
import de.derfrzocker.spigot.utils.language.loader.PluginLanguageLoader;
import de.derfrzocker.spigot.utils.language.manager.DirectLanguageManager;
import de.derfrzocker.spigot.utils.setting.ConfigSetting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// TODO clean class up
public class OreControl extends JavaPlugin implements Listener {

    public final static String BASE_WIKI_URL = "https://github.com/DerFrZocker/Ore-Control/wiki/";
    private static final Version[] SUPPORTED_VERSION = new Version[]{Version.v1_20_R1, Version.v1_19_R3, Version.v1_19_R2, Version.v1_19_R1, Version.v1_18_R2, Version.v1_18_R1};
    private Version version = Version.UNKNOWN;
    private boolean loaded = false;
    private OreControlManager oreControlManager;
    private LanguageManager languageManager;
    private OreControlGuiManager guiManager;
    private List<ConfigSetting> guiSettings = new ArrayList<>();
    private NMSReplacer nmsReplacer;
    @Deprecated
    private Codec<Config> configCodec;

    @Override
    public void onLoad() {
        version = Version.getServerVersion(getServer());
        if (!Version.isSupportedVersion(getLogger(), version, SUPPORTED_VERSION)) {
            return;
        }

        loaded = true;
    }

    @Override
    public void onEnable() {
        if (!loaded) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        OreControlRegistries registries = new OreControlRegistries();
        ConfigDao configDao = new ConfigDao(registries);
        ConfigInfoDao configInfoDao = new ConfigInfoDao(this, new File(getDataFolder(), "data/configs"), new File(getDataFolder(), "data/global"));
        ConfigInfoCache configInfoCache = new ConfigInfoCache(configInfoDao);
        ConfigManager configManager = new BasicConfigManager(configInfoCache, new ExtraValueCache(new ExtraValueDao(), configInfoCache::getGlobalConfigInfo), new ConfigCache(configDao, configInfoCache::getGlobalConfigInfo));
        configManager.reload();
        oreControlManager = new OreControlManager(registries, configManager, world -> nmsReplacer.getBiomes(world));

        configCodec = configDao.getConfigCodec();
        nmsReplacer = getNmsReplacer();

        register(registries);
        File defaults = new File(getDataFolder(), "data/default");
        nmsReplacer.saveDefaultValues(defaults);

        nmsReplacer.hookIntoBiomes();

        File languageDirectory = new File(getDataFolder(), "lang");
        languageManager = new DirectLanguageManager(this, new MergeLanguageLoader(this, new PluginLanguageLoader(this, languageDirectory, true), new FileLanguageLoader(this, languageDirectory)), "en");
        saveResource("lang/README.txt", true);
        BlockInteractionManager interactionManager = new BlockInteractionManager(languageManager);
        getServer().getPluginManager().registerEvents(interactionManager, this);
        guiManager = new OreControlGuiManager(this, oreControlManager, languageManager, name -> {
            ConfigSetting guiSetting = new ConfigSetting(() -> YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("gui/default/" + name), Charsets.UTF_8)));
            guiSettings.add(guiSetting);
            return guiSetting;
        }, new Stats(this, registries), interactionManager);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if (oreControlManager != null) {
            oreControlManager.getConfigManager().save();
        }
    }

    private NMSReplacer getNmsReplacer() {
        if (version == Version.v1_18_R1) {
            return new NMSReplacer_v1_18_R1(oreControlManager, configCodec);
        } else if (version == Version.v1_18_R2) {
            return new NMSReplacer_v1_18_R2(oreControlManager, configCodec);
        } else if (version == Version.v1_19_R1) {
            return new NMSReplacer_v1_19_R1(this, oreControlManager, configCodec);
        } else if (version == Version.v1_19_R2) {
            return new NMSReplacer_v1_19_R2(this, oreControlManager, configCodec);
        } else if (version == Version.v1_19_R3) {
            return new NMSReplacer_v1_19_R3(this, oreControlManager, configCodec);
        } else if (version == Version.v1_20_R1) {
            return new NMSReplacer_v1_20_R1(this, oreControlManager, configCodec);
        } else {
            throw new IllegalStateException(String.format("No NMSReplacer found for version '%s', this is a bug!", version));
        }
    }

    private void register(OreControlRegistries registries) {
        registerRuleTests(registries);
        registerValueTypes(registries);
        registerFeatureGenerators(registries);
        registerPlacementModifier(registries);
        nmsReplacer.register();
    }

    private void registerRuleTests(OreControlRegistries registries) {
        registries.getRuleTestTypeRegistry().register(AlwaysTrueRuleTestType.INSTANCE);
        registries.getRuleTestTypeRegistry().register(BlockMatchRuleTestType.INSTANCE);
        registries.getRuleTestTypeRegistry().register(BlockStateMatchRuleTestType.INSTANCE);
        registries.getRuleTestTypeRegistry().register(RandomBlockMatchRuleTestType.INSTANCE);
        registries.getRuleTestTypeRegistry().register(RandomBlockStateMatchRuleTestType.INSTANCE);
        registries.getRuleTestTypeRegistry().register(TagMatchRuleTestType.INSTANCE);
    }

    private void registerValueTypes(OreControlRegistries registries) {
        registries.getValueTypeRegistry(IntegerType.class).register(FixedIntegerType.INSTANCE);
        registries.getValueTypeRegistry(IntegerType.class).register(FixedDoubleToIntegerType.INSTANCE);
        registries.getValueTypeRegistry(IntegerType.class).register(new UniformIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(new TrapezoidIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(new WeightedListIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(new BiasedToBottomIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(new ClampedIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(new ClampedNormalIntegerType(registries));
        registries.getValueTypeRegistry(IntegerType.class).register(Exp4jIntegerType.INSTANCE);
        registries.getValueTypeRegistry(FloatType.class).register(FixedFloatType.INSTANCE);
        registries.getValueTypeRegistry(BooleanType.class).register(FixedBooleanType.INSTANCE);
        registries.getValueTypeRegistry(TargetListType.class).register(new FixedTargetListType(registries));
    }

    private void registerFeatureGenerators(OreControlRegistries registries) {
    }

    private void registerPlacementModifier(OreControlRegistries registries) {
        registries.getPlacementModifierRegistry().register(new ActivationModifier(registries));
    }

    @Override // TODO move to own class
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        guiSettings.forEach(ConfigSetting::reload);
        guiManager.openGui(player);

        return true;
    }

    @EventHandler // TODO move to own class
    public void onWorldInit(WorldInitEvent event) {
        ConfigInfo configInfo = oreControlManager.getConfigManager().getOrCreateConfigInfo(event.getWorld().getName());
        if (configInfo.getConfigType() != ConfigType.WORLD) {
            configInfo.setConfigType(ConfigType.WORLD);
            oreControlManager.getConfigManager().saveAndReload();
        }
    }
}
