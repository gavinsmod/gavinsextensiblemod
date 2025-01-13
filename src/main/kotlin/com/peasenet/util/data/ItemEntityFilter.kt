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
                PlayerUtils.sendMessage("Invalid regex! Disabling filter: ${filter.filterName}", true)
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
        if (customName != null) {
            val content = (customName.toString())
            matches = content.contains(filterString)
            matches = matches || (regex?.containsMatchIn(content) ?: false)
        }
        if (lore != null) {
            matches = matches || lore.lines.any { it.content.toString().contains(filterString) }
            matches = matches || lore.lines.any {
                regex?.containsMatchIn((it.content.toString())) ?: false
            }
        }
        return matches
    }
}
