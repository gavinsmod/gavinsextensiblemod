package com.peasenet.config.movement

import com.peasenet.config.Config

/**
 *
 * @author GT3CH1
 * @version 05-31-2026
 * @since 05-31-2026 
 */
class NoFallConfig  : Config<NoFallConfig>() {
    init {
        key = "nofall"
    }

    public var pauseOnElytra: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

}