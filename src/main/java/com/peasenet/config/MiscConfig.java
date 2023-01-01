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

package com.peasenet.config;

/**
 * The miscellaneous configuration.
 *
 * @author gt3ch1
 * @version 12/31/2022
 */
public class MiscConfig extends Config<MiscConfig> {

    /**
     * The instance of the configuration.
     */
    private static MiscConfig instance;
    /**
     * Whether to show messages about enabling or disabling mods.
     */
    private boolean messages = true;

    public MiscConfig() {
        setKey("misc");
        setInstance(this);
    }

    /**
     * Whether to show messages about enabling or disabling mods.
     *
     * @return True if messages should be shown.
     */
    public boolean isMessages() {
        return getInstance().messages;
    }

    /**
     * Sets whether to show messages about enabling or disabling mods.
     *
     * @param messages - True if messages should be shown.
     */
    public void setMessages(boolean messages) {
        this.getInstance().messages = messages;
    }

    @Override
    public MiscConfig getInstance() {
        return instance;
    }

    @Override
    public void setInstance(MiscConfig data) {
        instance = data;
    }
}
