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

import com.peasenet.util.event.data.ChatMessage
import com.peasenet.util.listeners.OnChatSendListener

/**
 * An event for when a packet is sent.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
class ChatSendEvent(msg: String) : CancellableEvent<OnChatSendListener>() {
    var message: ChatMessage

    /**
     * Creates a new PacketSendEvent.
     */
    init {
        message = ChatMessage(msg)
    }

    override fun fire(listeners: ArrayList<OnChatSendListener>) {
        for (listener in listeners) {
            listener.onChatSend(message)
            if (message.isCancelled) cancel()
        }
    }

    override val event: Class<OnChatSendListener>
        get() = OnChatSendListener::class.java
}