package com.peasenet.util.data

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.ItemEntity
import net.minecraft.text.PlainTextContent
import java.util.*

/**
 * A dataclass that represents filters for item entities.
 *
 * @param filterString The string to filter by.
 * @param filterName The name of the filter.
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
    var filterString: String,
    var filterName: String = "",
    var enabled: Boolean = true,
    val uuid: UUID = UUID.randomUUID()
) {
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
        val regex = Regex(filterString, setOf(RegexOption.DOT_MATCHES_ALL))
        if (customName != null) {
            val content = (customName.content as PlainTextContent.Literal).string
            matches = content.contains(filterString)
            matches = matches || regex.containsMatchIn(content)
        }
        if (lore != null) {
            matches =
                matches || lore.lines.any { (it.content as PlainTextContent.Literal).string.contains(filterString) }
            matches =
                matches || lore.lines.any { regex.containsMatchIn((it.content as PlainTextContent.Literal).string) }
        }
        return matches
    }
}
