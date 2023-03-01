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
package com.peasenet.mods.esp

import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.entity.mob.MobEntity

/**
 * @author gt3ch1
 * @version 01/03/2022
 * A mod that allows the client to see boxes around mobs.
 */
class ModMobEsp : Mod(Type.MOB_ESP), EntityRenderListener {
    init {
        val hostileEspColor = ColorSetting("gavinsmod.settings.esp.mob.hostile.color")
        hostileEspColor.setCallback { espConfig.hostileMobColor = hostileEspColor.color }
        hostileEspColor.color = espConfig.hostileMobColor
        val peacefulEspColor = ColorSetting("gavinsmod.settings.esp.mob.peaceful.color")
        peacefulEspColor.setCallback { espConfig.peacefulMobColor = peacefulEspColor.color }
        peacefulEspColor.color = espConfig.peacefulMobColor
        val hostileEspToggle = ToggleSetting("gavinsmod.settings.esp.mob.hostile")
        hostileEspToggle.setCallback { espConfig.showHostileMobs = hostileEspToggle.value }
        hostileEspToggle.value = espConfig.showHostileMobs
        val peacefulEspToggle = ToggleSetting("gavinsmod.settings.esp.mob.peaceful")
        peacefulEspToggle.setCallback { espConfig.showPeacefulMobs = peacefulEspToggle.value }
        peacefulEspToggle.value = espConfig.showPeacefulMobs
        addSetting(hostileEspColor)
        addSetting(peacefulEspColor)
        addSetting(hostileEspToggle)
        addSetting(peacefulEspToggle)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderListener::class.java, this)
    }

    override fun onEntityRender(er: EntityRender) {
        val box = RenderUtils.getEntityBox(er.delta, er.entity)
        if (er.entity !is MobEntity) return
        if (er.buffer == null) return
        if (espConfig.showPeacefulMobs && er.entityType.spawnGroup.isPeaceful) {
            RenderUtils.drawBox(er.stack, er.buffer, box, espConfig.peacefulMobColor, espConfig.alpha)
        } else if (espConfig.showHostileMobs && !er.entityType.spawnGroup.isPeaceful) {
            RenderUtils.drawBox(er.stack, er.buffer, box, espConfig.hostileMobColor, espConfig.alpha)
        }
    }
}