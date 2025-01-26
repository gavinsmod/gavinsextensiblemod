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

import com.peasenet.util.event.data.DrawState
import com.peasenet.util.listeners.ShouldDrawSideListener

/**
 * Called when a block is rendered and if the side should be drawn. This event is cancellable.
 * @see CancellableEvent
 * @see ShouldDrawSideListener
 * @param evt - The [DrawState] event.
 *
 * @author GT3CH1
 * @version 01-26-2025
 * @since 03-02-2023
 */
class ShouldDrawSideEvent(private var evt: DrawState) : CancellableEvent<ShouldDrawSideListener>() {
    override fun fire(listeners: ArrayList<ShouldDrawSideListener>) {
        for (listener in listeners) {
            listener.onDrawSide(evt)
            if (evt.isCancelled) {
                cancel()
            }
        }
    }

    override val event: Class<ShouldDrawSideListener>
        get() = ShouldDrawSideListener::class.java
}