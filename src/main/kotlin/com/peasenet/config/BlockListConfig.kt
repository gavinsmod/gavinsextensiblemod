package com.peasenet.config

import com.peasenet.annotations.Exclude
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import java.util.function.Consumer

open class BlockListConfig<E>(blockFilter: (it: Block?) -> Boolean = { false }) : Config<BlockListConfig<E>>() {
    @Exclude
    private var defaultList = emptyList<String>()

    init {
        val blocks = Registries.BLOCK.filter(blockFilter).toList()

        blocks.forEach {
            defaultList = defaultList.plus(getId(it))
        }
    }

    var blocks: HashSet<String> = HashSet()

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
        addBlock(id)
    }
    
    fun addBlock(b: String) {
        blocks.add(b)
        saveConfig()
    }

    /**
     * Sets the xray block list.
     *
     * @param list - the list to set to.
     */
    private fun setList(list: List<String>) {
        blocks.clear()
        list.forEach(Consumer { b: String -> addBlock(b) })
        saveConfig()
    }

    /**
     * Removes a block from the list.
     *
     * @param b - The block to remove.
     */
    fun removeBlock(b: Block) {
        val id = getId(b)
        removeBlock(id)
    }
    
    fun removeBlock(b: String) {
        blocks.remove(b)
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
            val path = b.lootTableKey.value.path
            return if (path == "empty") b.translationKey.replace("block.minecraft.", "") else path.replace(
                "blocks/",
                ""
            )
        }

    }
}