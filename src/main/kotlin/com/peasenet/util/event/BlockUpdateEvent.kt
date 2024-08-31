package com.peasenet.util.event

import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.listeners.BlockUpdateListener

class BlockUpdateEvent(private var blockUpdateData: BlockUpdate) : Event<BlockUpdateListener>() {

    override fun fire(listeners: ArrayList<BlockUpdateListener>) {
        for (listener in listeners) {
            listener.onBlockUpdate(blockUpdateData)
        }
    }

    override val event: Class<BlockUpdateListener>
        get() = BlockUpdateListener::class.java
}