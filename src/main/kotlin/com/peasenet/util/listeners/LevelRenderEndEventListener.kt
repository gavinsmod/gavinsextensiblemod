package com.peasenet.util.listeners

import com.peasenet.util.event.data.LevelRenderEndEventData

/**
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
interface LevelRenderEndEventListener : Listener {
    fun onLevelRenderEnd(event: LevelRenderEndEventData)
}