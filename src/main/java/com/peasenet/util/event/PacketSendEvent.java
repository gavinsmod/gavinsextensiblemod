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

import com.peasenet.util.event.data.OutputPacket;
import com.peasenet.util.listeners.PacketSendListener;
import net.minecraft.network.Packet;

import java.util.ArrayList;

/**
 * An event for when a packet is sent.
 *
 * @author GT3CH1
 * @version 12/22/2022
 */
public class PacketSendEvent extends CancellableEvent<PacketSendListener> {
    OutputPacket packet;

    /**
     * Creates a new PacketSendEvent.
     *
     * @param packet - The packet being sent.
     */
    public PacketSendEvent(Packet<?> packet) {
        this.packet = new OutputPacket(packet);
    }

    @Override
    public void fire(ArrayList<PacketSendListener> listeners) {
        for (PacketSendListener listener : listeners) {
            listener.onPacketSend(packet);
            if (packet.isCancelled())
                this.cancel();
        }
    }

    @Override
    public Class<PacketSendListener> getEvent() {
        return PacketSendListener.class;
    }
}