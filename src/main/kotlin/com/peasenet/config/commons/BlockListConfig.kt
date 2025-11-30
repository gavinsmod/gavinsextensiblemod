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

package com.peasenet.config.commons

import com.peasenet.annotations.Exclude
import com.peasenet.config.Config
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import net.minecraft.world.level.block.Block
import net.minecraft.core.registries.BuiltInRegistries

/**
 * A configuration class for mods that need a list of blocks.
 * @param E The type of the configuration. Must extend Config.
 * @param blockFilter A filter for the blocks to add to the list.
 *
 * @author GT3CH1
 * @version 09-01-2024
 * @since 09-01-2024
 * @see Config
 */
open class BlockListConfig<E : Config<*>>(blockFilter: (it: Block) -> Boolean = { false }) :
    Config<BlockListConfig<E>>() {

    @Exclude
    /**
     * The default list of blocks, used to reset the list.
     */
    private var defaultList = emptyList<Block>()

    /**
     * The list of blocks that are in the configuration.
     */
    var blocks: HashSet<String> = HashSet()

    var blockColor: Color = Colors.DARK_SPRING_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var alpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }

    init {
        BuiltInRegistries.BLOCK.filter(blockFilter).toList().forEach {
            defaultList = defaultList.plus(it)
            blocks.add(getId(it))
        }

    }


    /**
     * Loads the default block list into the configuration, and saves it.
     */
    fun loadDefaultBlocks() {
        setList(defaultList)
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
        list.forEach { addBlock(it) }
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
        /**
         * Gets the name of the block, used to identify blocks to xray.
         *
         * @param b - The block to get the name of.
         * @return The name of the block.
         */
        fun getId(b: Block): String {

            val path = b.descriptionId
            return if (path == "empty") b.descriptionId.replace("block.minecraft.", "") else path.replace(
                "blocks/",
                ""
            )
        }
    }
}