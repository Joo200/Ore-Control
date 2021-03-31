/*
 * MIT License
 *
 * Copyright (c) 2019 - 2021 Marvin (DerFrZocker)
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

package de.derfrzocker.ore.control.gui.settings;

import de.derfrzocker.spigot.utils.Language;
import de.derfrzocker.spigot.utils.gui.BasicSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LanguageGuiSettings extends BasicSettings {

    public LanguageGuiSettings(@NotNull Plugin plugin, @NotNull String file) {
        super(plugin, file);
    }

    public LanguageGuiSettings(@NotNull Plugin plugin, @NotNull String file, boolean copy) {
        super(plugin, file, copy);
    }

    public LanguageGuiSettings(@NotNull Plugin plugin, @NotNull Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getLanguageItemStack(@NotNull final Language language) {
        final ItemStack itemStack = getSection().getItemStack("language." + language + ".item-stack");

        if (itemStack == null) {
            throw new IllegalArgumentException("There is no ItemStack for the language '" + language + "'");
        }

        return itemStack.clone();
    }

    public int getLanguageSlot(@NotNull final Language language) {
        return getSection().getInt("language." + language + ".slot");
    }

    public ItemStack getBackItemStack() {
        return getSection().getItemStack("back.item-stack").clone();
    }

    public int getBackSlot() {
        return getSection().getInt("back.slot");
    }

    @NotNull
    public ItemStack getInfoItemStack() {
        return getSection().getItemStack("info.item-stack").clone();
    }

    public int getInfoSlot() {
        return getSection().getInt("info.slot");
    }

}
