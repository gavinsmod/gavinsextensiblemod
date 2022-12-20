/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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


package com.peasenet.util.event;

import com.peasenet.mods.Mod;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Event {

    // a list of event classes with the mod that created them

    protected static final HashMap<Class<Event>, HashSet<Mod>> eventMap = new HashMap<>();
    private boolean cancelled;

    public static void subscribe(Class<? extends Event> event, Mod mod) {
        // check if the event is already in the map
        if (eventMap.get(event) == null) {
            eventMap.put((Class<Event>) event, new HashSet<>());
        }
        eventMap.get(event).add(mod);
    }

    public static void unsubscribe(Class<? extends Event> event, Mod mod) {
        eventMap.get(event).remove(mod);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
