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
package com.peasenet.util.event

import com.peasenet.util.listeners.Listener

/**
 * The event manager. Allows adding/removing listeners and firing events.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
open class EventManager {
    /**
     * Adds a listener to the event manager.
     *
     * @param event    - The event class.
     * @param listener - The listener to add.
     */
    fun <L : Listener> subscribe(event: Class<L>, listener: L) {
        eventMap.computeIfAbsent(event) { k: Class<out Listener?>? -> ArrayList() }
        (eventMap[event] as ArrayList<L>).add(listener)
    }

    /**
     * Removes a listener from the event manager.
     *
     * @param event    - The event class.
     * @param listener - The listener to remove.
     */
    fun <L : Listener> unsubscribe(event: Class<L>, listener: L) {
        eventMap[event]!!.remove(listener)
    }

    /**
     * Calls the event to be fired.
     *
     * @param event - The event to fire.
     */
    fun <L : Listener, E : Event<L>> call(event: E) {
        // get all listeners for the event
        val listeners = eventMap[event.event] ?: return

        event.fire(listeners as ArrayList<L>)
    }


    companion object {
        // a list of event classes with the mod that created them
        protected val eventMap = HashMap<Class<out Listener>, ArrayList<Listener>>()

        @JvmStatic
        val eventManager = EventManager()


    }
}