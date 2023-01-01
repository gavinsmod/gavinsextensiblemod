/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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

package com.peasenet.config;

import com.peasenet.annotations.Exclude;
import com.peasenet.main.Settings;

/**
 * The base class for all configuration files.
 *
 * @param <E> The type of the config.
 * @author gt3ch1
 * @version 12/31/2022
 */
public abstract class Config<E extends Config<?>> {

    /**
     * The key for the config.
     */
    @Exclude
    private transient String key;

    /**
     * Gets the instance of the configuration.
     *
     * @return The instance of the configuration.
     */
    public abstract E getInstance();

    /**
     * Sets the instance of the configuration.
     *
     * @param data - The instance of the configuration.
     */
    public abstract void setInstance(E data);

    /**
     * Saves the configuration to file.
     */
    public void saveConfig() {
        var cfg = getInstance();
        Settings.settings.put(key, cfg);
        Settings.save();
    }

    /**
     * Reads and sets the instance of the configuration from file.
     *
     * @return The instance of the configuration.
     */
    public E readFromSettings() {
        var _e = Settings.getConfig(this.getClass(), key);
        setInstance((E) _e);
        return (E) _e;
    }

    /**
     * Gets the key of the configuration.
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key of the configuration.
     *
     * @param key - The key of the configuration.
     */
    protected void setKey(String key) {
        this.key = key;
    }
}
