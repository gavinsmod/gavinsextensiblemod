package com.peasenet.util.event

import com.peasenet.util.event.data.LevelRenderEndEventData
import com.peasenet.util.listeners.LevelRenderEndEventListener

/**
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
class TextRenderEvent(private var evt: LevelRenderEndEventData) : CancellableEvent<LevelRenderEndEventListener>() {
    override fun fire(listeners: ArrayList<LevelRenderEndEventListener>) {
        for (listener in listeners) {
            listener.onLevelRenderEnd(evt)
            if (evt.isCancelled) {
                cancel()
            }
        }
    }

    override val event: Class<LevelRenderEndEventListener>
        get() = LevelRenderEndEventListener::class.java
}