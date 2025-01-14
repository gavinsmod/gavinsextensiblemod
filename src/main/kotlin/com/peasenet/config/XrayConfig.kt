/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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
package com.peasenet.config

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.minecraft.block.ExperienceDroppingBlock

/**
 * The configuration for xray.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class XrayConfig : BlockListConfig<XrayConfig>({ it is ExperienceDroppingBlock }) {

    /**
     * Whether to cull blocks.
     */
    var blockCulling = false
        set(value) {
            field = value
            if(!readMode)
                saveConfig()
        }

    init {
        key = "xray"
    }

    @Transient
    var readMode = false
}


class XrayConfigGsonAdapter : TypeAdapter<XrayConfig>() {
    override fun write(out: JsonWriter?, value: XrayConfig?) {
        // Write the block list
        out?.beginObject()
        out?.name("blocks")
        out?.beginArray()
        value?.blocks?.forEach {
            out?.value(it)
        }
        out?.endArray()
        out?.name("blockCulling")
        out?.value(value?.blockCulling)
        out?.endObject()
    }

    override fun read(reader: JsonReader?): XrayConfig {
        val config = XrayConfig()
        config.readMode = true
        reader?.beginObject()
        while (reader?.hasNext() == true) {
            val token = reader.peek()
            var fieldName = ""
            if (token.equals(JsonToken.NAME))
                fieldName = reader.nextName()

            when (fieldName) {
                "blocks" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        config.blocks.add(reader.nextString())
                    }
                    reader.endArray()
                }

                "blockCulling" -> {
                    config.blockCulling = reader.nextBoolean()
                }
            }
        }
        reader?.endObject()
        config.readMode = false
        return config
    }

}