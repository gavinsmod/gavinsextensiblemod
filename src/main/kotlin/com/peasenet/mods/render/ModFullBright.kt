/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mods.render

import com.peasenet.config.FullbrightConfig
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.mixinterface.ISimpleOption
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
class ModFullBright : RenderMod(
    "Full Bright", "gavinsmod.mod.render.fullbright", "fullbright"
) {
    companion object {
        lateinit var fullbrightConfig: FullbrightConfig
    }
    init {
        fullbrightConfig = Settings.getConfig<FullbrightConfig>("fullbright")
        val gammaFade = SettingBuilder().setTitle("gavinsmod.settings.render.fullbright.gammafade")
            .setState(fullbrightConfig.gammaFade)
//            .setWidth(100)
//            .setHeight(10)
            .buildToggleSetting()
        gammaFade.setCallback { fullbrightConfig.gammaFade = gammaFade.value }

        val autoFullBright = SettingBuilder().setTitle("gavinsmod.settings.render.fullbright.autofullbright")
            .setState(fullbrightConfig.autoFullBright)
//            .setWidth(100)
//            .setHeight(10)
            .buildToggleSetting()
        autoFullBright.setCallback { fullbrightConfig.autoFullBright = autoFullBright.value }

//        val gamma = SlideSetting("gavinsmod.settings.render.fullbright.gamma")
//        gamma.setCallback { fullbrightConfig.gamma = gamma.value }
//        gamma.value = fullbrightConfig.gamma
        val gamma =
            SettingBuilder().setTitle("gavinsmod.settings.render.fullbright.gamma").setValue(fullbrightConfig.gamma)
                .setWidth(100).setHeight(10).buildSlider()
        gamma.setCallback { fullbrightConfig.gamma = gamma.value }


//        val subSetting = SubSetting(100, 10, translationKey)

        val subSetting = SettingBuilder().setWidth(100).setHeight(10).setTitle(translationKey).setDefaultMaxChildren(4)
            .buildSubSetting()

        subSetting.add(gammaFade)
        subSetting.add(autoFullBright)
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
//        gamma = fullbrightConfig.gamma.toDouble()
        super.deactivate()
    }


    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    fun setHighGamma() {
        if (fullbrightConfig.gammaFade) {
            fadeGammaUp()
        } else {
            gamma = fullbrightConfig.maxGamma.toDouble()
        }
    }

    /**
     * The last player configured gamma.
     */
    private var LAST_GAMMA = 0.0
    
    /**
     * Resets the gamma to the players last configured value.
     */
    fun setLowGamma() {
        if (fullbrightConfig.gammaFade) {
            fadeGammaDown()
        } else {
            gamma = LAST_GAMMA
        }
    }

    var gamma: Double
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
            var newValue = gamma
            val maxGamma = fullbrightConfig.maxGamma
            if (newValue < 0.0) newValue = 0.0
            if (newValue > maxGamma) newValue = maxGamma.toDouble()
            val newGamma = GavinsModClient.minecraftClient.options.gamma
            if (newGamma.value != newValue) {
                val newGamma2 = (newGamma as ISimpleOption<Double>)
                newGamma2.forceSetValue(newValue)
            }
        }
    val isHighGamma: Boolean
        get() = gamma == 16.0
    val isLastGamma: Boolean
        get() = gamma <= LAST_GAMMA

    fun setLastGamma() {
        if (gamma > 1) return
        LAST_GAMMA = gamma
    }

    fun getLastGamma(): Double {
        return LAST_GAMMA
    }

    private fun fadeGammaUp() {
        gamma += 0.2f
    }

    private fun fadeGammaDown() {
        gamma -= 0.2f
        if (gamma < getLastGamma()) gamma = getLastGamma()
    }

}
