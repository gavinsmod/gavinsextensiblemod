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
package com.peasenet.config

import com.peasenet.annotations.Exclude
import com.peasenet.main.Settings

/**
 * The base class for all configuration files.
 *
 * @param <E> The type of the config.
 * @author gt3ch1
 * @version 03-02-2023
</E> */
abstract class Config<E : Config<E>> {
    /**
     * The key for the config.
     */
    @Exclude
    @Transient
    var key: String? = null
        protected set

    /**
     * Saves the configuration to file.
     */
    fun saveConfig() {
        val cfg = this
        Settings.settings[key!!] = cfg
        Settings.save()
    }

    /**
     * Reads and sets the instance of the configuration from file.
     *
     * @return The instance of the configuration.
     */
    fun readFromSettings(): Config<*> {
        return Settings.getConfig(javaClass, key!!)
    }
}