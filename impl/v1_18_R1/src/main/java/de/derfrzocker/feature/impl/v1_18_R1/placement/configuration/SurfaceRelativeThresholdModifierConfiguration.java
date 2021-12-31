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

package de.derfrzocker.feature.impl.v1_18_R1.placement.configuration;

import de.derfrzocker.feature.api.FeaturePlacementModifier;
import de.derfrzocker.feature.api.PlacementModifierConfiguration;
import de.derfrzocker.feature.common.value.number.IntegerValue;
import de.derfrzocker.feature.impl.v1_18_R1.value.heightmap.HeightmapValue;

public class SurfaceRelativeThresholdModifierConfiguration implements PlacementModifierConfiguration {

    private final FeaturePlacementModifier<?> placementModifier;
    private final HeightmapValue heightmap;
    private final IntegerValue minInclusive;
    private final IntegerValue maxInclusive;


    public SurfaceRelativeThresholdModifierConfiguration(FeaturePlacementModifier<?> placementModifier, HeightmapValue heightmap, IntegerValue minInclusive, IntegerValue maxInclusive) {
        this.placementModifier = placementModifier;
        this.heightmap = heightmap;
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    @Override
    public FeaturePlacementModifier<?> getPlacementModifier() {
        return placementModifier;
    }

    public HeightmapValue getHeightmap() {
        return heightmap;
    }

    public IntegerValue getMinInclusive() {
        return minInclusive;
    }

    public IntegerValue getMaxInclusive() {
        return maxInclusive;
    }
}
