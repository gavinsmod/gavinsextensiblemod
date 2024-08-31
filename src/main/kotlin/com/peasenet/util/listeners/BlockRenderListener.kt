package com.peasenet.util.listeners

import com.peasenet.util.event.data.BlockRender

/**
 * A listener for the block render event.
 * 
 * @author GT3CH1
 * @version 08-31-2024
 */
interface BlockRenderListener : Listener {
    /**
     * Called when a block is rendered.
     */
    fun onBlockRender(blockRender: BlockRender)
}