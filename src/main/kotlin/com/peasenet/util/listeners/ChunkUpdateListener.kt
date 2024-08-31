package com.peasenet.util.listeners

import com.peasenet.util.event.data.ChunkUpdate

interface ChunkUpdateListener : Listener{
    fun onChunkUpdate(chunkUpdate: ChunkUpdate)
}