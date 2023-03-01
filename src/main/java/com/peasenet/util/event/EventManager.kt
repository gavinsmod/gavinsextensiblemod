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
package com.peasenet.util.event;

import com.peasenet.util.listeners.Listener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The event manager. Allows adding/removing listeners and firing events.
 *
 * @author GT3CH1
 * @version 12/22/2022
 */
public class EventManager {

    // a list of event classes with the mod that created them
    protected static final HashMap<Class<? extends Listener>, ArrayList<? extends Listener>> eventMap = new HashMap<>();

    /**
     * Adds a listener to the event manager.
     *
     * @param event    - The event class.
     * @param listener - The listener to add.
     */
    public <L extends Listener> void subscribe(Class<L> event, L listener) {
        eventMap.computeIfAbsent(event, k -> new ArrayList<>());
        ((ArrayList<L>) (eventMap.get(event))).add(listener);
    }

    /**
     * Removes a listener from the event manager.
     *
     * @param event    - The event class.
     * @param listener - The listener to remove.
     */
    public <L extends Listener> void unsubscribe(Class<L> event, L listener) {
        eventMap.get(event).remove(listener);
    }

    /**
     * Calls the event to be fired.
     *
     * @param event - The event to fire.
     */
    @SuppressWarnings({"unchecked"})
    public <L extends Listener, E extends Event<L>> void call(E event) {
        var listeners = (ArrayList<L>) eventMap.get(event.getEvent());
        if (listeners != null) {
            event.fire(listeners);
        }
    }
}
