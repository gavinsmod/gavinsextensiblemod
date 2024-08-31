package com.peasenet.util.event

import com.peasenet.util.event.data.ChunkUpdate
import com.peasenet.util.listeners.ChunkUpdateListener

class ChunkUpdateEvent(private val chunkUpdate: ChunkUpdate) : Event<ChunkUpdateListener>() {

    override fun fire(listeners: ArrayList<ChunkUpdateListener>) {
        listeners.forEach { it.onChunkUpdate(chunkUpdate) }
    }

    override val event: Class<ChunkUpdateListener>
        get() = ChunkUpdateListener::class.java
}