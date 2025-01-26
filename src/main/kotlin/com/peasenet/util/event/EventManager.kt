/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.peasenet.util.event

import com.peasenet.util.listeners.Listener

/**
 * An event manager that allows for [Listener]s to subscribe to their corresponding events, as well for calling and
 * managing those events.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 03-02-2023
 */
open class EventManager {
    /**
     * Adds a listener to the event manager. For example:
     * ~~~
     * subscribe(RenderSubmergedOverlayListener::class.java, this)
     * ~~~
     * This will add [listener] to the list of [com.peasenet.util.listeners.RenderSubmergedOverlayListener]s, which
     * will be called when the corresponding event is fired.
     * @param event    The event class.
     * @param listener The listener to add.
     */
    @Suppress("UNCHECKED_CAST")
    fun <L : Listener> subscribe(event: Class<L>, listener: L) {
        eventMap.computeIfAbsent(event) { _: Class<out Listener?>? -> ArrayList() }
        (eventMap[event] as ArrayList<L>).add(listener)
    }

    /**
     * Removes the given listener from the event manager. For example:
     * ~~~
     * unsubscribe(RenderSubmergedOverlayListener::class.java, this)
     * ~~~
     * This will remove [listener] from the list of [com.peasenet.util.listeners.RenderSubmergedOverlayListener]s, which
     * will prevent it from being called when the corresponding event is fired.
     * @param event    The event class.
     * @param listener The listener to remove.
     * @throws IllegalArgumentException Will be thrown if/when the listener is not found, usually caused by [subscribe]
     * not being called.
     */
    fun <L : Listener> unsubscribe(event: Class<L>, listener: L) {
        try {
            eventMap[event]!!.remove(listener)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("Listener not found. Please report this error.")
        }
    }

    /**
     * Calls all listeners for the given event. For example:
     * ~~~
     * call(EntityRenderNameEvent(EntityNameRender(...)))
     * ~~~
     * will call all [com.peasenet.util.listeners.EntityRenderNameListener]s with the given [com.peasenet.util.event.EntityRenderNameEvent]
     * @param event The event to fire.
     */
    @Suppress("UNCHECKED_CAST")
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