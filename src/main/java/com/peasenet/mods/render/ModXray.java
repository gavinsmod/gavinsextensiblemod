/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mods.render;

import com.peasenet.gui.mod.xray.GuiXray;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import net.minecraft.block.BlockState;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A mod for xray like feature, allowing the player to see through certain blocks.
 */
public class ModXray extends Mod {

    public ModXray() {
        super(Type.XRAY);
        var dropdownWidth = 80;
        SubSetting xraySubSetting = new SubSetting(100, 10, getTranslationKey());
        ToggleSetting culling = new ToggleSetting("xray.disable_culling", "gavinsmod.settings.xray.culling");
        culling.setWidth(dropdownWidth);
        culling.setCallback(() -> {
            if (isActive()) reload();
        });
        ClickSetting menu = new ClickSetting("xray.menu", "gavinsmod.settings.xray.blocks");
        menu.setCallback(() -> getClient().setScreen(new GuiXray()));
        xraySubSetting.add(menu);
        menu.setWidth(dropdownWidth);
        xraySubSetting.add(culling);
        addSetting(xraySubSetting);
    }


    /**
     * Checks if a block is visible
     *
     * @param block Block to check
     * @return True if visible, false if not
     */
    public static boolean shouldDrawFace(BlockState block) {
        if (GavinsMod.isEnabled(Type.XRAY)) return Settings.isXrayBlock(block.getBlock());
        return true;
    }

    @Override
    public void activate() {
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT)) RenderUtils.setLastGamma();
        getClient().setChunkCulling(Settings.getBool("xray.disable_culling"));
        super.activate();
        reloadRenderer();
    }

    /**
     * Reloads the renderer if and only if the setting "xray.forcereload" is true.
     */
    private void reloadRenderer() {
        if (Mods.getMod("xray").isActive()) getClient().getWorldRenderer().reload();
    }

    @Override
    public void onTick() {
        if (isActive() && !RenderUtils.isHighGamma()) RenderUtils.setHighGamma();
        else if (!GavinsMod.isEnabled(Type.FULL_BRIGHT) && !RenderUtils.isLastGamma() && deactivating) {
            RenderUtils.setLowGamma();
            deactivating = !RenderUtils.isLastGamma();
        }
    }

    @Override
    public boolean isDeactivating() {
        return deactivating;
    }

    @Override
    public void deactivate() {
        // check if full bright is disabled, if it is, reset gamma back to LAST_GAMMA
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT)) RenderUtils.setLowGamma();
        getClient().setChunkCulling(true);
        reloadRenderer();
        deactivating = true;
        RenderUtils.setGamma(4);
        super.deactivate();
    }
}
