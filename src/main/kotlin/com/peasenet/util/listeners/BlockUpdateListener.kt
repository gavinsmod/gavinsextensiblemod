package com.peasenet.util.listeners

import com.peasenet.util.event.data.BlockUpdate

interface BlockUpdateListener : Listener {
    fun onBlockUpdate(bue: BlockUpdate)
}