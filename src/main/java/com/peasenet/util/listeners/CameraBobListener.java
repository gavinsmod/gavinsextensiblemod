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
package com.peasenet.util.listeners;

import com.peasenet.util.event.CancellableEvent;

import java.util.ArrayList;

/**
 * A listener for packets being sent.
 *
 * @author GT3CH1
 * @version 12/22/2022
 */
public interface CameraBobListener extends Listener {
    /**
     * Called when a packet is sent.
     */
    void onCameraViewBob(CameraBob c);

    /**
     * An event for when a packet is sent.
     *
     * @author GT3CH1
     * @version 12/22/2022
     */
    class CameraBobEvent extends CancellableEvent<CameraBobListener> {
        CameraBob cameraBob;

        /**
         * Creates a new PacketSendEvent.
         */
        public CameraBobEvent() {
            this.cameraBob = new CameraBob();
        }

        @Override
        public void fire(ArrayList<CameraBobListener> listeners) {
            for (CameraBobListener listener : listeners) {
                listener.onCameraViewBob(cameraBob);
                if (cameraBob.isCancelled()) this.cancel();
            }
        }

        @Override
        public Class<CameraBobListener> getEvent() {
            return CameraBobListener.class;
        }
    }

    class CameraBob {
        private boolean cancelled = false;

        public boolean isCancelled() {
            return cancelled;
        }

        public void cancel() {
            this.cancelled = true;
        }
    }
}