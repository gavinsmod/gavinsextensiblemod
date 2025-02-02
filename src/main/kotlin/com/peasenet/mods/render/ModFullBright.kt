/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
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
 */
@file:Suppress("UNCHECKED_CAST")

package com.peasenet.mods.render

import com.peasenet.config.render.FullbrightConfig
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.mixinterface.ISimpleOption
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.SlideSetting
import com.peasenet.settings.SubSetting
import com.peasenet.settings.ToggleSetting

/**
 * A mod that allows the client to see very clearly in the absence of a light source.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 03-02-2023
 */
@Suppress("KotlinConstantConditions")
class ModFullBright : RenderMod(
    "gavinsmod.mod.render.fullbright", "fullbright"
) {
    companion object {
        lateinit var fullbrightConfig: FullbrightConfig
    }

    init {
        fullbrightConfig = Settings.getConfig("fullbright")
        val gammaFade = SettingBuilder<ToggleSetting>().setTitle("gavinsmod.settings.render.fullbright.gammafade")
            .setState(fullbrightConfig.gammaFade)
            .setCallback { fullbrightConfig.gammaFade = it.value }
            .buildToggleSetting()

        val gamma =
            SettingBuilder<SlideSetting>().setTitle("gavinsmod.settings.render.fullbright.gamma")
                .setValue(fullbrightConfig.gamma)
                .setWidth(100).setHeight(10).setCallback { fullbrightConfig.gamma = it.value }
                .buildSlideSetting()
        val subSetting =
            SettingBuilder<SubSetting>().setWidth(100).setHeight(10).setTitle(translationKey).setDefaultMaxChildren(4)
                .buildSubSetting()

        subSetting.add(gammaFade)
        subSetting.add(gamma)

        addSetting(subSetting)
    }

    override fun activate() {
        super.activate()
        if (!GavinsMod.isEnabled("xray")) setLastGamma()
        deactivating = false
    }

    override fun onTick() {
        if (isActive && !isHighGamma) {
            setHighGamma()
            return
        } else if (!GavinsMod.isEnabled("xray") && !isLastGamma && deactivating) {
            setLowGamma()
            deactivating = !isLastGamma
        }
    }

    override fun onDisable() {
        super.onDisable()
        if (!fullbrightConfig.gammaFade) {
            gamma = getLastGamma()
            return
        }
        if (gamma > 16F && !deactivating) gamma = 16.0

    }


    override fun deactivate() {
        deactivating = true
        super.deactivate()
    }


    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    private fun setHighGamma() {
        if (fullbrightConfig.gammaFade) {
            fadeGammaUp()
        } else {
            gamma = fullbrightConfig.maxGamma().toDouble()
        }
    }

    /**
     * The last player configured gamma.
     */
    private var lastGamma = 0.0

    /**
     * Resets the gamma to the players last configured value.
     */
    private fun setLowGamma() {
        if (fullbrightConfig.gammaFade) {
            fadeGammaDown()
        } else {
            gamma = lastGamma
        }
    }

    private var gamma: Double
        /**
         * Gets the current game gamma.
         *
         * @return The current game gamma.
         */
        get() = GavinsModClient.minecraftClient.options.gamma.value
        /**
         * Sets the gamma to the given value.
         *
         * @param gamma The value to set the gamma to.
         */
        set(gamma) {
            val newValue = gamma
            val maxGamma = fullbrightConfig.maxGamma()
            newValue.coerceAtLeast(0.0)
                .coerceAtMost(maxGamma.toDouble())
            val newGamma = GavinsModClient.minecraftClient.options.gamma
            (newGamma as ISimpleOption<Double>).forceSetValue(newValue)
        }
    private val isHighGamma: Boolean
        get() = gamma == 16.0
    private val isLastGamma: Boolean
        get() = gamma <= lastGamma

    private fun setLastGamma() {
        if (gamma > 1) return
        lastGamma = gamma
    }

    private fun getLastGamma(): Double {
        return lastGamma
    }

    private fun fadeGammaUp() {
        gamma += 0.2f
    }

    private fun fadeGammaDown() {
        gamma -= 0.2f
        gamma.coerceAtLeast(getLastGamma())
    }

}
