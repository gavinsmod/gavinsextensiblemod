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
package com.peasenet.config

import net.minecraft.block.Block
import net.minecraft.block.ExperienceDroppingBlock
import net.minecraft.registry.Registries
import java.util.function.Consumer

/**
 * The configuration for xray.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class XrayConfig : Config<XrayConfig>() {
    /**
     * The list of blocks to xray.
     */
    private val blocks: HashSet<String>

    /**
     * Whether to cull blocks.
     */
    var blockCulling = false
        set(value) {
            field = value
            saveConfig()
        }

    init {
        key = "xray"
        blocks = HashSet()
        defaultBlockList.forEach { b: Block -> blocks.add(getId(b)) }
    }

    /**
     * Loads the default block list into the configuration, and saves it.
     */
    fun loadDefaultBlocks() {
        setList(defaultBlockList)
    }

    /**
     * Adds a block to the list.
     *
     * @param b - The block to add.
     */
    fun addBlock(b: Block) {
        val id = getId(b)
        blocks.add(id)
        saveConfig()
    }

    /**
     * Sets the xray block list.
     *
     * @param list - the list to set to.
     */
    private fun setList(list: List<Block>) {
        blocks.clear()
        list.forEach(Consumer { b: Block -> addBlock(b) })
        saveConfig()
    }

    /**
     * Removes a block from the list.
     *
     * @param b - The block to remove.
     */
    fun removeBlock(b: Block) {
        val id = getId(b)
        blocks.remove(id)
        saveConfig()
    }

    /**
     * Whether the block is in the list.
     *
     * @param b - The block to check.
     * @return Whether the block is in the list.
     */
    fun isInList(b: Block): Boolean {
        val id = getId(b)
        return blocks.contains(id)
    }

    companion object {
        private val defaultBlockList: List<Block>
            /**
             * Gets the default block list.
             *
             * @return The default block list.
             */
            get() = Registries.BLOCK.stream().filter { b: Block? -> b is ExperienceDroppingBlock }
                .toList()

        /**
         * Gets the name of the block, used to identify blocks to xray.
         *
         * @param b - The block to get the name of.
         * @return The name of the block.
         */
        private fun getId(b: Block): String {
            val path = b.lootTableId.path
            return if (path == "empty") b.translationKey.replace("block.minecraft.", "") else path.replace(
                "blocks/",
                ""
            )
        }
    }
}