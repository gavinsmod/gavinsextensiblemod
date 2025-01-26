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

package com.peasenet.util.data

import com.peasenet.main.GavinsModClient
import com.peasenet.util.PlayerUtils
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.ItemEntity
import net.minecraft.text.PlainTextContent
import net.minecraft.text.Text
import java.util.*
import java.util.regex.PatternSyntaxException

/**
 * A dataclass that represents filters for item entities.
 *
 * @param filterName The name of the filter.
 * @param filterString The string to filter by.
 * @param enabled Whether the filter is enabled.
 * @param uuid The unique identifier of the filter.
 *
 * @sample [com.peasenet.mods.tracer.ModEntityItemTracer]]
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 01-12-2025
 */
class ItemEntityFilter(
    var filterName: String = "",
    var filterString: String,
    var searchCustomName: Boolean = true,
    var searchLore: Boolean = true,
    var enabled: Boolean = true,
    val uuid: UUID = UUID.randomUUID()
) {

    companion object {
        /**
         * Validates the given filter to make sure that the filter name and filter string are not empty, as well as the filter string is valid regex.
         * If the filter is invalid, this will set the `enabled` property to false and return false.
         *
         * @param filter The filter to validate.
         *
         * @return Whether the filter is valid.
         */
        fun validate(filter: ItemEntityFilter): Boolean {
            try {
                Regex(filter.filterString)
            } catch (e: PatternSyntaxException) {
                filter.enabled = false
                return false
            }
            return filter.filterString.isNotEmpty() && filter.filterName.isNotEmpty() && filter.filterString.isNotBlank() && filter.filterName.isNotBlank()
        }
    }

    /**
     * Checks if the custom name of an item entity matches the filter. If the filter is not enabled, this will return false.
     * This will use regex to check the [filterString] against the custom name and lore of the item entity.
     *
     * @param itemEntity The item entity to check.
     * @return Whether the custom name of the item entity matches the filter.
     */
    fun customNameMatches(itemEntity: ItemEntity): Boolean {
        if (!this.enabled) return false
        val stack = itemEntity.stack
        val customName = stack.get(DataComponentTypes.CUSTOM_NAME)
        val lore = stack.get(DataComponentTypes.LORE)
        var matches = false
        var regex: Regex? = null
        try {
            regex = Regex(filterString)
        } catch (e: PatternSyntaxException) {
            throw PatternSyntaxException(
                "Invalid regex! Did you change something manually and that broke it?", filterString, 0
            )
        }
        if (customName != null && searchCustomName) {
            val content = (customName.toString())
            matches = regex.containsMatchIn(content)
        }
        if (lore != null && searchLore) {
            matches = matches || lore.lines.any {
                regex.containsMatchIn((it.toString()))
            }
        }
        return matches
    }
}
