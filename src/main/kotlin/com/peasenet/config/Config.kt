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
 * The base class for all configuration files. You should extend this class for your own configuration,
 * and then calling [Settings.addConfig] in your mod's init method BEFORE calling GavinsMod.addMod.
 * It is also required that you initialize the "key" for your settings. You can then fetch and manipulate
 * your config using the Settings#getConfig<T>(key) method. For examples of extending this class,
 * see [MiscConfig].
 *
 * Settings defined in the config should be public and have a setter method, as well as be serializable.
 * For example,
 * ~~~kotlin
 * class MyConfig : Config<MyConfig>() {
 *   init {
 *      key = "myConfig"
 *   }
 *   val somethingEnabled: Boolean 
 *   set(value) {
 *      field = value
 *      saveConfig()
 *   }
 * }
 * ~~~
 * It is important that you call [saveConfig] after changing a setting, otherwise the setting will not be saved and thus
 * not applied. Upon calling [saveConfig], the config will be saved to the settings file under the key specified.
 * You may then get the current state of your config by calling [Settings.getConfig] with the key specified.
 * @param <E> The type of the config.
 * @author GT3CH1
 * @version 07-18-2023
 * @see MiscConfig
 * @see Settings
 */
abstract class Config<E : Config<E>> {
    /**
     * The key for the config. This is used for fetching the config from the settings file.
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
        return Settings.fetchConfig(javaClass, key!!)
    }
}